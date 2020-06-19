package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.*;
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
public class TestDivide {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        Divide divide1 = new Divide(new IntLiteral(42), new IntLiteral(-6));
        String result1 = divide1.decompile();
        String expected1 = "(42 / -6)";
        assertThat(result1, is(expected1));

        Divide divide2 = new Divide(new IntLiteral(0), new FloatLiteral(2.554f));
        String result2 = divide2.decompile();
        String expected2 = "(0 / 0x1.46e978p1)";
        assertThat(result2, is(expected2));

        Divide divide3 = new Divide(new FloatLiteral(-502.084f), new FloatLiteral(2.554f));
        String result3 = divide3.decompile();
        String expected3 = "(-0x1.f61582p8 / 0x1.46e978p1)";
        assertThat(result3, is(expected3));

        Divide divide4 = new Divide(sonL, sonR);
        String result4 = divide4.decompile();
        String expected4 = "( / )";
        assertThat(result4, is(expected4));
    }
}
