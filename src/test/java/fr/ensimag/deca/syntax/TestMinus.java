package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Minus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestMinus {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        // int - int
        Minus minus1 = new Minus(new IntLiteral(42), new IntLiteral(-6));
        String result1 = minus1.decompile();
        String expected1 = "(42 - -6)";
        assertThat(result1, is(expected1));

        // int - float
        Minus minus2 = new Minus(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = minus2.decompile();
        String expected2 = "(0 - 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        // float - int
        Minus minus3 = new Minus(new FloatLiteral(42.00f), new IntLiteral(69));
        String result3 = minus3.decompile();
        String expected3 = "(0x1.5p5 - 69)";
        assertThat(result3, is(expected3));

        // float - float
        Minus minus4 = new Minus(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result4 = minus4.decompile();
        String expected4 = "(-0x1.f61582p8 - 0x1.46e978p1)";
        assertThat(result4, is(expected4));

        Minus minus5 = new Minus(sonL, sonR);
        String result5 = minus5.decompile();
        String expected5 = "( - )";
        assertThat(result5, is(expected5));
    }
}
