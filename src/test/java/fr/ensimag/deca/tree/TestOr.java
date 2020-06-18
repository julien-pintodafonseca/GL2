package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestOr {
    private final Label anyLabel = new Label("my_basic_label");
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);

    @Test
    public void testCodeGenCMPWithReverse() throws DecacFatalError {
        // true || true
        List<String> expectedTrue1 = new ArrayList<>();
        expectedTrue1.add("BRA or_end0");
        expectedTrue1.add("or0:");
        expectedTrue1.add("or_end0:");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolTrueExpr2, true, expectedTrue1);

        // true || false
        List<String> expectedTrue2 = new ArrayList<>();
        expectedTrue2.add("BRA or_end0");
        expectedTrue2.add("or0:");
        expectedTrue2.add("BRA my_basic_label");
        expectedTrue2.add("or_end0:");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolFalseExpr2, true, expectedTrue2);

        // false || true
        List<String> expectedTrue3 = new ArrayList<>();
        expectedTrue3.add("BRA or0");
        expectedTrue3.add("BRA or_end0");
        expectedTrue3.add("or0:");
        expectedTrue3.add("or_end0:");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolTrueExpr2, true, expectedTrue3);

        // false || false
        List<String> expectedTrue4 = new ArrayList<>();
        expectedTrue4.add("BRA or0");
        expectedTrue4.add("BRA or_end0");
        expectedTrue4.add("or0:");
        expectedTrue4.add("BRA my_basic_label");
        expectedTrue4.add("or_end0:");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolFalseExpr2, true, expectedTrue4);
    }

    @Test
    public void testCodeGenCMPWithoutReverse() throws DecacFatalError {
        // true || true
        List<String> expectedFalse1 = new ArrayList<>();;
        expectedFalse1.add("BRA my_basic_label");
        expectedFalse1.add("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolTrueExpr2, false, expectedFalse1);

        // true || false
        List<String> expectedFalse2 = new ArrayList<>();
        expectedFalse2.add("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr1, boolFalseExpr2, false, expectedFalse2);

        // false || true
        List<String> expectedFalse3 = new ArrayList<>();
        expectedFalse3.add("BRA my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolTrueExpr2, false, expectedFalse3);

        // false || false
        List<String> expectedFalse4 = Collections.singletonList("");
        codeGenCMPWithSpecificParams(boolFalseExpr1, boolFalseExpr2, false, expectedFalse4);
    }

    private void codeGenCMPWithSpecificParams(BooleanLiteral boolExpr1, BooleanLiteral boolExpr2,
                                              Boolean reverse, List<String> expected) throws DecacFatalError {
        // check codeGenCMP
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setLabelManager();

        Or or = new Or(boolExpr1, boolExpr2);
        or.codeGenCMP(myCompiler, anyLabel, reverse);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(expected));
    }
}
