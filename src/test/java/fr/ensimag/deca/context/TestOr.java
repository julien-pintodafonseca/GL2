package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.tree.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestOr {
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);
    private final IntLiteral intExpr1 = new IntLiteral(5);
    private final FloatLiteral floatExpr2 = new FloatLiteral(-742.221354f);
    private final StringLiteral stringExpr2 = new StringLiteral("bonsoir");

    private DecacCompiler compiler;

    @Before
    public void setup() {
        compiler = new DecacCompiler(null, null);
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        // true && true
        verifyExprWithSpecificParams(boolTrueExpr1, boolTrueExpr2);

        // true && false
        verifyExprWithSpecificParams(boolTrueExpr1, boolFalseExpr2);

        // false && true
        verifyExprWithSpecificParams(boolFalseExpr1, boolTrueExpr2);

        // false && false
        verifyExprWithSpecificParams(boolFalseExpr1, boolFalseExpr2);
    }

    private void verifyExprWithSpecificParams(BooleanLiteral leftOperand, BooleanLiteral rightOperand)
            throws ContextualError {
        // check verifyExpr and verifyCondition
        Or or = new Or(leftOperand, rightOperand);

        assertTrue(leftOperand.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(rightOperand.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(or.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testWrongConditionTypes() {
        // check that verifyCondition with a INT leftOperand throws contextualError
        Or or1 = new Or(intExpr1, boolTrueExpr2);

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "int" + ".", null);
        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            or1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyCondition with a FLOAT rightOperand throws contextualError
        Or or2 = new Or(boolFalseExpr1, floatExpr2);

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "float" + ".", null);
        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            or2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));


        // check that verifyCondition with a STRING rightOperand throws contextualError
        Or or3 = new Or(boolFalseExpr1, stringExpr2);

        ContextualError expected3 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "string" + ".", null);
        ContextualError result3 = assertThrows(ContextualError.class, () -> {
            or3.verifyExpr(compiler, null, null);
        });

        assertThat(result3.getMessage(), is(expected3.getMessage()));
    }
}

