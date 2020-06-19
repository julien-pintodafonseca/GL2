package fr.ensimag.deca.codegen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestTSTOManager {

    @Test
    public void testResetCurrentAndMax() {
        TSTOManager manager = new TSTOManager();

        manager.resetCurrentAndMax();
        assertEquals(0, manager.getMax());

        manager.addCurrent(5);
        assertEquals(5, manager.getMax());
        manager.resetCurrentAndMax();
        assertEquals(0, manager.getMax());
    }

    @Test
    public void testAddCurrent() {
        TSTOManager manager = new TSTOManager();

        manager.addCurrent(5); // current = 5 ; max = 5
        assertEquals(5, manager.getMax());

        manager.addCurrent(-2); // current = 3 ; max = 5
        assertEquals(5, manager.getMax());

        manager.addCurrent(4); // current = 7 ; max = 7
        assertEquals(7, manager.getMax());

        manager.addCurrent(-1); // current = 6 ; max = 7
        assertEquals(7, manager.getMax());

        manager.addCurrent(0); // current = 6 ; max = 7
        assertEquals(7, manager.getMax());
    }
}
