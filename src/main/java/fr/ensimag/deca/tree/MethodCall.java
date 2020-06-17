package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import javax.management.ListenerNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class MethodCall extends AbstractExpr {
    AbstractExpr obj;
    AbstractIdentifier meth;
    ListExpr params;

    public MethodCall (AbstractExpr obj, AbstractIdentifier meth, ListExpr params) {
        Validate.notNull(meth);
        Validate.notNull(params);
        this.obj = obj;
        this.meth = meth;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Syntaxe contextuelle : règles (3.71), (3.72), (3.73) et (3.74)
        obj.verifyExpr(compiler, localEnv, currentClass);
        if (obj.getType() != null) {
            Type typeClass = obj.getType();
            if (typeClass.isClass()) {
                ClassDefinition classDef = compiler.environmentType.getClassDefinition(typeClass.getName());
                meth.verifyExpr(compiler, classDef.getMembers(), currentClass);
                Signature signMeth = meth.getMethodDefinition().getSignature();
                int paramNumber = 0;
                int numberOfParam = signMeth.size();
                for (AbstractExpr expr : params.getList()) {
                    if (paramNumber < numberOfParam) {
                        params.set(paramNumber, expr.verifyRValue(compiler, localEnv, currentClass, signMeth.paramNumber(paramNumber)));
                        paramNumber++;
                    } else if (numberOfParam == 0) {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_NO_PARAM_EXPECTED + meth.getName() + ".", getLocation());
                    } else {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_MORE_OR_LESS_PARAM_EXPECTED + numberOfParam + " paramètre(s) au lieu de " + params.getList().size() + " paramètre(s) : " + meth.getName() + ".", getLocation());
                    }
                }
                if (paramNumber != numberOfParam) {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_MORE_OR_LESS_PARAM_EXPECTED + numberOfParam + " paramètre(s) au lieu de " + params.getList().size() + " paramètre(s) : " + meth.getName() + ".", getLocation());
                }

                setType(meth.getType());
                return meth.getType();
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_EXPR_IS_NOT_CLASS + obj.decompile() + " est de type " + obj.getType() + ".", getLocation());
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_WITHOUT_CLASS + meth.getName() + ".", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        // Cas spécifique où il faut stocker le résultat retourné par la méthode appellée dans le registre register
        this.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.R0, register));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("Appel de la méthode : " + this.decompile() + " ligne " + getLocation().getLine());

        // On reserve de la place pour 1 + nombre de paramètres
        compiler.addInstruction(new ADDSP(params.size()+1));
        compiler.getTSTOManager().addCurrent(params.size()+1);

        // On récupère le param implicite
        obj.codeGenInst(compiler, Register.R1);
        // On empile le paramètre implicite (obj)
        compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(0,Register.SP)));

        // On empile les paramètres
        int i = -1;
        for (AbstractExpr param : params.getList()) {
            param.codeGenInst(compiler, Register.R1);
            compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(i, Register.SP)));
            i--;
        }

        //on recupère le paramètre implicite
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.SP), Register.R1));
        //test si null
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_NULL_DEREFERENCEMENT))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_NULL_DEREFERENCEMENT);

        // Récupère adresse de la table des méthodes
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R1), Register.R1));
        // Appel de la méthode
        ClassDefinition def =compiler.environmentType.getClassDefinition(obj.getType().getName());
        MethodDefinition methDef = (MethodDefinition) def.getMembers().get(meth.getName());
        compiler.getTSTOManager().addCurrent(2);
        compiler.addInstruction(new BSR(new RegisterOffset(methDef.getIndex(), Register.R1)));
        compiler.getTSTOManager().addCurrent(-2);

        // Dépile les paramètres
        compiler.addInstruction(new SUBSP(params.size()+1));
        compiler.getTSTOManager().addCurrent(-(params.size()+1));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) throws DecacFatalError {
        codeGenInst(compiler, Register.R1);
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (obj != null) {
            obj.decompile(s);
        }
        s.print('.');
        meth.decompile(s);
        s.print('(');
        params.decompile(s);
        s.print(')');
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (obj != null) {
            obj.prettyPrint(s, prefix, false);
        }
        meth.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (obj != null) {
            obj.iter(f);
        }
        meth.iter(f);
        params.iter(f);
    }
}
