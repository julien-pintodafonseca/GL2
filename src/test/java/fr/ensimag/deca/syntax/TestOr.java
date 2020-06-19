package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.deca.tree.Greater;
import fr.ensimag.deca.tree.Or;
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
public class TestOr {
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);

    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        // true || true
        Or or1 = new Or(boolTrueExpr1, boolTrueExpr2);
        String result1 = or1.decompile();
        String expected1 = "(true || true)";
        assertThat(result1, is(expected1));

        // true || false
        Or or2 = new Or(boolTrueExpr1, boolFalseExpr2);
        String result2 = or2.decompile();
        String expected2 = "(true || false)";
        assertThat(result2, is(expected2));

        // false || true
        Or or3 = new Or(boolFalseExpr1, boolTrueExpr2);
        String result3 = or3.decompile();
        String expected3 = "(false || true)";
        assertThat(result3, is(expected3));

        // false || false
        Or or4 = new Or(boolFalseExpr1, boolFalseExpr2);
        String result4 = or4.decompile();
        String expected4 = "(false || false)";
        assertThat(result4, is(expected4));

        Or or5 = new Or(sonL, sonR);
        String result5 = or5.decompile();
        String expected5 = "( || )";
        assertThat(result5, is(expected5));
    }
}
