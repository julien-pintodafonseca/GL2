package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class UnaryMinus extends AbstractUnaryExpr {
    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.37)
        getOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t = getOperand().getType();
        if (t.isFloat() || t.isInt()) {
            setType(t);
            return t;
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_UNARY_MINUS_INCOMPATIBLE_TYPE + t + ".", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        getOperand().codeGenInst(compiler, register);
        compiler.addInstruction(new OPP(register, Register.R1));
        compiler.addInstruction(new LOAD(Register.R1, register));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            getOperand().codeGenInst(compiler, Register.getR(i));
            compiler.addInstruction(new OPP(Register.getR(i), Register.R1));
            super.codeGenPrint(compiler, printHex);
            compiler.getRegisterManager().free(i);
        } else {
            int j = compiler.getRegisterManager().getSize() -1;
            compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 1 registre
            compiler.getTSTOManager().addCurrent(1);
            getOperand().codeGenInst(compiler, Register.getR(j));
            compiler.addInstruction(new OPP(Register.getR(j), Register.R1));
            super.codeGenPrint(compiler, printHex);
            compiler.addInstruction(new POP(Register.getR(j))); // restauration du registre
            compiler.getTSTOManager().addCurrent(-1);
        }
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
}
