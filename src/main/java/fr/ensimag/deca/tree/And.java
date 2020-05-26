package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label) throws DecacFatalError {
        getLeftOperand().codeGenCMP(compiler, label);
        getRightOperand().codeGenCMP(compiler, label);
    }
}
