package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Plus;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the Plus node using mockito, without using advanced features.
 * @see TestPlusAdvanced for more advanced examples.
 * @see TestPlusWithoutMock too see what would need to be written if the test
 * was done without using Mockito.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestPlusPlain {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);

    @Test
    public void testType() throws ContextualError, DecacFatalError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        AbstractExpr left = Mockito.mock(AbstractExpr.class);
        when(left.verifyExpr(compiler, null, null)).thenReturn(INT);
        AbstractExpr right = Mockito.mock(AbstractExpr.class);
        when(right.verifyExpr(compiler, null, null)).thenReturn(INT);
        Plus plus = new Plus(left, right);
        // check the result
        assertTrue(plus.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(left).verifyExpr(compiler, null, null);
        verify(right).verifyExpr(compiler, null, null);
    }
}
