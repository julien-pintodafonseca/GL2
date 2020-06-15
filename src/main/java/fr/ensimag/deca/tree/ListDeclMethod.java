package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
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
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listClassMethods : start");
        // Règle syntaxe contextuelle : (2.6)
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyClassMembers(compiler, currentClass);
        }
        LOG.debug("verify listClassMethods: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    protected void verifyListClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listClassMethodsBody : start");
        // Règle syntaxe contextuelle : (3.10)
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyClassBody(compiler, localEnv, currentClass);
        }
        LOG.debug("verify listClassMethodsBody : end");
    }

    /**
     * Generate the code of the method
     * @param compiler
     */
    protected void codeGenMethod(DecacCompiler compiler, ClassDefinition currentClass) throws DecacFatalError {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.codeGenMethod(compiler, currentClass);
        }
    }
}
