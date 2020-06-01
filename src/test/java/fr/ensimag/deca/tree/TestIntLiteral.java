package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TestIntLiteral extends TestCase {

    private DecacCompiler compiler = new DecacCompiler(null, null);

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
    public void testCodeGenPrint() {
        // Cas d'une valeur nulle
        IntLiteral int1 = new IntLiteral(0);
        int1.verifyExpr(compiler, null, null);
        IntLiteral int1CodeGenPrint = new IntLiteral(0);
        int1CodeGenPrint.verifyExpr(compiler, null, null);

        // Cas d'une valeur quelconque
        IntLiteral int2 = new IntLiteral(5);
        int2.verifyExpr(compiler, null, null);
        IntLiteral int2CodeGenPrint = new IntLiteral(5);
        int2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int1CodeGenPrint.codeGenPrint(compiler);
        assertEquals(int1.getValue(), int1CodeGenPrint.getValue());
        assertEquals(int1.getType(), int1CodeGenPrint.getType());

        int2CodeGenPrint.codeGenPrint(compiler);
        assertEquals(int2.getValue(), int2CodeGenPrint.getValue());
        assertEquals(int2.getType(), int2CodeGenPrint.getType());
    }

    @Test
    public void testCodeGenInst() {
        // Cas d'une valeur nulle
        IntLiteral int1 = new IntLiteral(0);
        int1.verifyExpr(compiler, null, null);
        IntLiteral int1CodeGenPrint = new IntLiteral(0);
        int1CodeGenPrint.verifyExpr(compiler, null, null);

        // Cas d'une valeur quelconque
        IntLiteral int2 = new IntLiteral(5);
        int2.verifyExpr(compiler, null, null);
        IntLiteral int2CodeGenPrint = new IntLiteral(5);
        int2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        int1CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(int1.getValue(), int1CodeGenPrint.getValue());
        assertEquals(int1.getType(), int1CodeGenPrint.getType());

        int2CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(int2.getValue(), int2CodeGenPrint.getValue());
        assertEquals(int2.getType(), int2CodeGenPrint.getType());
    }
}