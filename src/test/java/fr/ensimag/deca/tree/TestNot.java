package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
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
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestNot {
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedtrue = new ArrayList<>();
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpectedfalse = new ArrayList<>();
    private final List<String> IMACodeGenInitializationAbstractExpr = new ArrayList<>();
    private final Label anyLabel = new Label("my_basic_label");
    private final BooleanLiteral boolTrueExpr = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr = new BooleanLiteral(false);

    @Mock private BooleanLiteral booleanExpr;
    @Mock private AbstractExpr expr;
    @Mock private Label lb;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);

        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("CMP #1, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("POP R4");
        IMACodeGenInitializationNoMoreRegistersExpectedtrue.add("BEQ lb");

        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("PUSH R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("CMP #1, R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("POP R4");
        IMACodeGenInitializationNoMoreRegistersExpectedfalse.add("BNE lb");

        IMACodeGenInitializationAbstractExpr.add("");
    }

    @Test
    public void testCodeGenCMPWithBooleanOperandAndReverse() throws DecacFatalError {
        // !true
        List<String> expectedTrue1 = new ArrayList<>();
        expectedTrue1.add("LOAD #1, R2");
        expectedTrue1.add("CMP #1, R2");
        expectedTrue1.add("BEQ my_basic_label");
        codeGenCMPWithBooleanOperandAndSpecificParams(boolTrueExpr, true, expectedTrue1);

        // !false
        List<String> expectedTrue2 = new ArrayList<>();
        expectedTrue2.add("LOAD #0, R2");
        expectedTrue2.add("CMP #1, R2");
        expectedTrue2.add("BEQ my_basic_label");
        codeGenCMPWithBooleanOperandAndSpecificParams(boolFalseExpr, true, expectedTrue2);
    }

    @Test
    public void testCodeGenCMPWithoutBooleanOperandAndReverse() throws DecacFatalError {
        // !true
        List<String> expectedFalse1 = new ArrayList<>();;
        expectedFalse1.add("LOAD #1, R2");
        expectedFalse1.add("CMP #1, R2");
        expectedFalse1.add("BNE my_basic_label");
        codeGenCMPWithBooleanOperandAndSpecificParams(boolTrueExpr, false, expectedFalse1);

        // !false
        List<String> expectedFalse2 = new ArrayList<>();
        expectedFalse2.add("LOAD #0, R2");
        expectedFalse2.add("CMP #1, R2");
        expectedFalse2.add("BNE my_basic_label");
        codeGenCMPWithBooleanOperandAndSpecificParams(boolFalseExpr, false, expectedFalse2);
    }

    private void codeGenCMPWithBooleanOperandAndSpecificParams(BooleanLiteral boolExpr1,
                                                                Boolean reverse, List<String> expected) throws DecacFatalError {
        // check codeGenCMP
        // with boolean operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);

        Not not = new Not(boolExpr1);
        not.codeGenCMP(myCompiler, anyLabel, reverse);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(expected));
    }

    @Test
    public void testCodeGenCMPAbstractExprReverseFalse() throws DecacFatalError {
        // check codeGenCMP
        // with AbstractExpr operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);

        Not not = new Not(expr);
        not.codeGenCMP(myCompiler, anyLabel, false);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationAbstractExpr));
    }

    @Test
    public void testCodeGenCMPAbstractExprReverseTrue() throws DecacFatalError {
        // check codeGenCMP
        // with AbstractExpr operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);

        Not not = new Not(expr);
        not.codeGenCMP(myCompiler, anyLabel, true);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationAbstractExpr));
    }

    @Test
    public void testNoMoreRegistersAvailableReverseFalse() throws DecacFatalError {
        // check that codeGenCMP with no registers available throws UnsupportedOperationException
        // with boolean operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setTSTOManager();
        myCompiler.getRegisterManager().take(2);
        myCompiler.getRegisterManager().take(3);
        myCompiler.getRegisterManager().take(4);

        Not not = new Not(booleanExpr);

        // Pas de modification des attributs lors de la génération de code
        not.codeGenCMP(myCompiler, lb, false);
        assertEquals(booleanExpr.getType(), not.getOperand().getType());
        assertThat(not.getOperand(), is(booleanExpr));

        String result2 = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationNoMoreRegistersExpectedfalse));
    }

    @Test
    public void testNoMoreRegistersAvailableReverseTrue() throws DecacFatalError {
        // check that codeGenCMP with no registers available throws UnsupportedOperationException
        // with boolean operand
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setTSTOManager();
        myCompiler.getRegisterManager().take(2);
        myCompiler.getRegisterManager().take(3);
        myCompiler.getRegisterManager().take(4);

        Not not = new Not(booleanExpr);

        // Pas de modification des attributs lors de la génération de code
        not.codeGenCMP(myCompiler, lb, true);
        assertEquals(booleanExpr.getType(), not.getOperand().getType());
        assertThat(not.getOperand(), is(booleanExpr));

        String result2 = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationNoMoreRegistersExpectedtrue));
    }
}
