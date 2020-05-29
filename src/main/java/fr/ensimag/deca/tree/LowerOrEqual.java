package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;

/**
 * Operator x <= y
 * @author Equipe GL2
 * @date 2020
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label) throws DecacFatalError {
        super.codeGenCMP(compiler, label);
        compiler.addInstruction(new BGT(label));
    }
    
    @Override
    protected void codeGenCMPNot(DecacCompiler compiler, Label label) throws DecacFatalError {
    	super.codeGenCMP(compiler, label);
        compiler.addInstruction(new BLE(label));
    }

}
