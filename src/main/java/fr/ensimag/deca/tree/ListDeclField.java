package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    protected void verifyListClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listClassFields : start");
        // Règle syntaxe contextuelle : (2.4)
        for (AbstractDeclField declField : getList()) {
            declField.verifyClassMembers(compiler, currentClass);
        }
        LOG.debug("verify listClassFields: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    protected void verifyListClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.6)
        LOG.debug("verify declFieldInit: start");
        for (AbstractDeclField declField : getList()) {
            declField.verifyClassBody(compiler, localEnv, currentClass);
        }
        LOG.debug("verify declFieldInit : end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()) {
            field.decompile(s);
            s.println();
        }
    }
}
