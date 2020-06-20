package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.ReadFloat;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestReadFloat {

	@Test
    public void testDecompile() {
    	 
		ReadFloat readfloat1 = new ReadFloat(); 
       
        String result1 = readfloat1.decompile();
        String expected1 = "readFloat()";
        assertThat(result1, is(expected1));
    }
}
