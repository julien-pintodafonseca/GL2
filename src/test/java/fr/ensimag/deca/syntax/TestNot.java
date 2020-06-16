package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.And;
import fr.ensimag.deca.tree.BooleanLiteral;
import fr.ensimag.deca.tree.Not;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestNot {
    private final BooleanLiteral boolTrueExpr1 = new BooleanLiteral(true);
    private final BooleanLiteral boolFalseExpr1 = new BooleanLiteral(false);

    @Test
    public void testDecompile() {
        // true
        Not not1 = new Not(boolTrueExpr1);
        String result1 = not1.decompile();
        String expected1 = "!true";
        assertThat(result1, is(expected1));

        // false
        Not not2 = new Not(boolFalseExpr1);
        String result2 = not2.decompile();
        String expected2 = "!false";
        assertThat(result2, is(expected2));

    }

}
