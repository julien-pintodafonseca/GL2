package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.While;
import fr.ensimag.ima.pseudocode.DAddr;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestInitialization {
	
    @Mock private AbstractExpr expr;

    @Before
    public void setup() throws ContextualError, DecacFatalError {
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
