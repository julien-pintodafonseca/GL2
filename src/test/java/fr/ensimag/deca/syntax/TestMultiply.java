package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Multiply;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestMultiply {
  

 

    @Test
    public void testDecompile() {
        Multiply multiply1 = new Multiply(new IntLiteral(42), new IntLiteral(-6));
        String result1 = multiply1.decompile();
        String expected1 = "(42 * -6)";
        assertThat(result1, is(expected1));

        Multiply multiply2 = new Multiply(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = multiply2.decompile();
        String expected2 = "(0 * 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        Multiply multiply3 = new Multiply(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result3 = multiply3.decompile();
        String expected3 = "(-0x1.f61582p8 * 0x1.46e978p1)";
        assertThat(result3, is(expected3));
    }
}
