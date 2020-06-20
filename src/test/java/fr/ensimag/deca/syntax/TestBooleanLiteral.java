package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.BooleanLiteral;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestBooleanLiteral {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetValue() {
        BooleanLiteral boolTrue = new BooleanLiteral(true); // Cas d'une valeur True
        BooleanLiteral boolFalse = new BooleanLiteral(false); // Cas d'une valeur False

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertFalse(boolFalse.getValue());
        assertTrue(boolTrue.getValue());
    }
    
    @Test
    public void testDecompile() {
    	BooleanLiteral boolTrue = new BooleanLiteral(true); // Cas d'une valeur True
        BooleanLiteral boolFalse = new BooleanLiteral(false); // Cas d'une valeur False

        String result1 = boolTrue.decompile();
        String expected1 = "true";
        assertThat(result1, is(expected1));
        
        String result2 = boolFalse.decompile();
        String expected2 = "false";
        assertThat(result2, is(expected2));
    }
}
