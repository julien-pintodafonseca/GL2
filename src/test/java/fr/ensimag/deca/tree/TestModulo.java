package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.utils.Utils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
public class TestModulo {
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedtrue = new ArrayList<>();
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedfalse = new ArrayList<>();
    private final List<String> IMACodeGenInitializationAbstractExpr = new ArrayList<>();
    
    private final DecacCompiler compiler = new DecacCompiler(null, null);
    private final Type INT = compiler.environmentType.INT;

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;
    
    private GPRegister reg1;
    private GPRegister reg2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);

        reg1 = Register.R0;
        reg2 = Register.R1;
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr1.getType()).thenReturn(INT);
        when(intexpr2.getType()).thenReturn(INT);
        when(sonL.getType()).thenReturn(INT);
        when(sonR.getType()).thenReturn(INT);

        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("PUSH R3");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("REM R3, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("BOV arithmetic_overflow; Overflow : check for previous operation");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("POP R3");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("LOAD R4, R1");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("WINT");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("POP R4");

        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("PUSH R3");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("REM R3, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("BOV arithmetic_overflow; Overflow : check for previous operation");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("POP R3");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("LOAD R4, R1");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("WINT");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("POP R4");

        IMACodeGenInitializationAbstractExpr.add("REM R3, R2");
        IMACodeGenInitializationAbstractExpr.add("BOV arithmetic_overflow; Overflow : check for previous operation");
        IMACodeGenInitializationAbstractExpr.add("LOAD R2, R1");
        IMACodeGenInitializationAbstractExpr.add("WINT");
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError, ContextualError {
        // on test l'ajout de l'instruction REM
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setErrorLabelManager();

        Modulo modulo = new Modulo(intexpr1, intexpr2);
        modulo.setType(INT);
        modulo.codeGenInstArith(myCompiler, reg1, reg2);

        List<String> expected = new ArrayList<>();
        expected.add("REM R0, R1");
        expected.add("BOV arithmetic_overflow; Overflow : check for previous operation");
        String result = myCompiler.displayIMAProgram();

        assertThat(Utils.normalizeDisplay(result).size(), is(expected.size()));
        assertTrue(Utils.normalizeDisplay(result).containsAll(expected));

    }

    @Test
    public void testCodeGenPrintAbstractExprReverseFalse() throws DecacFatalError {
        // check codeGenPrint
        // with AbstractExpr operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();
        myCompiler.setErrorLabelManager();

        Modulo node = new Modulo(sonL, sonR);
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

        Modulo node = new Modulo(sonL, sonR);
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

        Modulo node = new Modulo(sonL, sonR);
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

        Modulo node = new Modulo(sonL, sonR);
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
