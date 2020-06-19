package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Initialization;
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
public class TestInitialization {
    @Mock private AbstractExpr expr;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        Initialization init = new Initialization(expr);

        String result1 = init.decompile();
        String expected1 = " = ";
        assertThat(result1, is(expected1));
    }
}
