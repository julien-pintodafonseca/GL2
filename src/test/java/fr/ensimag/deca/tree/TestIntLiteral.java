package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestIntLiteral {
    private final List<String> IMACodeGenInstExpectedInt0 = new ArrayList<>();
    private final List<String> IMACodeGenInstExpectedInt = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedInt0 = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedInt = new ArrayList<>();

    private DecacCompiler compiler;

    @Before
    public void setup() {
        IMACodeGenInstExpectedInt0.add("LOAD #0, R1");
        IMACodeGenInstExpectedInt.add("LOAD #5, R1");
        IMACodeGenPrintExpectedInt0.add("LOAD #0, R1");
        IMACodeGenPrintExpectedInt0.add("WINT");
        IMACodeGenPrintExpectedInt.add("LOAD #5, R1");
        IMACodeGenPrintExpectedInt.add("WINT");
    }

    @Test
    public void testGetValue() {
        IntLiteral int1 = new IntLiteral(0); // Cas d'une valeur nulle
        IntLiteral int2 = new IntLiteral(5); // Cas d'une valeur quelconque

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertEquals(0, int1.getValue());
        assertEquals(5, int2.getValue());
    }

    @Test
    public void testVerifyExpr() {
        compiler = new DecacCompiler(null, null);

        IntLiteral int1 = new IntLiteral(0); // Cas d'une valeur nulle
        IntLiteral int2 = new IntLiteral(5); // Cas d'une valeur quelconque

        // Le type renvoyé est bien INT
        assertEquals(compiler.environmentType.INT, int1.verifyExpr(compiler, null, null));
        assertEquals(compiler.environmentType.INT, int2.verifyExpr(compiler, null, null));

        // Le type de l'expression est bien INT
        assertEquals(compiler.environmentType.INT, int1.getType());
        assertEquals(compiler.environmentType.INT, int2.getType());
    }

    @Test
    public void testCodeGenPrint() throws DecacFatalError {
        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur nulle
        IntLiteral int1 = new IntLiteral(0);
        int1.verifyExpr(compiler, null, null);
        IntLiteral int1CodeGenPrint = new IntLiteral(0);
        int1CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int1CodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(int1.getValue(), int1CodeGenPrint.getValue());
        assertEquals(int1.getType(), int1CodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenPrintExpectedInt0));

        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur quelconque
        IntLiteral int2 = new IntLiteral(5);
        int2.verifyExpr(compiler, null, null);
        IntLiteral int2CodeGenPrint = new IntLiteral(5);
        int2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int2CodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(int2.getValue(), int2CodeGenPrint.getValue());
        assertEquals(int2.getType(), int2CodeGenPrint.getType());

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenPrintExpectedInt));
    }

    @Test
    public void testCodeGenInst() {
        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur nulle
        IntLiteral int1 = new IntLiteral(0);
        int1.verifyExpr(compiler, null, null);
        IntLiteral int1CodeGenPrint = new IntLiteral(0);
        int1CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int1CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(int1.getValue(), int1CodeGenPrint.getValue());
        assertEquals(int1.getType(), int1CodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpectedInt0));

        compiler = new DecacCompiler(null, null);

        // Cas d'une valeur quelconque
        IntLiteral int2 = new IntLiteral(5);
        int2.verifyExpr(compiler, null, null);
        IntLiteral int2CodeGenPrint = new IntLiteral(5);
        int2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int2CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(int2.getValue(), int2CodeGenPrint.getValue());
        assertEquals(int2.getType(), int2CodeGenPrint.getType());

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpectedInt));
    }
}
