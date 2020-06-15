package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Plus;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestPlus {

    @Test
    public void testDecompile() {
        Plus plus1 = new Plus(new IntLiteral(42), new IntLiteral(-6));
        String result1 = plus1.decompile();
        String expected1 = "(42 + -6)";
        assertThat(result1, is(expected1));

        Plus plus2 = new Plus(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = plus2.decompile();
        String expected2 = "(0 + 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        Plus plus3 = new Plus(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result3 = plus3.decompile();
        String expected3 = "(-0x1.f61582p8 + 0x1.46e978p1)";
        assertThat(result3, is(expected3));
    }
}
