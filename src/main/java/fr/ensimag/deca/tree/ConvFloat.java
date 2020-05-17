package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    protected void codeGenInst(DecacCompiler compiler) {
        AbstractExpr fils = getOperand();

        if (fils instanceof Identifier) {
            VariableDefinition varDef = ((Identifier) fils).getVariableDefinition();
            compiler.addInstruction(new FLOAT(varDef.getOperand(), Register.R1));
        } else if (fils instanceof IntLiteral) {
            Float res = ((IntLiteral) fils).getValue() + 0.0f;
            compiler.addInstruction(new LOAD(new ImmediateFloat(res), Register.R1));
        } else {
            throw new UnsupportedOperationException("not yet implemented");
        }

    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
