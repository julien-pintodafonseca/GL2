package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListInst.class);

    public Signature verifyListClassMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethod) throws ContextualError {
        LOG.debug("verify listClassParams : start");
        // Règle syntaxe contextuelle : (2.8)
        Signature s=new Signature();
        for (AbstractDeclParam declParam : getList()) {
            s.add(declParam.verifyClassMembers(compiler, currentClass, currentMethod));
        }

        LOG.debug("verify listClassParams : end");
        return s;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        int i = 0;
        for (AbstractDeclParam param : this.getList()) {
            if (i > 0) {
                s.print(",");
            }
            param.decompile(s);
            i++;
        }
    }
}
