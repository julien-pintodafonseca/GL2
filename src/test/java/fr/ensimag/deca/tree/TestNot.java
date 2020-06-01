package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestNot extends TestCase {

    @Mock
    BooleanLiteral exprBoolean;

    @Mock
    AbstractExpr expr;

    @Mock
    Label lb;

    private DecacCompiler compiler;
    private DecacCompiler compilerWithoutAvailableRegisters;

    final UnsupportedOperationException expectedNoMoreRegister = new UnsupportedOperationException("no more available registers : policy not yet implemented");

    @Before
    public void setUp() throws ContextualError, DecacFatalError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        compiler.setRegisterManager(5);
        when(expr.verifyExpr(compiler, null, null)).thenReturn(compiler.environmentType.BOOLEAN);
        when(exprBoolean.verifyExpr(compiler, null, null)).thenReturn(compiler.environmentType.BOOLEAN);

        compilerWithoutAvailableRegisters = new DecacCompiler(null, null);
        compilerWithoutAvailableRegisters.setRegisterManager(8);
        int i;
        while ((i = compilerWithoutAvailableRegisters.getRegisterManager().nextAvailable()) != -1) { // on marque tous les registres comme étant utilisés
            compilerWithoutAvailableRegisters.getRegisterManager().take(i);
        }
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        Not not = new Not(expr);

        // Le type renvoyé est bien BOOLEAN
        assertEquals(not.verifyExpr(compiler, null, null), compiler.environmentType.BOOLEAN);

        // Le type de l'expression est bien BOOLEAN
        assertEquals(not.getType(), compiler.environmentType.BOOLEAN);
    }

    @Test // Cas où l'attribut "operand" est de type BooleanLiteral et le paramètre reverse=false
    public void testCodeGenCMPBooleanLiteralReverseFalse() throws DecacFatalError {
        Not notBoolean = new Not(exprBoolean);

        // Appel à la fonction codeGenInst() lorsqu'il reste un registre disponible
        notBoolean.codeGenCMP(compiler, lb, false);
        verify(exprBoolean).codeGenInst(eq(compiler), any(GPRegister.class));

        // Pas de modification des attributs lors de la génération de code
        assertEquals(exprBoolean.getType(), notBoolean.getOperand().getType());
        assertThat(notBoolean.getOperand(), is(exprBoolean));


        // Levée d'une erreur si plus de registre disponible
        UnsupportedOperationException resultNoMoreRegister = assertThrows(UnsupportedOperationException.class, () -> {
            notBoolean.codeGenCMP(compilerWithoutAvailableRegisters, lb, false); // Appel avec "reverse" = false
        });
        assertThat(resultNoMoreRegister.getMessage(), is(expectedNoMoreRegister.getMessage()));
    }

    @Test // Cas où l'attribut "operand" est de type BooleanLiteral et le paramètre reverse=true
    public void testCodeGenCMPBooleanLiteralReverseTrue() throws DecacFatalError {
        Not notBoolean = new Not(exprBoolean);

        // Appel à la fonction codeGenInst() lorsqu'il reste un registre disponible
        notBoolean.codeGenCMP(compiler, lb, true);
        verify(exprBoolean).codeGenInst(eq(compiler), any(GPRegister.class));

        // Pas de modification des attributs lors de la génération de code
        assertEquals(exprBoolean.getType(), notBoolean.getOperand().getType());
        assertThat(notBoolean.getOperand(), is(exprBoolean));


        // Levée d'une erreur si plus de registre disponible
        UnsupportedOperationException resultNoMoreRegister = assertThrows(UnsupportedOperationException.class, () -> {
            notBoolean.codeGenCMP(compilerWithoutAvailableRegisters, lb, true); // Appel avec "reverse" = true
        });
        assertThat(resultNoMoreRegister.getMessage(), is(expectedNoMoreRegister.getMessage()));
    }

    @Test
    public void testCodeGenCMPAbstractExpr() throws DecacFatalError { // Cas où l'attribut "operand" est de type AbstractExpr
        Not not = new Not(expr);

        // Les appels récursifs à la méthode codeGenCMP() se font en donnant la valeur inverse pour l'argument "reverse"
        not.codeGenCMP(compiler, lb, false); // i.e. si on appelle codeGenCMP avec reverse=false
        verify(expr).codeGenCMP(compiler, lb, true); // alors la fonction rappelle la même méthode avec reverse=true

        not.codeGenCMP(compiler, lb, true);
        verify(expr).codeGenCMP(compiler, lb, false);

        // Pas de modification des attributs lors de la génération de code
        assertEquals(expr.getType(), not.getOperand().getType());
        assertThat(not.getOperand(), is(expr));
    }
}