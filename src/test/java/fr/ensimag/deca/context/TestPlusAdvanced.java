package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.*;
import fr.ensimag.deca.utils.Utils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the Plus node using mockito, using @Mock and @Before annotations.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestPlusAdvanced {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;

    private GPRegister reg1;
    private GPRegister reg2;
    private DecacCompiler compiler;
    
    @Before
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        reg1 = Register.R0;
        reg2 = Register.R1;
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
        Plus plus = new Plus(intexpr1, intexpr2);
        // check the result
        assertTrue(plus.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Plus plus = new Plus(intexpr1, floatexpr1);
        // check the result
        assertTrue(plus.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertTrue(plus.getLeftOperand() instanceof ConvFloat);
        assertFalse(plus.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Plus plus = new Plus(floatexpr1, intexpr1);
        // check the result
        assertTrue(plus.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        assertTrue(plus.getRightOperand() instanceof ConvFloat);
        assertFalse(plus.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError {
        // on test l'ajout de l'instruction ADD
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setErrorLabelManager();

        Plus plus = new Plus(intexpr1, intexpr2);
        plus.codeGenInstArith(myCompiler, reg1, reg2);

        List<String> expected = Collections.singletonList("ADD R0, R1");
        String result = myCompiler.displayIMAProgram();

        assertThat(Utils.normalizeDisplay(result).size(), is(expected.size()));
        assertTrue(Utils.normalizeDisplay(result).containsAll(expected));

        // ------------------------------------------------------------

        // on test la boucle genCodeError
        List<Plus> plusList = new ArrayList<>();
        plusList.add(new Plus(intexpr1, floatexpr2));
        plusList.add(new Plus(floatexpr1, intexpr2));
        plusList.add(new Plus(floatexpr1, floatexpr2));

        List<String> expectedFor = new ArrayList<>();
        expectedFor.add("ADD R0, R1");
        expectedFor.add("BOV arithmetic_overflow; Overflow : check for previous operation");

        for (Plus p : plusList) {
            // on reset le compiler
            myCompiler = new DecacCompiler(null, null);
            myCompiler.setErrorLabelManager();

            // on test l'ajout de l'instruction SUB et BOV avec Overflow
            p.codeGenInstArith(myCompiler, reg1, reg2);
            String resultFor = myCompiler.displayIMAProgram();
            assertThat(Utils.normalizeDisplay(resultFor).size(), is(expectedFor.size()));
            assertTrue(Utils.normalizeDisplay(resultFor).containsAll(expectedFor));
        }
    }

    @Test
    public void testDecompile() {
        Plus plus1 = new Plus(new IntLiteral(42), new IntLiteral(-6));
        String result1 = plus1.decompile();
        String expected1 = "(42 + -6)";
        assertThat(result1, is(expected1));

        Plus plus2 = new Plus(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = plus2.decompile();
        String expected2 = "(0 + 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        Plus plus3 = new Plus(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result3 = plus3.decompile();
        String expected3 = "(-0x1.f61582p8 + 0x1.46e978p1)";
        assertThat(result3, is(expected3));
    }
}
