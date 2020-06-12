package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.And;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestAnd {
    private final List<String> IMACodeGenCMPExpectedAndTrue = new ArrayList<>();
    private final List<String> IMACodeGenCMPExpectedAndFalse = new ArrayList<>();

    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;
    @Mock private Label lb;

    private DecacCompiler compiler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        compiler.setLabelManager();
        when(sonL.getType()).thenReturn(compiler.environmentType.BOOLEAN);
        when(sonR.getType()).thenReturn(compiler.environmentType.BOOLEAN);
        IMACodeGenCMPExpectedAndTrue.add("");
        IMACodeGenCMPExpectedAndFalse.add("BRA or_end0");
        IMACodeGenCMPExpectedAndFalse.add("or0:");
        IMACodeGenCMPExpectedAndFalse.add("or_end0:");
    }

    @Test
    public void testCodeGenCMP() throws DecacFatalError {
        And and = new And(sonL, sonR);

        // Les appels récursifs à la méthode codeGenCMP() se font en gardant la même valeur pour l'argument "reverse"
        and.codeGenCMP(compiler, lb, false);
        verify(sonL).codeGenCMP(eq(compiler), any(Label.class), eq(false));
        verify(sonR).codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), and.getLeftOperand().getType());
        assertThat(and.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), and.getRightOperand().getType());
        assertThat(and.getRightOperand(), is(sonR));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedAndFalse));

        compiler = new DecacCompiler(null, null);

        // Les appels récursifs à la méthode codeGenCMP() se font en gardant la même valeur pour l'argument "reverse"
        and.codeGenCMP(compiler, lb, true);
        verify(sonL).codeGenCMP(compiler, lb, true);
        verify(sonR).codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), and.getLeftOperand().getType());
        assertThat(and.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), and.getRightOperand().getType());
        assertThat(and.getRightOperand(), is(sonR));

        result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedAndTrue));
    }
}
