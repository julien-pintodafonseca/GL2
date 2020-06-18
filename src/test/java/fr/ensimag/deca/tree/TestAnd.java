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
import java.util.Collections;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestAnd {
    private final Label anyLabel = new Label("my_basic_label");
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);

    @Test
    public void testCodeGenCMPWithReverse() throws DecacFatalError {
        // true && true
        List<String> expectedTrue1 = Collections.singletonList("");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolTrueExpr2, true, expectedTrue1);

        // true && false
        List<String> expectedTrue2 = Collections.singletonList("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolFalseExpr2, true, expectedTrue2);

        // false && true
        List<String> expectedTrue3 = Collections.singletonList("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolTrueExpr2, true, expectedTrue3);

        // false && false
        List<String> expectedTrue4 = new ArrayList<>();
        expectedTrue4.add("BRA my_basic_label");
        expectedTrue4.add("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolFalseExpr2, true, expectedTrue4);
    }

    @Test
    public void testCodeGenCMPWithoutReverse() throws DecacFatalError {
        // true && true
        List<String> expectedFalse1 = new ArrayList<>();
        expectedFalse1.add("BRA or0");
        expectedFalse1.add("BRA or_end0");
        expectedFalse1.add("or0:");
        expectedFalse1.add("BRA my_basic_label");
        expectedFalse1.add("or_end0:");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolTrueExpr2, false, expectedFalse1);

        // true && false
        List<String> expectedFalse2 = new ArrayList<>();
        expectedFalse2.add("BRA or0");
        expectedFalse2.add("BRA or_end0");
        expectedFalse2.add("or0:");
        expectedFalse2.add("or_end0:");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolFalseExpr2, false, expectedFalse2);

        // false && true
        List<String> expectedFalse3 = new ArrayList<>();
        expectedFalse3.add("BRA or_end0");
        expectedFalse3.add("or0:");
        expectedFalse3.add("BRA my_basic_label");
        expectedFalse3.add("or_end0:");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolTrueExpr2, false, expectedFalse3);

        // false && false
        List<String> expectedFalse4 = new ArrayList<>();
        expectedFalse4.add("BRA or_end0");
        expectedFalse4.add("or0:");
        expectedFalse4.add("or_end0:");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolFalseExpr2, false, expectedFalse4);
    }

    private void codeGenCMPWithSpecificParams(BooleanLiteral boolExpr1, BooleanLiteral boolExpr2,
                                              Boolean reverse, List<String> expected) throws DecacFatalError {
        // check codeGenCMP
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setLabelManager();

        And and = new And(boolExpr1, boolExpr2);
        and.codeGenCMP(myCompiler, anyLabel, reverse);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(expected));
    }
}
