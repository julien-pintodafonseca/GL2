package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass declClass : getList()) {
            declClass.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    protected void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        // Règle syntaxe contextuelle : (1.2)
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    protected void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        // Règle syntaxe contextuelle : (2.2)
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    protected void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        // Règle syntaxe contextuelle : (3.2)
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    /**
     * Construction of the Virtual Method Table
     *
     * @param compiler
     */
    protected void codeGenMethodTable(DecacCompiler compiler) {
        LOG.debug("codeGen MethodTable for ListDeclClass: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenMethodTable(compiler);
        }
        LOG.debug("verify Method Table for ListDeclClass: end");
    }
}
