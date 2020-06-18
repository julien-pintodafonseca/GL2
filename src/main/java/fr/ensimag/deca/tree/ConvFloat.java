package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
    	  // Règle syntaxe contextuelle : (3.37)
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
            getOperand().codeGenInst(compiler, register);
            compiler.addInstruction(new FLOAT(register, Register.R1));
            Label lb = new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_CONV_FLOAT));
            compiler.addInstruction(new BOV(lb));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_CONV_FLOAT);
            compiler.addInstruction(new LOAD(Register.R1, register));
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
}
