package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestWhile {
    private final List<String> IMACodeGenInstExpected = new ArrayList<>();

    @Mock private AbstractExpr conditionBool;
    @Mock private AbstractExpr conditionNotBool;
    @Mock private ListInst body;
    @Mock private EnvironmentExp localEnv;
    @Mock private ClassDefinition currentClass;
    @Mock private Type returnType;

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conditionBool.getType()).thenReturn(compiler.environmentType.BOOLEAN);
        when(conditionNotBool.getType()).thenReturn(compiler.environmentType.INT);

        IMACodeGenInstExpected.add("while0:");
        IMACodeGenInstExpected.add("BRA while0");
        IMACodeGenInstExpected.add("while_end0:");
    }

    @Test
    public void testVerifyInst() throws ContextualError, DecacFatalError {
        While whileNodeBool = new While(conditionBool, body);
        While whileNodeNotBool = new While(conditionNotBool, body);

        whileNodeBool.verifyInst(compiler, localEnv, currentClass, returnType);
        verify(conditionBool).verifyExpr(compiler, localEnv, currentClass);
        verify(body).verifyListInst(compiler, localEnv, currentClass, returnType);

        // Levée d'une erreur si la condition n'est pas de type booléen
        ContextualError resultNotBooleanType = assertThrows(ContextualError.class, () -> {
            whileNodeNotBool.verifyInst(compiler, null, null, null);
        });
        assertThat(resultNotBooleanType.getMessage(), is(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE +
                conditionNotBool.getType()));
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
