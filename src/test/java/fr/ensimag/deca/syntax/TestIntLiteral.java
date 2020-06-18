package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.IntLiteral;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;


/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestIntLiteral {
    private final List<String> IMACodeGenInstExpectedInt0 = new ArrayList<>();
    private final List<String> IMACodeGenInstExpectedInt = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedInt0 = new ArrayList<>();
    private final List<String> IMACodeGenPrintExpectedInt = new ArrayList<>();

    private DecacCompiler compiler;

    @Before
    public void setup() {
        IMACodeGenInstExpectedInt0.add("LOAD #0, R1");
        IMACodeGenInstExpectedInt.add("LOAD #5, R1");
        IMACodeGenPrintExpectedInt0.add("LOAD #0, R1");
        IMACodeGenPrintExpectedInt0.add("WINT");
        IMACodeGenPrintExpectedInt.add("LOAD #5, R1");
        IMACodeGenPrintExpectedInt.add("WINT");
    }

    @Test
    public void testGetValue() {
        IntLiteral int1 = new IntLiteral(0); // Cas d'une valeur nulle
        IntLiteral int2 = new IntLiteral(5); // Cas d'une valeur quelconque

        // La valeur retournée par la méthode getValue() correspond à la valeur donnée lors de l'initilisation
        assertEquals(0, int1.getValue());
        assertEquals(5, int2.getValue());
    }
    
    @Test
    public void testDecompile() {
    	IntLiteral int1 = new IntLiteral(0); // Cas d'une valeur nulle
        IntLiteral int2 = new IntLiteral(5); // Cas d'une valeur quelconque
         
        String result1 = int1.decompile();
        String expected1 = "0";
        assertThat(result1, is(expected1));
        
        String result2 = int2.decompile();
        String expected2 = "5";
        assertThat(result1, is(expected1));
    }

}
