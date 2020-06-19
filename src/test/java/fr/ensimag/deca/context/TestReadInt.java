package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.ReadInt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestReadInt {
    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Test
    public void testVerifyExpr() {
        ReadInt read = new ReadInt();

        // Le type renvoyé est bien INT
        assertEquals(read.verifyExpr(compiler, null, null), compiler.environmentType.INT);

        // Le type de l'expression est bien INT
        assertEquals(read.getType(), compiler.environmentType.INT);
    }
}
