package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestNot {
    private final Label anyLabel = new Label("my_basic_label");
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);

    @Test
    public void testCodeGenCMPWithReverse() throws DecacFatalError {
        // not true
        //List<String> expectedTrue1 = Collections.singletonList("");
        List<String> expectedTrue1 = new ArrayList<>();
        expectedTrue1.add("LOAD #1, R2");
        expectedTrue1.add("CMP #1, R2");
        expectedTrue1.add("BEQ my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr1, true, expectedTrue1);

        // not false
        //List<String> expectedTrue2 = Collections.singletonList("BRA my_basic_label");
        List<String> expectedTrue2 = new ArrayList<>();
        expectedTrue2.add("LOAD #0, R2");
        expectedTrue2.add("CMP #1, R2");
        expectedTrue2.add("BEQ my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr1, true, expectedTrue2);

    }

    @Test
    public void testCodeGenCMPWithoutReverse() throws DecacFatalError {
        // not true
        List<String> expectedFalse1 = new ArrayList<>();;
        expectedFalse1.add("LOAD #1, R2");
        expectedFalse1.add("CMP #1, R2");
        expectedFalse1.add("BNE my_basic_label");
        codeGenCMPWithSpecificParams(boolTrueExpr1, false, expectedFalse1);

        // not false
        List<String> expectedFalse2 = new ArrayList<>();
        expectedFalse2.add("LOAD #0, R2");
        expectedFalse2.add("CMP #1, R2");
        expectedFalse2.add("BNE my_basic_label");
        codeGenCMPWithSpecificParams(boolFalseExpr1, false, expectedFalse2);

    }

    private void codeGenCMPWithSpecificParams(BooleanLiteral boolExpr1,
                                              Boolean reverse, List<String> expected) throws DecacFatalError {
        // check codeGenCMP
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setRegisterManager(5);
        myCompiler.setLabelManager();

        Not not = new Not(boolExpr1);
        not.codeGenCMP(myCompiler, anyLabel, reverse);

        String result = myCompiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(expected));
    }
}


