package fr.ensimag.deca.syntax;


import fr.ensimag.deca.tree.FloatLiteral;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Modulo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestModulo {  

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
