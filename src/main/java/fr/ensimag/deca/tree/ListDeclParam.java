package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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

    public Signature verifyListParamMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethod) throws ContextualError {
        LOG.debug("verify listClassParams : start");
        // Règle syntaxe contextuelle : (2.8)
        Signature s=new Signature();
        for (AbstractDeclParam declParam : getList()) {
            s.add(declParam.verifyParamMembers(compiler, currentClass, currentMethod));
        }

        LOG.debug("verify listClassParams : end");
        return s;
    }

    public EnvironmentExp verifyListParamBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassParams : start");
        // Règle syntaxe contextuelle : (3.12)
        EnvironmentExp envExpParams = new EnvironmentExp(null);
        for (AbstractDeclParam declParam : getList()) {
            declParam.verifyParamBody(compiler, envExpParams);
        }

        LOG.debug("verify listClassParams : end");
        return envExpParams;
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
