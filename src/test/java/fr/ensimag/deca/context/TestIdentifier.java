package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestIdentifier {
    @Mock EnvironmentExp localEnv;

    private DecacCompiler compiler = new DecacCompiler(null, null);
    AbstractIdentifier identExist = new Identifier(compiler.createSymbol("ident1"));
    AbstractIdentifier identNotExist = new Identifier(compiler.createSymbol("ident2"));
    AbstractIdentifier typeExist = new Identifier(compiler.createSymbol("int"));
    AbstractIdentifier typeNotExist = new Identifier(compiler.createSymbol("typeStrange"));


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(localEnv.get(identExist.getName())).thenReturn(new VariableDefinition(compiler.environmentType.FLOAT, null));
        when(localEnv.get(identNotExist.getName())).thenReturn(null);
    }

    @Test
    public void testVerifyExpr() throws DecacFatalError, ContextualError {
        Type typeResult = identExist.verifyExpr(compiler, localEnv, null);

        assertThat(typeResult, is(compiler.environmentType.FLOAT));
        assertThat(identExist.getType(), is(compiler.environmentType.FLOAT));


        ContextualError expected1 =
                new ContextualError("La variable suivante est indéfinie : " + identNotExist.getName(), null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> { identNotExist.verifyExpr(compiler, localEnv, null);});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testVerifyType() throws ContextualError {
        Type typeResult = typeExist.verifyType(compiler);

        assertThat(typeResult, is(compiler.environmentType.INT));
        assertThat(typeExist.getDefinition(), is(compiler.environmentType.defOfType(compiler.environmentType.INT.getName())));


        ContextualError expected1 =
                new ContextualError("Le type de variable suivant est indéfini : " + typeNotExist.getName(), null);

        ContextualError result1 = assertThrows(ContextualError.class, () -> { typeNotExist.verifyType(compiler);});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }
}
