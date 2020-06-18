package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.deca.tree.Not;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestNot {
    private final BooleanLiteral boolTrueExpr = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr = new BooleanLiteral(false);

    @Mock
    private AbstractExpr son;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        // !true
        Not not1 = new Not(boolTrueExpr);
        String result1 = not1.decompile();
        String expected1 = "!true";
        assertThat(result1, is(expected1));

        // !false
        Not not2 = new Not(boolFalseExpr);
        String result2 = not2.decompile();
        String expected2 = "!false";
        assertThat(result2, is(expected2));

        // AbstractExpr
        Not not3 = new Not(son);
        String result3 = not3.decompile();
        String expected3 = "!";
        assertThat(result3, is(expected3));
    }
}
