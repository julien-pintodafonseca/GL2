package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
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

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestNotEquals {
    private final List<String> IMACodeGenCMPExpectedReverseTrue = new ArrayList<>();
    private final List<String> IMACodeGenCMPExpectedReverseFalse = new ArrayList<>();

    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;
    @Mock private Label lb;

    private DecacCompiler compiler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IMACodeGenCMPExpectedReverseTrue.add("CMP R3, R2");
        IMACodeGenCMPExpectedReverseTrue.add("BEQ lb");
        IMACodeGenCMPExpectedReverseFalse.add("CMP R3, R2");
        IMACodeGenCMPExpectedReverseFalse.add("BNE lb");
    }

    @Test
    public void testCodeGenCMPReverseTrue() throws DecacFatalError { // Cas où le paramètre reverse=true
        NotEquals notEquals = new NotEquals(sonL, sonR);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(5);

        notEquals.codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), notEquals.getLeftOperand().getType());
        assertThat(notEquals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), notEquals.getRightOperand().getType());
        assertThat(notEquals.getRightOperand(), is(sonR));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedReverseTrue));
    }

    @Test
    public void testCodeGenCMPReverseFalse() throws DecacFatalError { // Cas où le paramètre reverse=true
        NotEquals notEquals = new NotEquals(sonL, sonR);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(5);

        notEquals.codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), notEquals.getLeftOperand().getType());
        assertThat(notEquals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), notEquals.getRightOperand().getType());
        assertThat(notEquals.getRightOperand(), is(sonR));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedReverseFalse));
    }
}
