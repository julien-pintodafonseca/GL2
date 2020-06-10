package fr.ensimag.deca.tools;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example JUnit class. A JUnit class must be named TestXXX or YYYTest. JUnit
 * will run each methods annotated with @Test.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class SymbolTest {
    @Test
    public void testSymbol() {
        SymbolTable t = new SymbolTable();
        Symbol s1 = t.create("foo");
        // Check that two objects are equal (using Object.equals())
        assertEquals(s1.getName(), "foo");
        Symbol s2 = t.create("foo");
        // Check that two objects are the same (same reference)
        assertSame(s1, s2);
        Symbol s3 = t.create("foobar");
        assertNotSame(s1, s3);
    }

    @Test
    public void multipleTables() {
        SymbolTable t1 = new SymbolTable();
        SymbolTable t2 = new SymbolTable();
        Symbol s1 = t1.create("foo");
        Symbol s2 = t2.create("foo");
        assertEquals(s1.getName(), s2.getName());
        assertNotSame(s1, s2);
    }
}
