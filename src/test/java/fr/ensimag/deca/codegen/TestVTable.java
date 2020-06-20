package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.Identifier;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestVTable {
    DecacCompiler compiler = new DecacCompiler(null, null);
    MethodDefinition methDef1 = new MethodDefinition(null, null, null, 1);
    AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
    AbstractDeclMethod meth1 = new DeclMethod(null, ident1, null, null);

    MethodDefinition methDef2 = new MethodDefinition(null, null, null, 2);
    AbstractIdentifier ident2 = new Identifier(compiler.createSymbol("ident1"));
    AbstractDeclMethod meth2 = new DeclMethod(null, ident2, null, null);

    MethodDefinition methDef3 = new MethodDefinition(null, null, null, 3);
    AbstractIdentifier ident3 = new Identifier(compiler.createSymbol("ident1"));
    AbstractDeclMethod meth3 = new DeclMethod(null, ident3, null, null);

    MethodDefinition methDef4 = new MethodDefinition(null, null, null, 4);
    AbstractIdentifier ident4 = new Identifier(compiler.createSymbol("ident1"));
    AbstractDeclMethod meth4 = new DeclMethod(null, ident4, null, null);

    @Test
    public void testGetMethod() {
        ident1.setDefinition(methDef1);
        ident2.setDefinition(methDef2);
        ident3.setDefinition(methDef3);
        ident4.setDefinition(methDef4);

        VTable vt = new VTable();

        vt.addMethod(meth1);
        vt.addMethod(meth2);
        vt.addMethod(meth3);
        vt.addMethod(meth4);

        assertThat(vt.getMethod(3), is(meth3));
        assertThat(vt.getMethod(1), is(meth1));
        assertThat(vt.getMethod(2), is(meth2));
        assertThat(vt.getMethod(4), is(meth4));
    }

    @Test
    public void testGetMethodDef() throws DecacFatalError {
        ident1.setDefinition(methDef1);
        ident2.setDefinition(methDef2);
        ident3.setDefinition(methDef3);
        ident4.setDefinition(methDef4);

        VTable vt = new VTable();

        vt.addMethod(meth1);
        vt.addMethod(meth2);
        vt.addMethod(meth3);
        vt.addMethod(meth4);

        assertThat(vt.getMethodDef(3), is(methDef3));
        assertThat(vt.getMethodDef(1), is(methDef1));
        assertThat(vt.getMethodDef(2), is(methDef2));
        assertThat(vt.getMethodDef(4), is(methDef4));
    }

    @Test
    public void testContainKey() {
        ident1.setDefinition(methDef1);
        ident3.setDefinition(methDef3);
        ident4.setDefinition(methDef4);

        VTable vt = new VTable();

        vt.addMethod(meth1);
        vt.addMethod(meth3);
        vt.addMethod(meth4);

        assertTrue(vt.containKey(1));
        assertFalse(vt.containKey(2));
        assertTrue(vt.containKey(3));
        assertTrue(vt.containKey(4));
        assertFalse(vt.containKey(5));
    }
}
