package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.ReadInt;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */

public class TestReadInt {
    
    @Test
    public void testDecompile() {
    	 
    	ReadInt readInt1 = new ReadInt(); 
       
        String result1 = readInt1.decompile();
        String expected1 = "readInt()";
        assertThat(result1, is(expected1));
               
    }
 
}
