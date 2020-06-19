package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestEquals {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);
    private final FloatLiteral anyFloatExpr = new FloatLiteral(-99999.999f);
    private final StringLiteral stringExpr1 = new StringLiteral("bonsoir");
    private final BooleanLiteral booleanExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;

    private DecacCompiler compiler;

    @Before
    public void setup() throws DecacFatalError, ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);

        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError, DecacFatalError {
        Equals equals = new Equals(intexpr1, intexpr2);
        // check the result
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(equals.getRightOperand() instanceof ConvFloat);
        assertFalse(equals.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError, DecacFatalError {
        Equals equals = new Equals(intexpr1, floatexpr2);
        // check the result
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(equals.getLeftOperand() instanceof ConvFloat);
        assertFalse(equals.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError, DecacFatalError {
        Equals equals = new Equals(floatexpr1, intexpr2);
        // check the result
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(equals.getRightOperand() instanceof ConvFloat);
        assertFalse(equals.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatFloat() throws ContextualError, DecacFatalError {
        Equals equals = new Equals(floatexpr1, floatexpr2);
        // check the result
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(equals.getRightOperand() instanceof ConvFloat);
        assertFalse(equals.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testWrongTypes() throws ContextualError {
        // check that verifyExpr with a STRING leftOperand throws contextualError
        Equals equals1 = new Equals(stringExpr1, anyFloatExpr);

        Type tA1 = stringExpr1.verifyExpr(compiler, null, null);
        Type tA2 = anyFloatExpr.verifyExpr(compiler, null, null);
        assertTrue(tA1.isString());
        assertTrue(tA2.isFloat());

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tA1 + " (pour " +
                        stringExpr1.decompile() + ") et " + tA2 + " (pour " + anyFloatExpr.decompile() + ").", null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            equals1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyExpr with a BOOLEAN rightOperand throws contextualError
        Equals equals2 = new Equals(anyFloatExpr, booleanExpr2);

        Type tB1 = anyFloatExpr.verifyExpr(compiler, null, null);
        Type tB2 = booleanExpr2.verifyExpr(compiler, null, null);
        assertTrue(tB1.isFloat());
        assertTrue(tB2.isBoolean());

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tB1 + " (pour " +
                        anyFloatExpr.decompile() + ") et " + tB2 + " (pour " + booleanExpr2.decompile() + ").", null);

        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            equals2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));
    }

    @Test
    public void testVerifyExpr() throws ContextualError, DecacFatalError {
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
            throws ContextualError, DecacFatalError {
        // check verifyExpr and verifyCondition
        And and = new And(leftOperand, rightOperand);

        assertTrue(leftOperand.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(rightOperand.verifyExpr(compiler, null, null).isBoolean());
        assertTrue(and.verifyExpr(compiler, null, null).isBoolean());
    }

    // TODO
}
