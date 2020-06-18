package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.utils.Utils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
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
public class TestPlus {
    private final Type INT = new IntType(null);
    private final Type FLOAT = new FloatType(null);
    private final GPRegister reg1 = Register.R0;
    private final GPRegister reg2 = Register.R1;

    @Mock private AbstractExpr intexpr1;
    @Mock private AbstractExpr intexpr2;
    @Mock private AbstractExpr floatexpr1;
    @Mock private AbstractExpr floatexpr2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(intexpr1.getType()).thenReturn(INT);
        when(intexpr2.getType()).thenReturn(INT);
        when(floatexpr1.getType()).thenReturn(FLOAT);
        when(floatexpr2.getType()).thenReturn(FLOAT);
    }

    @Test
    public void testCodeGenInstArith() throws DecacFatalError {
        // on test l'ajout de l'instruction ADD
        DecacCompiler myCompiler = new DecacCompiler(null, null);
        myCompiler.setErrorLabelManager();

        Plus plus = new Plus(intexpr1, intexpr2);
        plus.codeGenInstArith(myCompiler, reg1, reg2);

        List<String> expected = Collections.singletonList("ADD R0, R1");
        String result = myCompiler.displayIMAProgram();

        assertThat(Utils.normalizeDisplay(result).size(), is(expected.size()));
        assertTrue(Utils.normalizeDisplay(result).containsAll(expected));

        // ------------------------------------------------------------

        // on test la boucle genCodeError
        List<Plus> plusList = new ArrayList<>();
        plusList.add(new Plus(intexpr1, floatexpr2));
        plusList.add(new Plus(floatexpr1, intexpr2));
        plusList.add(new Plus(floatexpr1, floatexpr2));

        List<String> expectedFor = new ArrayList<>();
        expectedFor.add("ADD R0, R1");
        expectedFor.add("BOV arithmetic_overflow; Overflow : check for previous operation");

        for (Plus p : plusList) {
            // on reset le compiler
            myCompiler = new DecacCompiler(null, null);
            myCompiler.setErrorLabelManager();

            // on test l'ajout de l'instruction SUB et BOV avec Overflow
            p.codeGenInstArith(myCompiler, reg1, reg2);
            String resultFor = myCompiler.displayIMAProgram();
            assertThat(Utils.normalizeDisplay(resultFor).size(), is(expectedFor.size()));
            assertTrue(Utils.normalizeDisplay(resultFor).containsAll(expectedFor));
        }
    }
}
