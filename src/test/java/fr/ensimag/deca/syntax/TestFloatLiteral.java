package fr.ensimag.deca.syntax;


import fr.ensimag.deca.tree.FloatLiteral;

import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestFloatLiteral {
  
    @Test
    public void testGetValue() {
        FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
        FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertThat(float1.getValue(), equalTo(0.0f));
        assertThat(float2.getValue(), equalTo(5.2f));
    }
    
    @Test
    public void testdecompile() {
    	FloatLiteral float1 = new FloatLiteral(0.0f); // Cas d'une valeur nulle
    	FloatLiteral float2 = new FloatLiteral(5.2f); // Cas d'une valeur quelconque
         
        String result1 = float1.decompile();
        String expected1 = "0.0f";
        assertThat(result1, equalTo(expected1));
        
        String result2 = float2.decompile();
        String expected2 = "5.2f";
        assertThat(result2, equalTo(expected2));
    }

}
