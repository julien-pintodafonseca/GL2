package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.ima.pseudocode.Register;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestFloatLiteral extends TestCase {
    private DecacCompiler compiler = new DecacCompiler(null, null);

    final private List<String> IMACodeGenInstExpectedFloat0 = new ArrayList<>();
    final private List<String> IMACodeGenInstExpectedFloat = new ArrayList<>();
    final private List<String> IMACodeGenPrintExpectedFloat0 = new ArrayList<>();
    final private List<String> IMACodeGenPrintExpectedFloat = new ArrayList<>();

    @Before
    public void setUp() {
        IMACodeGenInstExpectedFloat0.add("LOAD #0x0.0p0, R1");
        IMACodeGenInstExpectedFloat.add("LOAD #0x1.4cccccp2, R1");
        IMACodeGenPrintExpectedFloat0.add("LOAD #0x0.0p0, R1");
        IMACodeGenPrintExpectedFloat0.add("WFLOAT");
        IMACodeGenPrintExpectedFloat.add("LOAD #0x1.4cccccp2, R1");
        IMACodeGenPrintExpectedFloat.add("WFLOAT");
    }

    @Test
    public void testGetValue() {
        FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
        FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertEquals(new Float(0.0), float1.getValue());
        assertEquals(new Float(5.2), float2.getValue());
    }

    @Test
    public void testVerifyExpr() {
        FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
        FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque

        // Le type renvoyé est bien FLOAT
        assertEquals(compiler.environmentType.FLOAT, float1.verifyExpr(compiler, null, null));
        assertEquals(compiler.environmentType.FLOAT, float2.verifyExpr(compiler, null, null));

        // Le type de l'expression est bien FLOAT
        assertEquals(compiler.environmentType.FLOAT, float1.getType());
        assertEquals(compiler.environmentType.FLOAT, float2.getType());
    }

    @Test
    public void testCodeGenPrint() throws DecacFatalError {
        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur nulle
        FloatLiteral float1 = new FloatLiteral(0.0f);
        float1.verifyExpr(compiler, null, null);
        FloatLiteral float1CodeGenPrint = new FloatLiteral(0.0f);
        float1CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float1CodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(float1.getValue(), float1CodeGenPrint.getValue());
        assertEquals(float1.getType(), float1CodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenPrintExpectedFloat0));

        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur quelconque
        FloatLiteral float2 = new FloatLiteral(5.2f);
        float2.verifyExpr(compiler, null, null);
        FloatLiteral float2CodeGenPrint = new FloatLiteral(5.2f);
        float2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float2CodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(float2.getValue(), float2CodeGenPrint.getValue());
        assertEquals(float2.getType(), float2CodeGenPrint.getType());

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenPrintExpectedFloat));
    }

    @Test
    public void testCodeGenInst() {
        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur nulle
        FloatLiteral float1 = new FloatLiteral(0.0f);
        float1.verifyExpr(compiler, null, null);
        FloatLiteral float1CodeGenPrint = new FloatLiteral(0.0f);
        float1CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float1CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(float1.getValue(), float1CodeGenPrint.getValue());
        assertEquals(float1.getType(), float1CodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpectedFloat0));

        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur quelconque
        FloatLiteral float2 = new FloatLiteral(5.2f);
        float2.verifyExpr(compiler, null, null);
        FloatLiteral float2CodeGenPrint = new FloatLiteral(5.2f);
        float2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float2CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(float2.getValue(), float2CodeGenPrint.getValue());
        assertEquals(float2.getType(), float2CodeGenPrint.getType());

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpectedFloat));
    }
}
