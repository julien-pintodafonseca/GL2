package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.Label;
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

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO : vérifier que les tests sont ok (ne comporte QUE des méthodes codegen, etc)
public class TestBooleanLiteral {
    private final List<String> IMACodeGenInstExpectedBooleanTrue = new ArrayList<>();
    private final List<String> IMACodeGenInstExpectedBooleanFalse = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedBooleanTrueTrue = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedBooleanFalseFalse = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedBooleanTrueFalse = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedBooleanFalseTrue = new ArrayList<>();

    @Mock private Label lb;

    private DecacCompiler compilerBoolTrue;
    private DecacCompiler compilerBoolFalse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IMACodeGenInstExpectedBooleanTrue.add("LOAD #1, R1");
        IMACodeGenInstExpectedBooleanFalse.add("LOAD #0, R1");
        IMACodeGenPrintExpectedBooleanTrueTrue.add("");
        IMACodeGenPrintExpectedBooleanFalseFalse.add("");
        IMACodeGenPrintExpectedBooleanTrueFalse.add("BRA lb");
        IMACodeGenPrintExpectedBooleanFalseTrue.add("BRA lb");
    }

    @Test
    public void testGetValue() {
        BooleanLiteral boolTrue = new BooleanLiteral(true); // Cas d'une valeur True
        BooleanLiteral boolFalse = new BooleanLiteral(false); // Cas d'une valeur False

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertEquals(false, boolFalse.getValue());
        assertEquals(true, boolTrue.getValue());
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        compilerBoolTrue = new DecacCompiler(null, null);
        compilerBoolFalse = new DecacCompiler(null, null);

        BooleanLiteral boolTrue = new BooleanLiteral(true); // Cas d'une valeur True
        BooleanLiteral boolFalse = new BooleanLiteral(false); // Cas d'une valeur False

        // Le type renvoyé est bien BOOLEAN
        assertEquals(compilerBoolTrue.environmentType.BOOLEAN, boolTrue.verifyExpr(compilerBoolTrue, null, null));
        assertEquals(compilerBoolFalse.environmentType.BOOLEAN, boolFalse.verifyExpr(compilerBoolFalse, null, null));

        // Le type de l'expression est bien BOOLEAN
        assertEquals(compilerBoolTrue.environmentType.BOOLEAN, boolTrue.getType());
        assertEquals(compilerBoolFalse.environmentType.BOOLEAN, boolFalse.getType());
    }

    @Test
    public void testCodeGenCMP() throws DecacFatalError, ContextualError {
        compilerBoolTrue = new DecacCompiler(null, null);
        compilerBoolFalse = new DecacCompiler(null, null);

        // Cas d'une valeur True
        BooleanLiteral boolTrue = new BooleanLiteral(true);
        boolTrue.verifyExpr(compilerBoolTrue, null, null);
        BooleanLiteral boolTrueCodeGenCMP = new BooleanLiteral(true);
        boolTrueCodeGenCMP.verifyExpr(compilerBoolTrue, null, null);

        // Cas d'une valeur False
        BooleanLiteral int2 = new BooleanLiteral(false);
        int2.verifyExpr(compilerBoolFalse, null, null);
        BooleanLiteral boolFalseCodeGenCMP = new BooleanLiteral(false);
        boolFalseCodeGenCMP.verifyExpr(compilerBoolFalse, null, null);

        // Pas de modification des attributs lors de la génération de code
        boolTrueCodeGenCMP.codeGenCMP(compilerBoolTrue, lb, true);
        assertEquals(boolTrue.getValue(), boolTrueCodeGenCMP.getValue());
        assertEquals(boolTrue.getType(), boolTrueCodeGenCMP.getType());

        boolFalseCodeGenCMP.codeGenCMP(compilerBoolFalse, lb, false);
        assertEquals(int2.getValue(), boolFalseCodeGenCMP.getValue());
        assertEquals(int2.getType(), boolFalseCodeGenCMP.getType());

        String resultTrue = compilerBoolTrue.displayIMAProgram();
        assertThat(normalizeDisplay(resultTrue), is(IMACodeGenPrintExpectedBooleanTrueTrue));

        String resultFalse = compilerBoolFalse.displayIMAProgram();
        assertThat(normalizeDisplay(resultFalse), is(IMACodeGenPrintExpectedBooleanFalseFalse));

        boolTrueCodeGenCMP.codeGenCMP(compilerBoolTrue, lb, false);
        assertEquals(boolTrue.getValue(), boolTrueCodeGenCMP.getValue());
        assertEquals(boolTrue.getType(), boolTrueCodeGenCMP.getType());

        boolFalseCodeGenCMP.codeGenCMP(compilerBoolFalse, lb, true);
        assertEquals(int2.getValue(), boolFalseCodeGenCMP.getValue());
        assertEquals(int2.getType(), boolFalseCodeGenCMP.getType());

        resultTrue = compilerBoolTrue.displayIMAProgram();
        assertThat(normalizeDisplay(resultTrue), is(IMACodeGenPrintExpectedBooleanTrueFalse));

        resultFalse = compilerBoolFalse.displayIMAProgram();
        assertThat(normalizeDisplay(resultFalse), is(IMACodeGenPrintExpectedBooleanFalseTrue));
    }

    @Test
    public void testCodeGenInst() throws ContextualError {
        compilerBoolTrue = new DecacCompiler(null, null);
        compilerBoolFalse = new DecacCompiler(null, null);

        // Cas d'une valeur True
        BooleanLiteral boolTrue = new BooleanLiteral(true);
        boolTrue.verifyExpr(compilerBoolTrue, null, null);
        BooleanLiteral boolTrueCodeGenCMP = new BooleanLiteral(true);
        boolTrueCodeGenCMP.verifyExpr(compilerBoolTrue, null, null);

        // Cas d'une valeur False
        BooleanLiteral boolFalse = new BooleanLiteral(false);
        boolFalse.verifyExpr(compilerBoolFalse, null, null);
        BooleanLiteral boolFalseCodeGenCMP = new BooleanLiteral(false);
        boolFalseCodeGenCMP.verifyExpr(compilerBoolFalse, null, null);

        // Pas de modification des attributs lors de la génération de code
        boolTrueCodeGenCMP.codeGenInst(compilerBoolTrue, Register.R1);
        assertEquals(boolTrue.getValue(), boolTrueCodeGenCMP.getValue());
        assertEquals(boolTrue.getType(), boolTrueCodeGenCMP.getType());

        boolFalseCodeGenCMP.codeGenInst(compilerBoolFalse, Register.R1);
        assertEquals(boolFalse.getValue(), boolFalseCodeGenCMP.getValue());
        assertEquals(boolFalse.getType(), boolFalseCodeGenCMP.getType());

        String resultTrue = compilerBoolTrue.displayIMAProgram();
        assertThat(normalizeDisplay(resultTrue), is(IMACodeGenInstExpectedBooleanTrue));

        String resultFalse = compilerBoolFalse.displayIMAProgram();
        assertThat(normalizeDisplay(resultFalse), is(IMACodeGenInstExpectedBooleanFalse));
    }
}
