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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestModulo {
    private final DecacCompiler compiler = new DecacCompiler(null, null);
    private final Type INT = compiler.environmentType.INT;
    private final Type FLOAT = compiler.environmentType.FLOAT;
    private final FloatLiteral anyFloatExpr = new FloatLiteral(-99999.999f);
    private final StringLiteral stringExpr1 = new StringLiteral("bonsoir");
    private final BooleanLiteral booleanExpr2 = new BooleanLiteral(true);

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(intexpr1.getType()).thenReturn(INT);
        when(intexpr2.getType()).thenReturn(INT);
        when(floatexpr1.getType()).thenReturn(FLOAT);
        when(floatexpr2.getType()).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError, DecacFatalError {
        Modulo modulo = new Modulo(intexpr1, intexpr2);
        // check the result
        assertTrue(modulo.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError, DecacFatalError {
        Modulo modulo = new Modulo(intexpr1, floatexpr1);

        Type tA1 = intexpr1.verifyExpr(compiler, null, null);
        Type tA2 = floatexpr1.verifyExpr(compiler, null, null);
        assertTrue(tA1.isInt());
        assertTrue(tA2.isFloat());

        // Levée d'une erreur si une des opérandes n'est pas un entier
        ContextualError resultModuloFloat = assertThrows(ContextualError.class, () -> {
            modulo.verifyExpr(compiler, null, null);
        });
        assertThat(resultModuloFloat.getMessage(), is(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE +
                tA1 + " (pour " + floatexpr1.decompile() + ") et " + tA2 + " (pour " + intexpr1.decompile() + ")."));
    }

    @Test
    public void testFloatInt() throws ContextualError, DecacFatalError {
        Modulo modulo = new Modulo(floatexpr1, intexpr1);

        Type tA1 = floatexpr1.verifyExpr(compiler, null, null);
        Type tA2 = intexpr1.verifyExpr(compiler, null, null);
        assertTrue(tA1.isFloat());
        assertTrue(tA2.isInt());

        // Levée d'une erreur si une des opérandes n'est pas un entier
        ContextualError resultModuloFloat = assertThrows(ContextualError.class, () -> {
            modulo.verifyExpr(compiler, null, null);
        });
        assertThat(resultModuloFloat.getMessage(), is(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE +
                tA1 + " (pour " + floatexpr1.decompile() + ") et "+ tA2 + " (pour "+ intexpr1.decompile() + ")."));
    }

    @Test
    public void testWrongTypes() throws ContextualError {
        // check that verifyExpr with a STRING leftOperand throws contextualError
        Modulo modulo1 = new Modulo(stringExpr1, anyFloatExpr);

        Type tA1 = stringExpr1.verifyExpr(compiler, null, null);
        Type tA2 = anyFloatExpr.verifyExpr(compiler, null, null);
        assertTrue(tA1.isString());
        assertTrue(tA2.isFloat());

        ContextualError expected1 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE
                        + tA1 + " (pour " + stringExpr1.decompile() + ") et " + tA2
                        + " (pour " + anyFloatExpr.decompile() + ").", null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> {
            modulo1.verifyExpr(compiler, null, null);
        });

        assertThat(result1.getMessage(), is(expected1.getMessage()));


        // check that verifyExpr with a BOOLEAN rightOperand throws contextualError
        Modulo modulo2 = new Modulo(anyFloatExpr, booleanExpr2);

        Type tB1 = anyFloatExpr.verifyExpr(compiler, null, null);
        Type tB2 = booleanExpr2.verifyExpr(compiler, null, null);
        assertTrue(tB1.isFloat());
        assertTrue(tB2.isBoolean());

        ContextualError expected2 =
                new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE
                        + tB1 + " (pour " + anyFloatExpr.decompile() + ") et " + tB2
                        + " (pour " + booleanExpr2.decompile() + ").", null);

        ContextualError result2 = assertThrows(ContextualError.class, () -> {
            modulo2.verifyExpr(compiler, null, null);
        });

        assertThat(result2.getMessage(), is(expected2.getMessage()));
    }
}
