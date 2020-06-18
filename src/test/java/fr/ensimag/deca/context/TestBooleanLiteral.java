package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
public class TestBooleanLiteral {

    private DecacCompiler compilerBoolTrue;
    private DecacCompiler compilerBoolFalse;

    @Test
    public void testVerifyExpr() throws ContextualError {
        compilerBoolTrue = new DecacCompiler(null, null);
        compilerBoolFalse = new DecacCompiler(null, null);

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
