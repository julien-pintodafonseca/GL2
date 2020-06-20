package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Plus;
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
public class TestPlus {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        // int + int
        Plus plus1 = new Plus(new IntLiteral(42), new IntLiteral(-6));
        String result1 = plus1.decompile();
        String expected1 = "(42 + -6)";
        assertThat(result1, is(expected1));

        // int + float
        Plus plus2 = new Plus(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = plus2.decompile();
        String expected2 = "(0 + 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        // float + int
        Plus plus3 = new Plus(new FloatLiteral(42.00f), new IntLiteral(69));
        String result3 = plus3.decompile();
        String expected3 = "(0x1.5p5 + 69)";
        assertThat(result3, is(expected3));

        // float + float
        Plus plus4 = new Plus(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result4 = plus4.decompile();
        String expected4 = "(-0x1.f61582p8 + 0x1.46e978p1)";
        assertThat(result4, is(expected4));

        Plus plus5 = new Plus(sonL, sonR);
        String result5 = plus5.decompile();

        String expected5 = "( + )";
        assertThat(result5, is(expected5));
    }
}
