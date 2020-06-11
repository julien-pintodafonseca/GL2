package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Minus;
import fr.ensimag.deca.utils.Utils;
import fr.ensimag.ima.pseudocode.GPRegister;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestMinus {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);
    private final String IMACodeGenInstArithExpected = "SUB reg1, reg2";

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;
    @Mock private GPRegister reg1;
    @Mock private GPRegister reg2;
    private DecacCompiler compiler;

    @Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
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
    public void testIntInt() throws ContextualError {
        Minus minus = new Minus(intexpr1, intexpr2);
        // check the result
        assertTrue(minus.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Minus minus = new Minus(intexpr1, floatexpr1);
        // check the result
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
        assertTrue(minus.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertTrue(minus.getRightOperand() instanceof ConvFloat);
        assertFalse(minus.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError {
        Minus minus = new Minus(intexpr1, intexpr2);
        minus.codeGenInstArith(compiler, reg1, reg2);
        String result = compiler.displayIMAProgram();
        assertThat(Utils.normalizeDisplay(result), is(IMACodeGenInstArithExpected));
    }
}
