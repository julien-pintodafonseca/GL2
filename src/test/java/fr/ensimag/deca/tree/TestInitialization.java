package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.DAddr;
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
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestInitialization {
    private final IntLiteral expectedIntLiteral = new IntLiteral(5);
    private final FloatLiteral expectedFloatLiteral = new FloatLiteral(5.5f);
    private final List<String> IMACodeGenInitializationExpected = new ArrayList<>();

    private final List<String> IMACodeGenInitializationFieldExpected = new ArrayList<>();

    @Mock private AbstractExpr exprInt;
    @Mock private AbstractExpr exprFloat;
    @Mock private AbstractExpr expr;
    @Mock private DAddr address;

    private DecacCompiler compiler1;
    private DecacCompiler compiler2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler1 = new DecacCompiler(null, null);
        compiler1.setRegisterManager(4);
        compiler1.setStackManager(compiler1.getRegisterManager());
        compiler1.getStackManager().setInClass(false);
        compiler2 = new DecacCompiler(null, null);
        compiler2.setRegisterManager(4);
        compiler2.setStackManager(compiler2.getRegisterManager());
        compiler2.getStackManager().setInClass(true);
        when(exprInt.verifyRValue(compiler1, null, null, compiler1.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler1, null, null, compiler1.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);
        when(exprInt.verifyRValue(compiler2, null, null, compiler2.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler2, null, null, compiler2.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);

        IMACodeGenInitializationExpected.add("STORE R0, address");

        IMACodeGenInitializationFieldExpected.add("LOAD -2(LB), R1");
        IMACodeGenInitializationFieldExpected.add("STORE R0, address");
    }

    @Test
    public void testCodeGenInitialization() throws DecacFatalError {
        Initialization init = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init.codeGenInitialization(compiler1, address);
        assertEquals(expr.getType(), init.getExpression().getType());
        assertThat(init.getExpression(), is(expr));

        String result = compiler1.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationExpected));
    }

    @Test
    public void testCodeGenInitializationField() throws DecacFatalError {
        Initialization init = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init.codeGenInitialization(compiler2, address);
        assertEquals(expr.getType(), init.getExpression().getType());
        assertThat(init.getExpression(), is(expr));

        String result = compiler2.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationFieldExpected));
    }
}
