package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestModulo {
    private final DecacCompiler compiler = new DecacCompiler(null, null);
    private final Type INT = compiler.environmentType.INT;

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;


    private GPRegister reg1;
    private GPRegister reg2;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        reg1 = Register.R0;
        reg2 = Register.R1;
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr1.getType()).thenReturn(INT);
        when(intexpr2.getType()).thenReturn(INT);
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

}
