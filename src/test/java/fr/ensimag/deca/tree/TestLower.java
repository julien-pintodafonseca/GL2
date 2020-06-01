package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.Label;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

public class TestLower extends TestCase {
    @Mock
    AbstractExpr sonL;

    @Mock
    AbstractExpr sonR;

    @Mock
    Label lb;

    @Mock
    DecacCompiler compiler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(compiler.getRegisterManager()).thenReturn(new RegisterManager(5));
    }

    @Test
    public void testCodeGenCMPReverseTrue() throws DecacFatalError { // Cas où le paramètre reverse=true
        Lower lower = new Lower(sonL, sonR);

        lower.codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), lower.getLeftOperand().getType());
        assertThat(lower.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), lower.getRightOperand().getType());
        assertThat(lower.getRightOperand(), is(sonR));
    }

    @Test
    public void testCodeGenCMPReverseFalse() throws DecacFatalError { // Cas où le paramètre reverse=true
        Lower lower = new Lower(sonL, sonR);

        lower.codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), lower.getLeftOperand().getType());
        assertThat(lower.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), lower.getRightOperand().getType());
        assertThat(lower.getRightOperand(), is(sonR));
    }

}