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
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.decompile(s);
            s.println();
        }
    }

    public void verifyListClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listClassMethods : start");
        // Règle syntaxe contextuelle : (2.6)
        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.verifyClassMembers(compiler, currentClass);
        }
        LOG.debug("verify listClassMethods: end");
    }

}
