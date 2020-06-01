package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TestRegisterManager extends TestCase {
    final IllegalArgumentException expectedIllegalArgEx = new IllegalArgumentException("The validated expression is false");
    final DecacFatalError expectedWrongReg8Ex = new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER+8);
    final DecacFatalError expectedWrongReg1Ex = new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER+1);

    @Test
    public void testRegisterManager() {
        // Création du register manager avec des paramètres illégaux
        // Cas avec argument en bordure inférieure
        IllegalArgumentException resultLowerArg = assertThrows(IllegalArgumentException.class, () -> {
            RegisterManager rm = new RegisterManager(2);
        });
        assertThat(resultLowerArg.getMessage(), is(expectedIllegalArgEx.getMessage()));

        // Cas avec argument en bordure supérieure
        IllegalArgumentException resultGreaterArg = assertThrows(IllegalArgumentException.class, () -> {
            RegisterManager rm = new RegisterManager(2);
        });
        assertThat(resultGreaterArg.getMessage(), is(expectedIllegalArgEx.getMessage()));
    }

    @Test
    public void testGetSize() {
        // La taille n précisée lors de l'appel du constructeur correspond à la taille renvoyée par getSize()
        RegisterManager rm1 = new RegisterManager(4); // Valeur minimum permise
        assertEquals(4, rm1.getSize());
        RegisterManager rm2 = new RegisterManager(9); // Valeur quelconque
        assertEquals(9, rm2.getSize());
        RegisterManager rm3 = new RegisterManager(16); // Valeur maximum permise
        assertEquals(16, rm3.getSize());

    }

    @Test
    public void testVerifyRegNumber() {
        RegisterManager rm = new RegisterManager(7);

        // Accès à un registre inexistant x (i.e. x est tel que x < 2 ou x > labelManager.getSize())
        // Cas avec numéro de registre bordure supérieure
        DecacFatalError resultGreaterReg = assertThrows(DecacFatalError.class, () -> {rm.isTaken(8);});
        assertThat(resultGreaterReg.getMessage(), is(expectedWrongReg8Ex.getMessage()));

        // Cas avec numéro de registre bordure inférieure
        DecacFatalError resultLowerReg = assertThrows(DecacFatalError.class, () -> {rm.isTaken(1);});
        assertThat(resultLowerReg.getMessage(), is(expectedWrongReg1Ex.getMessage()));
    }

    @Test
    public void testIsTaken() throws DecacFatalError {
        RegisterManager rm = new RegisterManager(7);

        // Accès à un registre non utilisé
        assertFalse(rm.isTaken(3));

        // Accès à un registre utilisé
        rm.take(5);
        assertTrue(rm.isTaken(5));
    }

    @Test
    public void testTake() throws DecacFatalError {
        RegisterManager rm = new RegisterManager(7);

        // Réservation d'un registre non utilisé
        assertFalse(rm.isTaken(5));
        rm.take(5);
        assertTrue(rm.isTaken(5));

        // Réservation d'un registre déjà utilisé
        rm.take(5);
        assertTrue(rm.isTaken(5));

    }

    @Test
    public void testFree() throws DecacFatalError {
        RegisterManager rm = new RegisterManager(7);

        // Libération d'un registre non utilisé
        assertFalse(rm.isTaken(5));
        rm.free(5);
        assertFalse(rm.isTaken(5));

        // Libération d'un registre utilisé
        rm.take(5);
        assertTrue(rm.isTaken(5));
        rm.free(5);
        assertFalse(rm.isTaken(5));
    }

    @Test
    public void testNextAvailable() throws DecacFatalError {
        RegisterManager rm = new RegisterManager(5);

        // Premier registre disponible
        assertEquals(2, rm.nextAvailable()); // le premier registre disponible (hors registre scratch)

        // Registre disponible après réservation du premier registre
        rm.take(2);
        assertEquals(3, rm.nextAvailable());
        rm.free(2);
        assertEquals(2, rm.nextAvailable());

        // Premier registre disponible après réservation d'un registre quelconque
        rm.take(3);
        assertEquals(2, rm.nextAvailable()); // renvoie le premier registre disponible dans l'ordre croissant

        // Si aucun registre n'est disponible
        rm.take(2);
        rm.take(3);
        rm.take(4);
        assertEquals(-1, rm.nextAvailable());
    }
}
