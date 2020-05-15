package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author @AUTHOR@
 * @date @DATE@
 */
public class ListInst extends TreeList<AbstractInst> {
    private static final Logger LOG = Logger.getLogger(ListInst.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.19)
        LOG.debug("verify ListInst: start");
        for (AbstractInst inst : getList()) {
            inst.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        LOG.debug("verify ListInst: end");
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst inst : getList()) {
            inst.codeGenInst(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst inst : getList()) {
            inst.decompileInst(s);
            s.println();
        }
    }
}
