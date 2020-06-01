package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class TestFloatLiteral extends TestCase {

    private DecacCompiler compiler = new DecacCompiler(null, null);

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
    public void testCodeGenPrint() {
        // Cas d'une valeur nulle
        FloatLiteral float1 = new FloatLiteral(0.0f);
        float1.verifyExpr(compiler, null, null);
        FloatLiteral float1CodeGenPrint = new FloatLiteral(0.0f);
        float1CodeGenPrint.verifyExpr(compiler, null, null);

        // Cas d'une valeur quelconque
        FloatLiteral float2 = new FloatLiteral(5.2f);
        float2.verifyExpr(compiler, null, null);
        FloatLiteral float2CodeGenPrint = new FloatLiteral(5.2f);
        float2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float1CodeGenPrint.codeGenPrint(compiler);
        assertEquals(float1.getValue(), float1CodeGenPrint.getValue());
        assertEquals(float1.getType(), float1CodeGenPrint.getType());

        float2CodeGenPrint.codeGenPrint(compiler);
        assertEquals(float2.getValue(), float2CodeGenPrint.getValue());
        assertEquals(float2.getType(), float2CodeGenPrint.getType());
    }

    @Test
    public void testCodeGenInst() {
        // Cas d'une valeur nulle
        FloatLiteral float1 = new FloatLiteral(0.0f);
        float1.verifyExpr(compiler, null, null);
        FloatLiteral float1CodeGenPrint = new FloatLiteral(0.0f);
        float1CodeGenPrint.verifyExpr(compiler, null, null);

        // Cas d'une valeur quelconque
        FloatLiteral float2 = new FloatLiteral(5.2f);
        float2.verifyExpr(compiler, null, null);
        FloatLiteral float2CodeGenPrint = new FloatLiteral(5.2f);
        float2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        float1CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(float1.getValue(), float1CodeGenPrint.getValue());
        assertEquals(float1.getType(), float1CodeGenPrint.getType());

        float2CodeGenPrint.codeGenInst(compiler, Register.R1);
        assertEquals(float2.getValue(), float2CodeGenPrint.getValue());
        assertEquals(float2.getType(), float2CodeGenPrint.getType());
    }
}