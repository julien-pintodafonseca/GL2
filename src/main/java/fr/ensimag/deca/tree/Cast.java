package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
        // Il faut distinguer les différents cas possibles.
        if ((type.getType().isFloat() || type.getType().isInt() || type.getType().isBoolean()) && type.getType().sameType(expr.getType()))
            // 1. Conversion de la forme (T)(e) où T est le type boolean, int ou float et e est une expression de type T.
            // Il suffit de coder l’expression, la conversion est l’opération identité.
            expr.codeGenInst(compiler, register);
        if (type.getType().isInt() && expr.getType().isFloat()) { // cast de int -> float
            // 2. Conversion de la forme (float)(e), où e est une expression de type int. On code l’expression, puis on effectue une opération FLOAT.
            expr.codeGenInst(compiler, Register.R1);
            compiler.addInstruction(new FLOAT(Register.R1, register));
            compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CONV_FLOAT))));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CONV_FLOAT);
        } else if (type.getType().isInt() && expr.getType().isFloat()) { // cast de float -> int
            // 3. Conversion de la forme (int)(e), où e est une expression de type float. On code l’expression, puis on effectue une opération INT.
            expr.codeGenInst(compiler, Register.R1);
            compiler.addInstruction(new INT(Register.R1, register));
            compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CAST_INT))));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CAST_INT);
        } else {
            // 4. Conversion de la forme (B)(a), où B est une classe et a une expression d’un type correspondant à une classe A.
            // On teste si a est une instance de B, ou bien la valeur null. Si c’est le cas, l’expression (B)(a) a pour valeur a.
            // Sinon, la conversion est incorrecte : on affiche un message d’erreur et le programme s’arrête.

            int i = compiler.getLabelManager().getLabelValue(LabelType.LB_CAST_POSSIBLE);
            compiler.getLabelManager().incrLabelValue(LabelType.LB_CAST_POSSIBLE);
            Label suite = new Label("cast" + i);

            expr.codeGenInst(compiler, register);
            compiler.addInstruction(new CMP(new NullOperand(), register));
            compiler.addInstruction(new BEQ(suite)); // si expr.getType().isNull(), on exécute la suite des instructions

            // si !expr.getType().isNull(), on teste si expr instanceof type
            InstanceOf instanceOf = new InstanceOf(expr, type);
            Label lbError = new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CAST_IMPOSSIBLE) + i);
            instanceOf.codeGenCMP(compiler, lbError, true); // si !(expr instanceof type), on saute au label d'erreur
            //compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CAST_IMPOSSIBLE);

            compiler.addInstruction(new BRA(suite));

            compiler.addLabel(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CAST_IMPOSSIBLE) + i));
            compiler.addInstruction(new WSTR("Erreur: cast impossible de " + expr.getType().toString() + " vers " + type.getName().toString()));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(suite);
            /*expr.codeGenInst(compiler, register);
            if(register == Register.R0) {
                compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R1));
                compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(0, register)));
            } else {
                compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R0));
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, register)));
            }*/
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
