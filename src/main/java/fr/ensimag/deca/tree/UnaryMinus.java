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
        getOperand().codeGenInst(compiler, Register.R0);
        compiler.addInstruction(new OPP(Register.R0, register));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
}
