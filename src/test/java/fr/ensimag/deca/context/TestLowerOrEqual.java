package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.tree.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestLowerOrEqual {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);
    private final FloatLiteral anyFloatExpr = new FloatLiteral(-99999.999f);
    private final StringLiteral stringExpr1 = new StringLiteral("bonsoir");
    private final BooleanLiteral booleanExpr2 = new BooleanLiteral(true);

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
        LowerOrEqual lowerOrEqual = new LowerOrEqual(intexpr1, intexpr2);
        // check the result
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(lowerOrEqual.getRightOperand() instanceof ConvFloat);
        assertFalse(lowerOrEqual.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError, DecacFatalError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(intexpr1, floatexpr2);
        // check the result
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(lowerOrEqual.getLeftOperand() instanceof ConvFloat);
        assertFalse(lowerOrEqual.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError, DecacFatalError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(floatexpr1, intexpr2);
        // check the result
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(lowerOrEqual.getRightOperand() instanceof ConvFloat);
        assertFalse(lowerOrEqual.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatFloat() throws ContextualError, DecacFatalError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(floatexpr1, floatexpr2);
        // check the result
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(lowerOrEqual.getRightOperand() instanceof ConvFloat);
        assertFalse(lowerOrEqual.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testWrongTypes() throws ContextualError {
        // check that verifyExpr with a STRING leftOperand throws contextualError
        LowerOrEqual lowerOrEqual1 = new LowerOrEqual(stringExpr1, anyFloatExpr);

        Type tA1 = stringExpr1.verifyExpr(compiler, null, null);
        Type tA2 = anyFloatExpr.verifyExpr(compiler, null, null);
        assertTrue(tA1.isString());
        assertTrue(tA2.isFloat());

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tA1 + " (pour " +
                        stringExpr1.decompile() + ") et " + tA2 + " (pour " + anyFloatExpr.decompile() + ").", null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            lowerOrEqual1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyExpr with a BOOLEAN rightOperand throws contextualError
        LowerOrEqual lowerOrEqual2 = new LowerOrEqual(anyFloatExpr, booleanExpr2);

        Type tB1 = anyFloatExpr.verifyExpr(compiler, null, null);
        Type tB2 = booleanExpr2.verifyExpr(compiler, null, null);
        assertTrue(tB1.isFloat());
        assertTrue(tB2.isBoolean());

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tB1 + " (pour " +
                        anyFloatExpr.decompile() + ") et " + tB2 + " (pour " + booleanExpr2.decompile() + ").", null);

        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            lowerOrEqual2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));
    }
}
