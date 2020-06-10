package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Minus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestMinusAdvanced {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;
    private DecacCompiler compiler;

    @Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError {
        Minus minus = new Minus(intexpr1, intexpr2);
        // check the result
        assertTrue(minus.getLeftOperand().verifyExpr(compiler, null, null).isInt());
        assertTrue(minus.getRightOperand().verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Minus minus = new Minus(intexpr1, floatexpr1);
        // check the result
        //assertTrue(t.getLeftOperand().verifyExpr(compiler, null, null).isInt());
        //assertTrue(t.getRightOperand().verifyExpr(compiler, null, null).isFloat());
        assertTrue(minus.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertTrue(minus.getLeftOperand() instanceof ConvFloat);
        assertFalse(minus.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Minus minus = new Minus(floatexpr1, intexpr1);
        // check the result
        //assertTrue(t.getLeftOperand().verifyExpr(compiler, null, null).isFloat());
        //assertTrue(t.getRightOperand().verifyExpr(compiler, null, null).isInt());
        assertTrue(minus.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertTrue(minus.getRightOperand() instanceof ConvFloat);
        assertFalse(minus.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }
}
