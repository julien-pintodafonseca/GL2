package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.mockito.internal.matchers.Null;

import java.io.PrintStream;

/**
 * Single precision, floating-point literal
 *
 * @author Equipe GL2
 * @date 2020
 */
public class InstanceOf extends AbstractExpr {
    AbstractExpr expr;
    AbstractIdentifier type;

    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        Validate.notNull(expr);
        Validate.notNull(type);
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.40)
        Type t1 = expr.verifyExpr(compiler, localEnv, currentClass);
        Type t2 = type.verifyType(compiler);
        if (t1.isClassOrNull()) {
            if (t2.isClass()) {
                setType(compiler.environmentType.BOOLEAN);
                return compiler.environmentType.BOOLEAN;
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INSTANCEOF_NOT_CLASS + t2 + ".", getLocation());
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INSTANCEOF_NOT_NULL_OR_CLASS + t2 + " : " + expr.decompile() + ".", getLocation());
        }
    }

    // At run time, the result of the instanceof operator is true if the value of expr is not null
    // and expr could be cast to type without raising a ClassCastException. Otherwise the result is false.
    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        int x = compiler.getLabelManager().getLabelValue(LabelType.LB_RETURN_TRUE);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_RETURN_TRUE);
        Label labelReturnTrue = new Label("true" + x);
        Label labelAfterReturnTrue = new Label("after_true" + x);

        int k = compiler.getRegisterManager().nextAvailable();
        if (k != -1) {
            compiler.getRegisterManager().take(k);
            codeGenIn(compiler, labelReturnTrue, Register.getR(k));
            compiler.getRegisterManager().free(k);
        } else {
            int y = compiler.getRegisterManager().getSize() -1;
            compiler.addInstruction(new PUSH(Register.getR(y))); // chargement dans la pile de 1 registre
            compiler.getTSTOManager().addCurrent(1);

            codeGenIn(compiler, labelReturnTrue, Register.getR(y));

            compiler.addInstruction(new POP(Register.getR(y))); // restauration du registre
            compiler.getTSTOManager().addCurrent(-1);
        }

        if (reverse) { // correspond à (expr instanceof type)
            compiler.addInstruction(new BRA(label)); // si le résultat de l'instance est faux, on exécute la branche fausse commençant au label label
            // sinon, on ne fait rien
            compiler.addLabel(labelReturnTrue);
        } else { // correspond à not(expr instanceof type)
            compiler.addInstruction(new BRA(labelAfterReturnTrue));

            compiler.addLabel(labelReturnTrue);
            compiler.addInstruction(new BRA(label));

            compiler.addLabel(labelAfterReturnTrue);
        }
    }

    private void codeGenIn(DecacCompiler compiler, Label labelReturnTrue, GPRegister register) throws DecacFatalError {
        int i = compiler.getLabelManager().getLabelValue(LabelType.LB_WHILE);
        Label labelWhileBegin = new Label("while" + i);
        Label labelWhileEnd = new Label("while_end" + i);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_WHILE);
        int j = compiler.getLabelManager().getLabelValue(LabelType.LB_ELSE);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_ELSE);
        Label labelIfEnd = new Label("end_if" + j);

        // Avant boucle while, superClass = expr
        expr.codeGenInst(compiler, register);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register)); // Récupération du type dynamique

        compiler.addLabel(labelWhileBegin); // while(superClass != null) {
        compiler.addInstruction(new CMP(new NullOperand(), register));
        compiler.addInstruction(new BEQ(labelWhileEnd));

        //if (superClass == type) {
        if (register == Register.R0) {
            compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R1));
            compiler.addInstruction(new CMP(Register.R1, register));
        } else {
            compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R0));
            compiler.addInstruction(new CMP(Register.R0, register));
        }
        compiler.addInstruction(new BNE(labelIfEnd));

        // Branche then
        compiler.addInstruction(new BRA(labelReturnTrue));

        // Label de fin du if
        compiler.addLabel(labelIfEnd);

        // superClass = superClass.superClass()
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));

        compiler.addInstruction(new BRA(labelWhileBegin));
        compiler.addLabel(labelWhileEnd);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        int x = compiler.getLabelManager().getLabelValue(LabelType.LB_RETURN_TRUE);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_RETURN_TRUE);
        Label labelReturnTrue = new Label("true" + x);
        Label labelAfterReturnTrue = new Label("after_true" + x);

        codeGenIn(compiler, labelReturnTrue, register);

        // Retourne faux
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(new BRA(labelAfterReturnTrue));

        // Retourne true
        compiler.addLabel(labelReturnTrue);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
        compiler.addLabel(labelAfterReturnTrue);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        expr.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        type.iter(f);
    }
}
