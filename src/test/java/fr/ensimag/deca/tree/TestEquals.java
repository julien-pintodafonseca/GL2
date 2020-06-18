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
// TODO
public class TestEquals {
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
        IMACodeGenCMPExpectedReverseTrue.add("BNE lb");
        IMACodeGenCMPExpectedReverseFalse.add("CMP R3, R2");
        IMACodeGenCMPExpectedReverseFalse.add("BEQ lb");
    }

    @Test
    public void testCodeGenCMPReverseTrue() throws DecacFatalError { // Cas où le paramètre reverse=true
        Equals equals = new Equals(sonL, sonR);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(5);

        equals.codeGenCMP(compiler, lb, true);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), equals.getLeftOperand().getType());
        assertThat(equals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), equals.getRightOperand().getType());
        assertThat(equals.getRightOperand(), is(sonR));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedReverseTrue));
    }

    @Test
    public void testCodeGenCMPReverseFalse() throws DecacFatalError { // Cas où le paramètre reverse=true
        Equals equals = new Equals(sonL, sonR);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(5);

        equals.codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(sonL.getType(), equals.getLeftOperand().getType());
        assertThat(equals.getLeftOperand(), is(sonL));
        assertEquals(sonR.getType(), equals.getRightOperand().getType());
        assertThat(equals.getRightOperand(), is(sonR));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenCMPExpectedReverseFalse));
    }
}
