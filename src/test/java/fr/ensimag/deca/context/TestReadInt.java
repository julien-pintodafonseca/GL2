package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.ReadInt;
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
public class TestReadInt extends TestCase {
    final private List<String> IMACodeGenPrintExpected = new ArrayList<>();
    final private List<String> IMACodeGenInstExpected = new ArrayList<>();
    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setUp() {
        IMACodeGenPrintExpected.add("RINT");
        IMACodeGenPrintExpected.add("BOV read_error; Overflow check for previous operation");
        IMACodeGenPrintExpected.add("WINT");
        IMACodeGenInstExpected.add("RINT");
        IMACodeGenInstExpected.add("BOV read_error; Overflow check for previous operation");
        IMACodeGenInstExpected.add("LOAD R1, R0");
    }

    @Test
    public void testVerifyExpr() {
        ReadInt read = new ReadInt();

        // Le type renvoyé est bien INT
        assertEquals(read.verifyExpr(compiler, null, null), compiler.environmentType.INT);

        // Le type de l'expression est bien INT
        assertEquals(read.getType(), compiler.environmentType.INT);
    }

    @Test
    public void testCodeGenPrint() throws DecacFatalError {
        compiler = new DecacCompiler(null, null);
        ReadInt read = new ReadInt();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadInt readCodeGenPrint = new ReadInt();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        readCodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(read.getType(), readCodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenPrintExpected));
    }

    @Test
    public void testCodeGenInst() throws DecacFatalError {
        compiler = new DecacCompiler(null, null);
        ReadInt read = new ReadInt();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadInt readCodeGenPrint = new ReadInt();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        readCodeGenPrint.codeGenInst(compiler, Register.R0);
        assertEquals(read.getType(), readCodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpected));
    }
}
