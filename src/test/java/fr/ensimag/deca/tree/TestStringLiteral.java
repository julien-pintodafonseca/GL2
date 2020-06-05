package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import junit.framework.TestCase;
import org.junit.Test;

public class TestStringLiteral extends TestCase {

    private DecacCompiler compiler = new DecacCompiler(null, null);

    @Test
    public void testVerifyExpr() {
        StringLiteral str1 = new StringLiteral(""); // Cas d'une chaine vide
        StringLiteral str2 = new StringLiteral("hello"); // Cas d'une chaine quelconque

        // Le type renvoyé est bien STRING
        assertEquals(compiler.environmentType.STRING, str1.verifyExpr(compiler, null, null));
        assertEquals(compiler.environmentType.STRING, str2.verifyExpr(compiler, null, null));

        // Le type de l'expression est bien STRING
        assertEquals(compiler.environmentType.STRING, str1.getType());
        assertEquals(compiler.environmentType.STRING, str2.getType());
    }

    @Test
    public void testCodeGenPrint() {
        // Cas d'une chaine vide
        StringLiteral str1 = new StringLiteral("");
        str1.verifyExpr(compiler, null, null);
        StringLiteral str1CodeGenPrint = new StringLiteral("");
        str1CodeGenPrint.verifyExpr(compiler, null, null);

        // Cas d'une chaine quelconque
        StringLiteral str2 = new StringLiteral("hello");
        str2.verifyExpr(compiler, null, null);
        StringLiteral str2CodeGenPrint = new StringLiteral("hello");
        str2CodeGenPrint.verifyExpr(compiler, null, null);

        // Pas de modification des attributs lors de la génération de code
        str1CodeGenPrint.codeGenPrint(compiler, false);
        assertEquals(str1.getValue(), str1CodeGenPrint.getValue());
        assertEquals(str1.getType(), str1CodeGenPrint.getType());

        str2CodeGenPrint.codeGenPrint(compiler, true);
        assertEquals(str2.getValue(), str2CodeGenPrint.getValue());
        assertEquals(str2.getType(), str2CodeGenPrint.getType());
    }
}
