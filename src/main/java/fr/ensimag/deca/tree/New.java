package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author Equipe GL2
 * @date 2020
 */
public class New extends AbstractExpr {
    private AbstractIdentifier ident;

    public AbstractIdentifier getIdent() {
        return ident;
    }

    public New(AbstractIdentifier ident) {
        Validate.notNull(ident);
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Règle syntaxe contextuelle : (3.42)
        Type temp=ident.verifyType(compiler);
        if (temp.isClass()) {
            setType(temp);
            return temp;
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_TYPE_FOR_NEW + temp + ".", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        compiler.addComment("new ligne "+getLocation().getLine());
        compiler.addInstruction(new NEW(ident.getClassDefinition().getNumberOfFields() + 1, register));
        compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_FULL_HEAP))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_FULL_HEAP);
        compiler.addInstruction(new LEA(ident.getClassDefinition().getOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)));
        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new BSR(new Label("init." + ident.getName())));
        compiler.addInstruction(new POP(register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iterChildren(f);
    }
}
