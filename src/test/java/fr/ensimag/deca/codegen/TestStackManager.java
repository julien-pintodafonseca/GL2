package fr.ensimag.deca.codegen;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestStackManager {

    @Test
    public void testInClass() {
        StackManager sm = new StackManager();

        sm.setInClass(true);
        assertTrue(sm.getInClass());

        sm.setInClass(true);
        assertTrue(sm.getInClass()); // répéter l'opération ne change rien

        sm.setInClass(false);
        assertFalse(sm.getInClass());

        sm.setInClass(false);
        assertFalse(sm.getInClass()); // répéter l'opération ne change rien

        sm.setInClass(true);
        assertTrue(sm.getInClass());
    }

    @Test
    public void testGetGB() {
        StackManager sm = new StackManager();

        // Premier GB disponible
        assertEquals(1, sm.getGB());
    }

    @Test
    public void testGetLB() {
        StackManager sm = new StackManager();

        // Premier LB disponible
        assertEquals(1, sm.getLB());
    }

    @Test
    public void testIncrGB() {
        StackManager sm = new StackManager();

        // GB augmente de 1 en 1
        assertEquals(1, sm.getGB());
        sm.incrGB();
        assertEquals(2, sm.getGB());
        sm.incrGB();
        assertEquals(3, sm.getGB());
        sm.incrGB();
        assertEquals(4, sm.getGB());

    }

    @Test
    public void testIncrLB() {
        StackManager sm = new StackManager();

        // GB augmente de 1 en 1
        assertEquals(1, sm.getLB());
        sm.incrLB();
        assertEquals(2, sm.getLB());
        sm.incrLB();
        assertEquals(3, sm.getLB());
        sm.incrLB();
        assertEquals(4, sm.getLB());
    }
}
