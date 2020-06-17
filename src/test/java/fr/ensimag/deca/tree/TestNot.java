package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestNot {
    private final Label anyLabel = new Label("my_basic_label");
    private final BooleanLiteral boolTrueExpr = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr = new BooleanLiteral(false);

    @Test
    public void testCodeGenCMPWithReverse() throws DecacFatalError {
        // !true
        List<String> expectedTrue1 = new ArrayList<>();
        expectedTrue1.add("LOAD #1, R2");
        expectedTrue1.add("CMP #1, R2");
        expectedTrue1.add("BEQ my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr, true, expectedTrue1);

        // !false
        List<String> expectedTrue2 = new ArrayList<>();
        expectedTrue2.add("LOAD #0, R2");
        expectedTrue2.add("CMP #1, R2");
        expectedTrue2.add("BEQ my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr, true, expectedTrue2);
    }

    @Test
    public void testCodeGenCMPWithoutReverse() throws DecacFatalError {
        // !true
        List<String> expectedFalse1 = new ArrayList<>();;
        expectedFalse1.add("LOAD #1, R2");
        expectedFalse1.add("CMP #1, R2");
        expectedFalse1.add("BNE my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr, false, expectedFalse1);

        // !false
        List<String> expectedFalse2 = new ArrayList<>();
        expectedFalse2.add("LOAD #0, R2");
        expectedFalse2.add("CMP #1, R2");
        expectedFalse2.add("BNE my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr, false, expectedFalse2);
    }

    private void codeGenCMPWithSpecificParams(BooleanLiteral boolExpr1,
                                              Boolean reverse, List<String> expected) throws DecacFatalError {
        // check codeGenCMP
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);

        Not not = new Not(boolExpr1);
        not.codeGenCMP(myCompiler, anyLabel, reverse);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(expected));
    }

    @Test
    public void testNoMoreRegistersAvailable() throws DecacFatalError {
        // check that codeGenCMP with no registers available throws UnsupportedOperationException
        // if operand is boolean
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.getRegisterManager().take(2);
        myCompiler.getRegisterManager().take(3);
        myCompiler.getRegisterManager().take(4);

        Not not = new Not(boolTrueExpr);

        // TODO : inscrire ce message d'erreur dans la classe ErrorMessages !
        UnsupportedOperationException expected =
                new UnsupportedOperationException("no more available registers : policy not yet implemented");
        UnsupportedOperationException resultWithReverse = assertThrows(UnsupportedOperationException.class, () -> {
            not.codeGenCMP(myCompiler, anyLabel, true);
        });
        UnsupportedOperationException resultWithoutReverse = assertThrows(UnsupportedOperationException.class, () -> {
            not.codeGenCMP(myCompiler, anyLabel, false);
        });

        assertThat(resultWithReverse.getMessage(), is(expected.getMessage()));
        assertThat(resultWithoutReverse.getMessage(), is(expected.getMessage()));
    }
}
