package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.ReadInt;
import fr.ensimag.deca.tree.While;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static fr.ensimag.deca.utils.Utils.normalizeDisplay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
// TODO
public class TestWhile {

    @Mock private AbstractExpr condition;
    @Mock private ListInst body;

    

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        
    }
    @Test
    public void testDecompile() {
    	 
    	While expr = new While(condition, body); 
    	 
        String result1 = expr.decompile();
        String expected1 = "while () {"+"\n"+"}";
        assertThat(result1, is(expected1));
               
    }

   
}
