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


}
