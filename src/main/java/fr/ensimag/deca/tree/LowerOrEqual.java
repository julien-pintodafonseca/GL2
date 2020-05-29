package fr.ensimag.deca.tree;

/**
 * Operator "x <= y"
 *
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

}
