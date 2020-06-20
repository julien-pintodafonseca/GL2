package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.While;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
public class TestWhile {
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
}
