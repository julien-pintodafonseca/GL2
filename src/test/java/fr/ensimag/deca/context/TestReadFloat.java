package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.ReadFloat;
import fr.ensimag.ima.pseudocode.GPRegister;
import junit.framework.TestCase;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestReadFloat extends TestCase {
    private DecacCompiler compiler = new DecacCompiler(null, null);

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
        ReadFloat read = new ReadFloat();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadFloat readCodeGenPrint = new ReadFloat();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        readCodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(read.getType(), readCodeGenPrint.getType());
    }

    @Test
    public void testCodeGenInst() throws DecacFatalError {
        ReadFloat read = new ReadFloat();
        compiler.setErrorLabelManager();
        read.verifyExpr(compiler, null, null);
        ReadFloat readCodeGenPrint = new ReadFloat();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        GPRegister register = mock(GPRegister.class);
        readCodeGenPrint.codeGenInst(compiler, register);
        assertEquals(read.getType(), readCodeGenPrint.getType());
    }
}
