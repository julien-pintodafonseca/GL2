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

public class TestLowerOrEqual extends TestCase {
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
        LowerOrEqual lowerOrEqual = new LowerOrEqual(sonL, sonR);

        lowerOrEqual.codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), lowerOrEqual.getLeftOperand().getType());
        assertThat(lowerOrEqual.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), lowerOrEqual.getRightOperand().getType());
        assertThat(lowerOrEqual.getRightOperand(), is(sonR));
    }

    @Test
    public void testCodeGenCMPReverseFalse() throws DecacFatalError { // Cas où le paramètre reverse=true
        LowerOrEqual lowerOrEqual = new LowerOrEqual(sonL, sonR);

        lowerOrEqual.codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), lowerOrEqual.getLeftOperand().getType());
        assertThat(lowerOrEqual.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), lowerOrEqual.getRightOperand().getType());
        assertThat(lowerOrEqual.getRightOperand(), is(sonR));
    }

}