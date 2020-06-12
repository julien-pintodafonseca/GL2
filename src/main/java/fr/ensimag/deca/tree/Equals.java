package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Operator "x == y"
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    public void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        super.codeGenCMP(compiler, label, reverse);
        if (reverse) {
            compiler.addInstruction(new BNE(label));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
    }

}
