package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.ReadFloat;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;

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
public class TestReadFloat {
    private final List<String> IMACodeGenPrintExpected = new ArrayList<>();
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        IMACodeGenPrintExpected.add("RFLOAT");
        IMACodeGenPrintExpected.add("BOV read_error; Overflow check for previous operation");
        IMACodeGenPrintExpected.add("WFLOAT");
        IMACodeGenInstExpected.add("RFLOAT");
        IMACodeGenInstExpected.add("BOV read_error; Overflow check for previous operation");
        IMACodeGenInstExpected.add("LOAD R1, R0");
    }

    @Test
    public void testVerifyExpr() {
        ReadFloat read = new ReadFloat();

        // Le type renvoyé est bien FLOAT
        assertEquals(read.verifyExpr(compiler, null, null), compiler.environmentType.FLOAT);

        // Le type de l'expression est bien FLOAT
        assertEquals(read.getType(), compiler.environmentType.FLOAT);
    }

    @Test
    public void testCodeGenPrint() throws DecacFatalError {
        compiler = new DecacCompiler(null, null);
        ReadFloat read = new ReadFloat();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadFloat readCodeGenPrint = new ReadFloat();
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
        ReadFloat read = new ReadFloat();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadFloat readCodeGenPrint = new ReadFloat();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        readCodeGenPrint.codeGenInst(compiler, Register.R0);
        assertEquals(read.getType(), readCodeGenPrint.getType());

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpected));
    }
}
