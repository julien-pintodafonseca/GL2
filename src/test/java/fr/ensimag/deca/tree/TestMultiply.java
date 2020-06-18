package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
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

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestMultiply {
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedtrue = new ArrayList<>();
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedfalse = new ArrayList<>();
    private final List<String> IMACodeGenInitializationAbstractExpr = new ArrayList<>();
    
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    private GPRegister reg1;
    private GPRegister reg2;
    private DecacCompiler compiler;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
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
        when(sonL.getType()).thenReturn(INT);
        when(sonR.getType()).thenReturn(INT);

        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("PUSH R3");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("MUL R3, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("POP R3");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("LOAD R4, R1");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("WINT");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("POP R4");

        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("PUSH R3");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("MUL R3, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("POP R3");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("LOAD R4, R1");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("WINT");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("POP R4");

        IMACodeGenInitializationAbstractExpr.add("MUL R3, R2");
        IMACodeGenInitializationAbstractExpr.add("LOAD R2, R1");
        IMACodeGenInitializationAbstractExpr.add("WINT");
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError {
        // on test l'ajout de l'instruction MUL
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setErrorLabelManager();

        Multiply multiply = new Multiply(intexpr1, intexpr2);
        multiply.codeGenInstArith(myCompiler, reg1, reg2);

        List<String> expected = Collections.singletonList("MUL R0, R1");
        String result = myCompiler.displayIMAProgram();

        assertThat(Utils.normalizeDisplay(result).size(), is(expected.size()));
        assertTrue(Utils.normalizeDisplay(result).containsAll(expected));

        // ------------------------------------------------------------

        // on test la boucle genCodeError
        List<Multiply> multiplyList = new ArrayList<>();
        multiplyList.add(new Multiply(intexpr1, floatexpr2));
        multiplyList.add(new Multiply(floatexpr1, intexpr2));
        multiplyList.add(new Multiply(floatexpr1, floatexpr2));

        List<String> expectedFor = new ArrayList<>();
        expectedFor.add("MUL R0, R1");
        expectedFor.add("BOV arithmetic_overflow; Overflow : check for previous operation");

        for (Multiply m : multiplyList) {
            // on reset le compiler
            myCompiler = new DecacCompiler(null, null);
            myCompiler.setErrorLabelManager();

            // on test l'ajout de l'instruction MUL et BOV avec Overflow
            m.codeGenInstArith(myCompiler, reg1, reg2);
            String resultFor = myCompiler.displayIMAProgram();
            assertThat(Utils.normalizeDisplay(resultFor).size(), is(expectedFor.size()));
            assertTrue(Utils.normalizeDisplay(resultFor).containsAll(expectedFor));
        }
    }

    @Test
    public void testCodeGenPrintAbstractExprReverseFalse() throws DecacFatalError {
        // check codeGenPrint
        // with AbstractExpr operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();
        myCompiler.setErrorLabelManager();

        Multiply node = new Multiply(sonL, sonR);
        node.setType(compiler.environmentType.INT);
        node.codeGenPrint(myCompiler, false);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationAbstractExpr));
    }

    @Test
    public void testCodeGenPrintAbstractExprReverseTrue() throws DecacFatalError {
        // check codeGenPrint
        // with AbstractExpr operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();
        myCompiler.setErrorLabelManager();

        Multiply node = new Multiply(sonL, sonR);
        node.setType(compiler.environmentType.INT);
        node.codeGenPrint(myCompiler, true);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationAbstractExpr));
    }

    @Test
    public void testCodeGenPrintNoMoreRegistersAvailableReverseFalse() throws DecacFatalError {
        // check that codeGenPrint with no registers available throws UnsupportedOperationException
        // with boolean operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();
        myCompiler.setErrorLabelManager();
        myCompiler.setTSTOManager();
        myCompiler.getRegisterManager().take(2);
        myCompiler.getRegisterManager().take(3);
        myCompiler.getRegisterManager().take(4);

        Multiply node = new Multiply(sonL, sonR);
        node.setType(compiler.environmentType.INT);

        // Pas de modification des attributs lors de la génération de code
        node.codeGenPrint(myCompiler, false);
        assertEquals(sonL.getType(), node.getLeftOperand().getType());
        assertEquals(sonR.getType(), node.getRightOperand().getType());
        assertThat(node.getLeftOperand(), is(sonL));
        assertThat(node.getRightOperand(), is(sonR));

        String result2 = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationNoMoreRegistersExpectedfalse));
    }

    @Test
    public void testCodeGenPrintNoMoreRegistersAvailableReverseTrue() throws DecacFatalError {
        // check that codeGenPrint with no registers available throws UnsupportedOperationException
        // with boolean operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();
        myCompiler.setErrorLabelManager();
        myCompiler.setTSTOManager();
        myCompiler.getRegisterManager().take(2);
        myCompiler.getRegisterManager().take(3);
        myCompiler.getRegisterManager().take(4);

        Multiply node = new Multiply(sonL, sonR);
        node.setType(compiler.environmentType.INT);

        // Pas de modification des attributs lors de la génération de code
        node.codeGenPrint(myCompiler,true);
        assertEquals(sonL.getType(), node.getLeftOperand().getType());
        assertEquals(sonR.getType(), node.getRightOperand().getType());
        assertThat(node.getLeftOperand(), is(sonL));
        assertThat(node.getRightOperand(), is(sonR));

        String result2 = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationNoMoreRegistersExpectedtrue));
    }
}
