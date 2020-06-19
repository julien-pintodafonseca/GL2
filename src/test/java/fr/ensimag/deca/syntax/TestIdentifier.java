package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestIdentifier {
    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Test
    public void testGetClassDefinition() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
        ClassDefinition classDef1 = new ClassDefinition(null, null, null);
        FieldDefinition fieldDef1 = new FieldDefinition(null, null, null, null, 1);

        ident1.setDefinition(classDef1);
        assertThat(ident1.getClassDefinition(), is(classDef1));
        assertThat(ident1.getDefinition(), is(classDef1));

        ident1.setDefinition(fieldDef1);

        DecacInternalError expected1 =
                new DecacInternalError("Identifier " + ident1.getName()
                                + " is not a class identifier, you can't call getClassDefinition on it");

        DecacInternalError result1 = assertThrows(DecacInternalError.class, () -> { ident1.getClassDefinition();});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testGetFieldDefinition() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
        ClassDefinition classDef1 = new ClassDefinition(null, null, null);
        FieldDefinition fieldDef1 = new FieldDefinition(null, null, null, null, 1);

        ident1.setDefinition(fieldDef1);
        assertThat(ident1.getFieldDefinition(), is(fieldDef1));
        assertThat(ident1.getDefinition(), is(fieldDef1));

        ident1.setDefinition(classDef1);

        DecacInternalError expected1 =
                new DecacInternalError("Identifier " + ident1.getName()
                        + " is not a field identifier, you can't call getFieldDefinition on it");

        DecacInternalError result1 = assertThrows(DecacInternalError.class, () -> { ident1.getFieldDefinition();});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testGetMethodDefinition() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
        MethodDefinition methodDef1 = new MethodDefinition(null,null, null, 1);
        FieldDefinition fieldDef1 = new FieldDefinition(null, null, null, null, 1);

        ident1.setDefinition(methodDef1);
        assertThat(ident1.getMethodDefinition(), is(methodDef1));
        assertThat(ident1.getDefinition(), is(methodDef1));

        ident1.setDefinition(fieldDef1);

        DecacInternalError expected1 =
                new DecacInternalError("Identifier " + ident1.getName()
                        + " is not a method identifier, you can't call getMethodDefinition on it");

        DecacInternalError result1 = assertThrows(DecacInternalError.class, () -> { ident1.getMethodDefinition();});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testGetVariableDefinition() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
        VariableDefinition varDef1 = new VariableDefinition(null, null);
        FieldDefinition fieldDef1 = new FieldDefinition(null, null, null, null, 1);

        ident1.setDefinition(varDef1);
        assertThat(ident1.getVariableDefinition(), is(varDef1));
        assertThat(ident1.getDefinition(), is(varDef1));

        ident1.setDefinition(fieldDef1);

        DecacInternalError expected1 =
                new DecacInternalError("Identifier " + ident1.getName()
                        + " is not a variable identifier, you can't call getVariableDefinition on it");

        DecacInternalError result1 = assertThrows(DecacInternalError.class, () -> { ident1.getVariableDefinition();});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testGetExpDefinition() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));
        FieldDefinition fieldDef1 = new FieldDefinition(null, null, null, null, 1);
        TypeDefinition typeDef1 = new TypeDefinition(null, null);

        ident1.setDefinition(fieldDef1);
        assertThat(ident1.getExpDefinition(), is(fieldDef1));
        assertThat(ident1.getDefinition(), is(fieldDef1));

        ident1.setDefinition(typeDef1);
        assertThat(ident1.getDefinition(), is(typeDef1));

        DecacInternalError expected1 =
                new DecacInternalError("Identifier " + ident1.getName()
                        + " is not a Exp identifier, you can't call getExpDefinition on it");

        DecacInternalError result1 = assertThrows(DecacInternalError.class, () -> { ident1.getExpDefinition();});

        assertThat(result1.getMessage(), is(expected1.getMessage()));
    }

    @Test
    public void testDecompile() {
        AbstractIdentifier ident1 = new Identifier(compiler.createSymbol("ident1"));

        String result1 = ident1.decompile();
        String expected1 = "ident1";
        assertThat(result1, is(expected1));
    }
}
