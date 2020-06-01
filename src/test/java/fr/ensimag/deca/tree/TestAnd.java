package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.Label;
import junit.framework.TestCase;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.progress.ArgumentMatcherStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestAnd extends TestCase {

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
        And and = new And(sonL, sonR);

        // Les appels récursifs à la méthode codeGenCMP() se font en gardant la même valeur pour l'argument "reverse"
        and.codeGenCMP(compiler, lb, false);
        verify(sonL).codeGenCMP(eq(compiler), any(Label.class), eq(false));
        verify(sonR).codeGenCMP(compiler, lb, false);

        and.codeGenCMP(compiler, lb, true);
        verify(sonL).codeGenCMP(compiler, lb, true);
        verify(sonR).codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), and.getLeftOperand().getType());
        assertThat(and.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), and.getRightOperand().getType());
        assertThat(and.getRightOperand(), is(sonR));
    }

}