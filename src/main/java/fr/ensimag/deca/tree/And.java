package fr.ensimag.deca.tree;

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
    	
    @Override
    protected void codeGenCMPNot(DecacCompiler compiler, Label label) throws DecacFatalError {
        // not(expr1 && expr2) => not(expr1) || not(expr2)
    	int i = compiler.getLabelManager().getLabelValue(LabelType.LB_OR);
    	compiler.getLabelManager().incrLabelValue(LabelType.LB_OR);
        Label labelBegin = new Label("or" + i);
        Label labelEnd = new Label("or_end" + i);
        getLeftOperand().codeGenCMPNot(compiler, labelBegin);
        compiler.addInstruction(new BRA(labelEnd));
        compiler.addLabel(labelBegin);
        getRightOperand().codeGenCMPNot(compiler, label);
        compiler.addLabel(labelEnd);
    }
    
}
