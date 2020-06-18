package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
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
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestInitialization {
    private final IntLiteral expectedIntLiteral = new IntLiteral(5);
    private final FloatLiteral expectedFloatLiteral = new FloatLiteral(5.5f);
    private final UnsupportedOperationException expectedNoMoreRegister =
            new UnsupportedOperationException("no more available registers : policy not yet implemented");
    private final List<String> IMACodeGenInitializationExpected = new ArrayList<>();

    @Mock private AbstractExpr exprInt;
    @Mock private AbstractExpr exprFloat;
    @Mock private AbstractExpr expr;
    @Mock private DAddr address;

    private DecacCompiler compiler;
    private DecacCompiler compilerWithoutAvailableRegisters;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(4);
        when(exprInt.verifyRValue(compiler, null, null, compiler.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler, null, null, compiler.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);

        compilerWithoutAvailableRegisters = new DecacCompiler(null, null);
        compilerWithoutAvailableRegisters.setRegisterManager(8);
        int i;
        while ((i = compilerWithoutAvailableRegisters.getRegisterManager().nextAvailable()) != -1) { // on marque tous les registres comme étant utilisés
            compilerWithoutAvailableRegisters.getRegisterManager().take(i);
        }

        IMACodeGenInitializationExpected.add("STORE R2, address");
    }

    @Test
    public void testVerifyInitialization() throws ContextualError {
        Initialization initInt = new Initialization(exprInt);
        initInt.verifyInitialization(compiler, compiler.environmentType.INT, null, null);
        assertEquals(expectedIntLiteral, initInt.getExpression());

        Initialization initFloat = new Initialization(exprFloat);
        initFloat.verifyInitialization(compiler, compiler.environmentType.FLOAT, null, null);
        assertEquals(expectedFloatLiteral, initFloat.getExpression());
    }

    @Test
    public void testCodeGenInitialization() throws DecacFatalError {
        Initialization init = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init.codeGenInitialization(compiler, address);
        assertEquals(expr.getType(), init.getExpression().getType());
        assertThat(init.getExpression(), is(expr));

        String result = compiler.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationExpected));

        // TODO
        /*
        // Levée d'une erreur si plus de registre disponible
        UnsupportedOperationException resultNoMoreRegister = assertThrows(UnsupportedOperationException.class, () -> {
            init.codeGenInitialization(compilerWithoutAvailableRegisters, address);
        });
        assertThat(resultNoMoreRegister.getMessage(), is(expectedNoMoreRegister.getMessage()));
         */
    }
}
