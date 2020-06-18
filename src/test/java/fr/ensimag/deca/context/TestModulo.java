package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.utils.Utils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestModulo {
    private final DecacCompiler compiler = new DecacCompiler(null, null);
    private final Type INT = compiler.environmentType.INT;
    private final Type FLOAT = compiler.environmentType.FLOAT;

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;

    private GPRegister reg1;
    private GPRegister reg2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        reg1 = Register.R0;
        reg2 = Register.R1;
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(intexpr1.getType()).thenReturn(INT);
        when(intexpr2.getType()).thenReturn(INT);
        when(floatexpr1.getType()).thenReturn(FLOAT);
        when(floatexpr2.getType()).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError, DecacFatalError {
        Modulo modulo = new Modulo(intexpr1, intexpr2);
        // check the result
        assertTrue(modulo.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Modulo modulo = new Modulo(intexpr1, floatexpr1);

        // Levée d'une erreur si une des opérandes n'est pas un entier
        ContextualError resultModuloFloat = assertThrows(ContextualError.class, () -> {
            modulo.verifyExpr(compiler, null, null);
        });
        assertThat(resultModuloFloat.getMessage(), is(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE + INT + " (pour null) et " + FLOAT + " (pour null)."));
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Modulo modulo = new Modulo(floatexpr1, intexpr1);

        // Levée d'une erreur si une des opérandes n'est pas un entier
        ContextualError resultModuloFloat = assertThrows(ContextualError.class, () -> {
            modulo.verifyExpr(compiler, null, null);
        });
        assertThat(resultModuloFloat.getMessage(), is(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE + FLOAT + " (pour null) et "+ INT + " (pour null)."));
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError, ContextualError {
        // on test l'ajout de l'instruction REM
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setErrorLabelManager();

        Modulo modulo = new Modulo(intexpr1, intexpr2);
        modulo.setType(INT);
        modulo.codeGenInstArith(myCompiler, reg1, reg2);

        List<String> expected = new ArrayList<>();
        expected.add("REM R0, R1");
        expected.add("BOV arithmetic_overflow; Overflow : check for previous operation");
        String result = myCompiler.displayIMAProgram();

        assertThat(Utils.normalizeDisplay(result).size(), is(expected.size()));
        assertTrue(Utils.normalizeDisplay(result).containsAll(expected));

    }

    @Test
    public void testDecompile() {
        Modulo modulo1 = new Modulo(new IntLiteral(42), new IntLiteral(-6));
        String result1 = modulo1.decompile();
        String expected1 = "(42 % -6)";
        assertThat(result1, is(expected1));

        Modulo modulo2 = new Modulo(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = modulo2.decompile();
        String expected2 = "(0 % 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        Modulo modulo3 = new Modulo(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result3 = modulo3.decompile();
        String expected3 = "(-0x1.f61582p8 % 0x1.46e978p1)";
        assertThat(result3, is(expected3));
    }
}
