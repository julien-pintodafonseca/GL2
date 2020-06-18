package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.StringLiteral;

import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestStringLiteral {
   
    @Test
    public void testDecompile() {
    	 
    	StringLiteral string1 = new StringLiteral(""); // Cas d'une chaine vide
    	StringLiteral string2 = new StringLiteral("hello"); // Cas d'une chaine quelconque

        String result1 = string1.decompile();
        String expected1 = "\"\"";
        assertThat(result1, is(expected1));
        
        String result2 = string2.decompile();
        String expected2 = "\"hello\"";
        assertThat(result2, is(expected2));
       
    }
 
}
