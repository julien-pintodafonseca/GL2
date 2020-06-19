package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.BooleanLiteral;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestBooleanLiteral {

    @Test
    public void testVerifyExpr() throws ContextualError {
        DecacCompiler compilerBoolTrue = new DecacCompiler(null, null);
        DecacCompiler compilerBoolFalse = new DecacCompiler(null, null);

        BooleanLiteral boolTrue = new BooleanLiteral(true); // Cas d'une valeur True
        BooleanLiteral boolFalse = new BooleanLiteral(false); // Cas d'une valeur False

        // Le type renvoyé est bien BOOLEAN
        assertEquals(compilerBoolTrue.environmentType.BOOLEAN, boolTrue.verifyExpr(compilerBoolTrue, null, null));
        assertEquals(compilerBoolFalse.environmentType.BOOLEAN, boolFalse.verifyExpr(compilerBoolFalse, null, null));

        // Le type de l'expression est bien BOOLEAN
        assertEquals(compilerBoolTrue.environmentType.BOOLEAN, boolTrue.getType());
        assertEquals(compilerBoolFalse.environmentType.BOOLEAN, boolFalse.getType());
    }
}
