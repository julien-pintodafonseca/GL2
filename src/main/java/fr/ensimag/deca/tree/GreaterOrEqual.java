package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 * Operator "x >= y"
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }
    
    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        super.codeGenCMP(compiler, label, reverse);
        if (reverse) {
            compiler.addInstruction(new BLT(label));
        } else {
            compiler.addInstruction(new BGE(label));
        }
    }
}
