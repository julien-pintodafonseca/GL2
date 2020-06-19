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
    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Test
    public void testVerifyExpr() {
        ReadFloat read = new ReadFloat();

        // Le type renvoyé est bien FLOAT
        assertEquals(read.verifyExpr(compiler, null, null), compiler.environmentType.FLOAT);

        // Le type de l'expression est bien FLOAT
        assertEquals(read.getType(), compiler.environmentType.FLOAT);
    }
}
