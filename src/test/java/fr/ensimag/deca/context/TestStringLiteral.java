package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.StringLiteral;
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
public class TestStringLiteral {
    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Test
    public void testVerifyExpr() {
        StringLiteral str1 = new StringLiteral(""); // Cas d'une chaine vide
        StringLiteral str2 = new StringLiteral("hello"); // Cas d'une chaine quelconque

        // Le type renvoyé est bien STRING
        assertEquals(compiler.environmentType.STRING, str1.verifyExpr(compiler, null, null));
        assertEquals(compiler.environmentType.STRING, str2.verifyExpr(compiler, null, null));

        // Le type de l'expression est bien STRING
        assertEquals(compiler.environmentType.STRING, str1.getType());
        assertEquals(compiler.environmentType.STRING, str2.getType());
    }
}
