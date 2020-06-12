package fr.ensimag.deca.codegen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestStackManager {
    @Test
    public void testGetGB() {
        StackManager sm = new StackManager(new RegisterManager(7));

        // Premier GB disponible
        assertEquals(1, sm.getGB());
    }

    @Test
    public void testGetLB() {
        StackManager sm = new StackManager(new RegisterManager(7));

        // Premier LB disponible
        assertEquals(1, sm.getLB());
    }

    @Test
    public void testIncrGB() {
        StackManager sm = new StackManager(new RegisterManager(7));

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
        StackManager sm = new StackManager(new RegisterManager(7));

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
