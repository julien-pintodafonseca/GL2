package fr.ensimag.deca.tree;

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

}
