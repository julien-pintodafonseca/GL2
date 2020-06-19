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
    private final List<String> IMACodeGenInitializationNoMoreRegistersExpected = new ArrayList<>();

    private final List<String> IMACodeGenInitializationFieldExpected = new ArrayList<>();
    private final List<String> IMACodeGenInitializationFieldNoMoreRegistersExpected = new ArrayList<>();

    @Mock private AbstractExpr exprInt;
    @Mock private AbstractExpr exprFloat;
    @Mock private AbstractExpr expr;
    @Mock private DAddr address;

    private DecacCompiler compiler1;
    private DecacCompiler compilerWithoutAvailableRegisters1;
    private DecacCompiler compiler2;
    private DecacCompiler compilerWithoutAvailableRegisters2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler1 = new DecacCompiler(null, null);
        compiler1.setRegisterManager(4);
        compiler2 = new DecacCompiler(null, null);
        compiler2.setRegisterManager(4);
        when(exprInt.verifyRValue(compiler1, null, null, compiler1.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler1, null, null, compiler1.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);
        when(exprInt.verifyRValue(compiler2, null, null, compiler2.environmentType.INT)).thenReturn(expectedIntLiteral);
        when(exprFloat.verifyRValue(compiler2, null, null, compiler2.environmentType.FLOAT)).thenReturn(expectedFloatLiteral);

        compilerWithoutAvailableRegisters1 = new DecacCompiler(null, null);
        compilerWithoutAvailableRegisters1.setRegisterManager(8);
        compilerWithoutAvailableRegisters1.setTSTOManager();
        int i;
        while ((i = compilerWithoutAvailableRegisters1.getRegisterManager().nextAvailable()) != -1) { // on marque tous les registres comme étant utilisés
            compilerWithoutAvailableRegisters1.getRegisterManager().take(i);
        }

        compilerWithoutAvailableRegisters2 = new DecacCompiler(null, null);
        compilerWithoutAvailableRegisters2.setRegisterManager(8);
        compilerWithoutAvailableRegisters2.setTSTOManager();
        while ((i = compilerWithoutAvailableRegisters2.getRegisterManager().nextAvailable()) != -1) { // on marque tous les registres comme étant utilisés
            compilerWithoutAvailableRegisters2.getRegisterManager().take(i);
        }

        IMACodeGenInitializationExpected.add("STORE R2, address");
        IMACodeGenInitializationNoMoreRegistersExpected.add("PUSH R7");
        IMACodeGenInitializationNoMoreRegistersExpected.add("STORE R7, address");
        IMACodeGenInitializationNoMoreRegistersExpected.add("POP R7");

        IMACodeGenInitializationFieldExpected.add("LOAD -2(LB), R1");
        IMACodeGenInitializationFieldExpected.add("STORE R2, address");
        IMACodeGenInitializationFieldNoMoreRegistersExpected.add("PUSH R7");
        IMACodeGenInitializationFieldNoMoreRegistersExpected.add("LOAD -2(LB), R1");
        IMACodeGenInitializationFieldNoMoreRegistersExpected.add("STORE R7, address");
        IMACodeGenInitializationFieldNoMoreRegistersExpected.add("POP R7");
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


        Initialization init2 = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init2.codeGenInitialization(compilerWithoutAvailableRegisters1, address);
        assertEquals(expr.getType(), init2.getExpression().getType());
        assertThat(init2.getExpression(), is(expr));

        String result2 = compilerWithoutAvailableRegisters1.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationNoMoreRegistersExpected));
    }

    @Test
    public void testCodeGenInitializationField() throws DecacFatalError {
        Initialization init = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init.codeGenInitializationField(compiler2, address);
        assertEquals(expr.getType(), init.getExpression().getType());
        assertThat(init.getExpression(), is(expr));

        String result = compiler2.displayIMAProgram();
        assertThat(normalizeDisplay(result), is(IMACodeGenInitializationFieldExpected));


        Initialization init2 = new Initialization(expr);

        // Pas de modification des attributs lors de la génération de code
        init2.codeGenInitializationField(compilerWithoutAvailableRegisters2, address);
        assertEquals(expr.getType(), init2.getExpression().getType());
        assertThat(init2.getExpression(), is(expr));

        String result2 = compilerWithoutAvailableRegisters2.displayIMAProgram();
        assertThat(normalizeDisplay(result2), is(IMACodeGenInitializationFieldNoMoreRegistersExpected));
    }
}
