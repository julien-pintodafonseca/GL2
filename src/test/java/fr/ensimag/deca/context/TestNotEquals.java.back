package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.NotEquals;
import fr.ensimag.ima.pseudocode.Label;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestNotEquals extends TestCase {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;
    @Mock private Label lb;
    @Mock private DecacCompiler compiler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(compiler.getRegisterManager()).thenReturn(new RegisterManager(5));
    }

    @Test
    public void testCodeGenCMPReverseTrue() throws DecacFatalError { // Cas où le paramètre reverse=true
        NotEquals notEquals = new NotEquals(sonL, sonR);

        notEquals.codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), notEquals.getLeftOperand().getType());
        assertThat(notEquals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), notEquals.getRightOperand().getType());
        assertThat(notEquals.getRightOperand(), is(sonR));
    }

    @Test
    public void testCodeGenCMPReverseFalse() throws DecacFatalError { // Cas où le paramètre reverse=true
        NotEquals notEquals = new NotEquals(sonL, sonR);

        notEquals.codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), notEquals.getLeftOperand().getType());
        assertThat(notEquals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), notEquals.getRightOperand().getType());
        assertThat(notEquals.getRightOperand(), is(sonR));
    }
}
