package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO : vérifier que les tests sont ok (ne comporte QUE des méthodes codegen, etc)
public class TestFloatLiteral {
    private final List<String> IMACodeGenInstExpectedFloat0 = new ArrayList<>();
    private final List<String> IMACodeGenInstExpectedFloat = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedFloat0 = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedFloat = new ArrayList<>();

    private DecacCompiler compiler;

    @Before
    public void setup() {
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
        assertThat(float1.getValue(), equalTo(0.0f));
        assertThat(float2.getValue(), equalTo(5.2f));
    }

    @Test
    public void testVerifyExpr() {
        compiler = new DecacCompiler(null, null);

        FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
        FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque

        // Le type renvoyé est bien FLOAT
        assertThat(compiler.environmentType.FLOAT, equalTo(float1.verifyExpr(compiler, null, null)));
        assertThat(compiler.environmentType.FLOAT, equalTo(float2.verifyExpr(compiler, null, null)));

        // Le type de l'expression est bien FLOAT
        assertThat(compiler.environmentType.FLOAT, equalTo(float1.getType()));
        assertThat(compiler.environmentType.FLOAT, equalTo(float2.getType()));
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
        assertThat(float1.getValue(), equalTo(float1CodeGenPrint.getValue()));
        assertThat(float1.getType(), equalTo(float1CodeGenPrint.getType()));

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
        assertThat(float2.getValue(), equalTo(float2CodeGenPrint.getValue()));
        assertThat(float2.getType(), equalTo(float2CodeGenPrint.getType()));

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
        assertThat(float1.getValue(), equalTo(float1CodeGenPrint.getValue()));
        assertThat(float1.getType(), equalTo(float1CodeGenPrint.getType()));

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
        assertThat(float2.getValue(), equalTo(float2CodeGenPrint.getValue()));
        assertThat(float2.getType(), equalTo(float2CodeGenPrint.getType()));

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpectedFloat));
    }
}
