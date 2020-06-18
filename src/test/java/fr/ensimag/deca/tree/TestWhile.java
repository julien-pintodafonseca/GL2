package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestWhile {
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    @Mock private AbstractExpr conditionBool;
    @Mock private ListInst body;

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conditionBool.getType()).thenReturn(compiler.environmentType.BOOLEAN);
       
        IMACodeGenInstExpected.add("while0:");
        IMACodeGenInstExpected.add("BRA while0");
        IMACodeGenInstExpected.add("while_end0:");
    }

    @Test
    public void testCodeGenInst() throws DecacFatalError {
        While whileNode = new While(conditionBool, body);
        compiler.setLabelManager();

        whileNode.codeGenInst(compiler);

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpected));
    }
}
