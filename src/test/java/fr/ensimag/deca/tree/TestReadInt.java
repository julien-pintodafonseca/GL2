package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import junit.framework.TestCase;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class TestReadInt extends TestCase {

    private DecacCompiler compiler = new DecacCompiler(null, null);

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
        ReadInt read = new ReadInt();
        read.verifyExpr(compiler, null, null);
        ReadInt readCodeGenPrint = new ReadInt();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        // TODO
        //readCodeGenPrint.codeGenPrint(compiler, false);
        //assertEquals(read.getType(), readCodeGenPrint.getType());
    }

    @Test
    public void testCodeGenInst() throws DecacFatalError {
        ReadInt read = new ReadInt();
        read.verifyExpr(compiler, null, null);
        ReadInt readCodeGenPrint = new ReadInt();
        readCodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        // TODO
        //GPRegister register = mock(GPRegister.class);
        //readCodeGenPrint.codeGenInst(compiler, register);
        //assertEquals(read.getType(), readCodeGenPrint.getType());

    }
}
