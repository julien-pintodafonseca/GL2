package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.33)
        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // Syntaxe contextuelle : signature des opérateurs
        if (t1.isInt() && t2.isInt()) {
            Type t = compiler.environmentType.INT;
            setType(t);
            return t;
        } else if ((t1.isInt() || t1.isFloat()) && (t2.isInt() || t2.isFloat())) {
            if (t1.isInt() && t2.isFloat()) {
                ConvFloat conv = new ConvFloat(getLeftOperand());
                conv.setType(t2);
                setLeftOperand(conv);
            } else if (t1.isFloat() && t2.isInt()) {
                ConvFloat conv = new ConvFloat(getRightOperand());
                conv.setType(t1);
                setRightOperand(conv);
            }
            Type t = compiler.environmentType.FLOAT;
            setType(t);
            return t;
        } else {
            throw new ContextualError( ErrorMessages.CONTEXTUAL_ERROR_ARITHMETIC_OPERATION_INCOMPATIBLE_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            getLeftOperand().codeGenInst(compiler, Register.getR(i));
            getRightOperand().codeGenInst(compiler, register);

            codeGenInstArith(compiler, register, Register.getR(i));
            compiler.addInstruction(new LOAD(Register.getR(i), register));
            compiler.getRegisterManager().free(i);
        } else {
            int j = compiler.getRegisterManager().getSize()-1;
            if (Register.getR(j) == register) {
                j = j-1;
            }
            compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 1 registre
            compiler.getTSTOManager().addCurrent(1);

            getLeftOperand().codeGenInst(compiler, Register.getR(j));
            getRightOperand().codeGenInst(compiler, register);

            codeGenInstArith(compiler, register, Register.getR(j));
            compiler.addInstruction(new LOAD(Register.getR(j), register));

            compiler.addInstruction(new POP(Register.getR(j))); // restauration du registre
            compiler.getTSTOManager().addCurrent(-1);
        }
    }

    protected void codeGenError(DecacCompiler compiler) throws DecacFatalError {
        // test débordement arithmétique sur les flottants ou division par zéro
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_ARITHMETIC_OVERFLOW);
        compiler.addInstruction(new BOV(new Label("" + compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_ARITHMETIC_OVERFLOW))), "Overflow : check for previous operation");
    }

    protected abstract void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) throws DecacFatalError;
}
