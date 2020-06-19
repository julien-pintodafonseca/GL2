package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestFloatLiteral {
    private DecacCompiler compiler;

    @Test
    public void testVerifyExpr() {
        compiler = new DecacCompiler(null, null);

        FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
        FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque

        // Le type renvoyé est bien FLOAT
        assertThat(compiler.environmentType.FLOAT, equalTo(float1.verifyExpr(compiler, null, null)));
        assertThat(compiler.environmentType.FLOAT, equalTo(float2.verifyExpr(compiler, null, null)));

        // Le type de l'expression est bien FLOAT
        assertThat(compiler.environmentType.FLOAT, equalTo(float1.getType()));
        assertThat(compiler.environmentType.FLOAT, equalTo(float2.getType()));
    }
}
