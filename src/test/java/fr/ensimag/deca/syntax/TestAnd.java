package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.And;
import fr.ensimag.deca.tree.BooleanLiteral;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestAnd {
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolTrueExpr2 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);
    private final BooleanLiteral boolFalseExpr2 = new BooleanLiteral(false);

    @Test
    public void testDecompile() {
        // true && true
        And and1 = new And(boolTrueExpr1, boolTrueExpr2);
        String result1 = and1.decompile();
        String expected1 = "(true && true)";
        assertThat(result1, is(expected1));

        // true && false
        And and2 = new And(boolTrueExpr1, boolFalseExpr2);
        String result2 = and2.decompile();
        String expected2 = "(true && false)";
        assertThat(result2, is(expected2));

        // false && true
        And and3 = new And(boolFalseExpr1, boolTrueExpr2);
        String result3 = and3.decompile();
        String expected3 = "(false && true)";
        assertThat(result3, is(expected3));

        // false && false
        And and4 = new And(boolFalseExpr1, boolFalseExpr2);
        String result4 = and4.decompile();
        String expected4 = "(false && false)";
        assertThat(result4, is(expected4));
    }
}
