package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.IfThenElse;
import fr.ensimag.deca.tree.ListInst;
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
public class TestIfThenElse {
    

    @Mock private AbstractExpr condition;
    @Mock private ListInst thenBranch;
    @Mock private ListInst elseBranch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDecompile() {
    	 
    	IfThenElse expr = new IfThenElse(condition, thenBranch, elseBranch); 
    	 
        String result1 = expr.decompile();
        String expected1 = "if () {\n} else {\n}";
        assertThat(result1, is(expected1));
               
    }

   
}
