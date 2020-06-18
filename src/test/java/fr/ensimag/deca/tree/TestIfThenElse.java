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
public class TestIfThenElse {
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    @Mock private AbstractExpr conditionBool;
    @Mock private ListInst thenBranch;
    @Mock private ListInst elseBranch;

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conditionBool.getType()).thenReturn(compiler.environmentType.BOOLEAN);

        IMACodeGenInstExpected.add("BRA end_if0");
        IMACodeGenInstExpected.add("else0:");
        IMACodeGenInstExpected.add("end_if0:");
    }

    @Test
    public void testCodeGenInst() throws DecacFatalError {
        IfThenElse ifNode = new IfThenElse(conditionBool, thenBranch, elseBranch);
        compiler.setLabelManager();

        ifNode.codeGenInst(compiler);

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInstExpected));
    }
}
