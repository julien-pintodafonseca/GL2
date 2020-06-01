package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.Label;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestOr extends TestCase {

    @Mock
    AbstractExpr sonL;

    @Mock
    AbstractExpr sonR;

    @Mock
    Label lb;

    private DecacCompiler compiler;

    @Before
    public void setUp() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        compiler.setLabelManager();
        when(sonL.getType()).thenReturn(compiler.environmentType.BOOLEAN);
        when(sonR.getType()).thenReturn(compiler.environmentType.BOOLEAN);
    }

    @Test
    public void testCodeGenCMP() throws DecacFatalError {
        Or or = new Or(sonL, sonR);

        // Les appels récursifs à la méthode codeGenCMP() se font en gardant la même valeur pour l'argument "reverse"
        or.codeGenCMP(compiler, lb, true);
        verify(sonL).codeGenCMP(eq(compiler), any(Label.class), eq(true));
        verify(sonR).codeGenCMP(compiler, lb, true);

        or.codeGenCMP(compiler, lb, false);
        verify(sonL).codeGenCMP(compiler, lb, false);
        verify(sonR).codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), or.getLeftOperand().getType());
        assertThat(or.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), or.getRightOperand().getType());
        assertThat(or.getRightOperand(), is(sonR));
    }
}