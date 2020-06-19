package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Greater;
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
public class TestGreater {
    @Mock private AbstractExpr sonL;
    @Mock private AbstractExpr sonR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecompile() {
        Greater greater1 = new Greater(sonL, sonR);
        String result1 = greater1.decompile();
        String expected1 = "( > )";
        assertThat(result1, is(expected1));
    }
}
