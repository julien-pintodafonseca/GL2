package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
    private static final Logger LOG = Logger.getLogger(ListDeclVar.class);

    @Override
    public void decompile(IndentPrintStream s) {
        LOG.debug("decompile listDeclVar: start");
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.decompile(s);
        }
        LOG.debug("decompile listDeclVar: end");
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */
    protected void verifyListDeclVar(DecacCompiler compiler, EnvironmentExp localEnv,
                                     ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        LOG.debug("verify listDeclVar: start");
        // Règle syntaxe contextuelle : (3.16)
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.verifyDeclVar(compiler, localEnv, currentClass);
        }
        LOG.debug("verify listDeclVar: end");
    }

    protected void codeGenListDeclVar(DecacCompiler compiler) throws DecacFatalError {
        if (getList().size() != 0) {
            compiler.addComment("---------- Declarations des variables :");
        }
        compiler.getStackManager().resetLB();
        for (AbstractDeclVar declVar : getList()) {
            declVar.codeGenDeclVar(compiler);
        }
    }
}
