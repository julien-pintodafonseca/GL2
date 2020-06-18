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
public class TestGreater {
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
        Greater greater = new Greater(intexpr1, intexpr2);
        // check the result
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(greater.getRightOperand() instanceof ConvFloat);
        assertFalse(greater.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError, DecacFatalError {
        Greater greater = new Greater(intexpr1, floatexpr2);
        // check the result
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(greater.getLeftOperand() instanceof ConvFloat);
        assertFalse(greater.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError, DecacFatalError {
        Greater greater = new Greater(floatexpr1, intexpr2);
        // check the result
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should have been inserted on the right side
        assertTrue(greater.getRightOperand() instanceof ConvFloat);
        assertFalse(greater.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatFloat() throws ContextualError, DecacFatalError {
        Greater greater = new Greater(floatexpr1, floatexpr2);
        // check the result
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
        // ConvFloat should not have been inserted
        assertFalse(greater.getRightOperand() instanceof ConvFloat);
        assertFalse(greater.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(floatexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testWrongTypes() throws ContextualError {
        // check that verifyExpr with a STRING leftOperand throws contextualError
        Greater greater1 = new Greater(stringExpr1, anyFloatExpr);

        Type tA1 = stringExpr1.verifyExpr(compiler, null, null);
        Type tA2 = anyFloatExpr.verifyExpr(compiler, null, null);
        assertTrue(tA1.isString());
        assertTrue(tA2.isFloat());

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tA1 + " (pour " +
                        stringExpr1.decompile() + ") et " + tA2 + " (pour " + anyFloatExpr.decompile() + ").", null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            greater1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyExpr with a BOOLEAN rightOperand throws contextualError
        Greater greater2 = new Greater(anyFloatExpr, booleanExpr2);

        Type tB1 = anyFloatExpr.verifyExpr(compiler, null, null);
        Type tB2 = booleanExpr2.verifyExpr(compiler, null, null);
        assertTrue(tB1.isFloat());
        assertTrue(tB2.isBoolean());

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + tB1 + " (pour " +
                        anyFloatExpr.decompile() + ") et " + tB2 + " (pour " + booleanExpr2.decompile() + ").", null);

        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            greater2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));
    }
}
