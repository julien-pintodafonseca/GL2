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
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Single precision, floating-point literal
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Cast extends AbstractExpr {
    AbstractIdentifier type;
    AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.39)
        Type t1 = type.verifyType(compiler);
        Type t2 = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!t2.isVoid()) {
            if (compiler.environmentType.assignCompatible(t1, t2) || compiler.environmentType.assignCompatible(t2, t1)) {
                setType(t1);
                return t1;
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CAST_INCOMPATIBLE_TYPE + t1 + " : " + expr.decompile() + " (type " + t2 + ").", getLocation());
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CAST_VOID_TYPE + expr.decompile() + ".", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        if (type.getType().isInt() && expr.getType().isFloat()) { // cast de int -> float
            expr.codeGenInst(compiler, Register.R1);
            compiler.addInstruction(new FLOAT(Register.R1, register));
            compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CONV_FLOAT))));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CONV_FLOAT);
        } else if (type.getType().isInt() && expr.getType().isFloat()) { // cast de float -> int
            expr.codeGenInst(compiler, Register.R1);
            compiler.addInstruction(new INT(Register.R1, register));
            compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CAST_INT))));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CAST_INT);
        } else if (!type.getType().sameType(expr.getType())) {
            // TODO

        } else {
            expr.codeGenInst(compiler, register);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
}
