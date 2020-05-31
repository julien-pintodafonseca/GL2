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
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        if (reverse) {
            // expr1 || expr2
            int i = compiler.getLabelManager().getLabelValue(LabelType.LB_OR);
            compiler.getLabelManager().getLabelValue(LabelType.LB_OR);
            Label labelBegin = new Label("or" + i);
            Label labelEnd = new Label("or_end" + i);

            getLeftOperand().codeGenCMP(compiler, labelBegin, true);
            compiler.addInstruction(new BRA(labelEnd));
            compiler.addLabel(labelBegin);
            getRightOperand().codeGenCMP(compiler, label, true);
            compiler.addLabel(labelEnd);
        } else {
            // not(expr1 || expr2) => not(expr1) && not(expr2)
            getLeftOperand().codeGenCMP(compiler, label, false);
            getRightOperand().codeGenCMP(compiler, label, false);
        }
    }

}
