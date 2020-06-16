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

public class TestNot {
    private final BooleanLiteral boolTrueExpr = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr = new BooleanLiteral(false);
    private final IntLiteral intExpr1 = new IntLiteral(5);
    private final FloatLiteral floatExpr1 = new FloatLiteral(-742.221354f);
    private final StringLiteral stringExpr1 = new StringLiteral("bonsoir");

    private DecacCompiler compiler;

    @Before
    public void setup() {
        compiler = new DecacCompiler(null, null);
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        // true
        verifyExprWithSpecificParams(boolTrueExpr);

        // false
        verifyExprWithSpecificParams(boolFalseExpr);

    }

    private void verifyExprWithSpecificParams(BooleanLiteral NotOperand)
            throws ContextualError {
        // check verifyExpr and verifyCondition
        Not not = new Not(NotOperand);

        assertTrue(NotOperand.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(not.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testWrongConditionTypes() {
        // check that verifyCondition with a INT leftOperand throws contextualError
        Not not1 = new Not(intExpr1);

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "int" + ".", null);
        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            not1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyCondition with a FLOAT rightOperand throws contextualError
        Not not2 = new Not(floatExpr1);

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "float" + ".", null);
        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            not2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));


        // check that verifyCondition with a STRING rightOperand throws contextualError
        Not not3 = new Not(stringExpr1);

        ContextualError expected3 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + "string" + ".", null);
        ContextualError result3 = assertThrows(ContextualError.class, () -> {
            not3.verifyExpr(compiler, null, null);
        });

        assertThat(result3.getMessage(), is(expected3.getMessage()));
    }
}
