package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Multiply;
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
public class TestMultiply {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

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

        Multiply multiply4 = new Multiply(sonL, sonR);
        String result4 = multiply4.decompile();
        String expected4 = "( * )";
        assertThat(result4, is(expected4));
    }
}
