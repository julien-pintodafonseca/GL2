package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.IntLiteral;
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
public class TestIntLiteral {

    private DecacCompiler compiler;

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
}
