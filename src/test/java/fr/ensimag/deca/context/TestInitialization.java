package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Mock private AbstractExpr exprInt;
    @Mock private AbstractExpr exprFloat;

    private DecacCompiler compiler;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(4);
        when(exprInt.verifyRValue(compiler, null, null, compiler.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler, null, null, compiler.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);
    }

    @Test
    public void testVerifyInitialization() throws ContextualError, DecacFatalError {
        Initialization initInt = new Initialization(exprInt);
        initInt.verifyInitialization(compiler, compiler.environmentType.INT, null, null);
        assertEquals(expectedIntLiteral, initInt.getExpression());

        Initialization initFloat = new Initialization(exprFloat);
        initFloat.verifyInitialization(compiler, compiler.environmentType.FLOAT, null, null);
        assertEquals(expectedFloatLiteral, initFloat.getExpression());
    }
}
