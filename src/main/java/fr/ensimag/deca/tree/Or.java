package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label) throws DecacFatalError {
        int i = compiler.getLabelManager().getLabelValue(LabelType.LB_OR);
        Label labelBegin = new Label("or" + i);
        Label labelEnd = new Label("or_end" + i);
        getLeftOperand().codeGenCMP(compiler, labelBegin);
        compiler.addInstruction(new BRA(labelEnd));
        compiler.addLabel(labelBegin);
        getRightOperand().codeGenCMP(compiler, label);
        compiler.addLabel(labelEnd);
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler, Label label) throws DecacFatalError {
        getLeftOperand().codeGenCMPNot(compiler, label);
        getRightOperand().codeGenCMPNot(compiler, label);
    }
}
