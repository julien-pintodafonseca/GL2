package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        int j = compiler.getRegisterManager().nextAvailable();
        if (j != -1) {
            getOperand().codeGenInst(compiler);
            compiler.addInstruction(new FLOAT(Register.getR(j), register));
            compiler.getRegisterManager().free(j);
        } else {
            throw new DecacFatalError("not yet implemented");
        }
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
