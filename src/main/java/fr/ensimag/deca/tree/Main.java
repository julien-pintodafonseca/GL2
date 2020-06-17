package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;

    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // Règles syntaxe contextuelle : (3.4) -> (3.18)
        LOG.debug("verify Main: start");
        EnvironmentExp env = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, env, null);
        if(insts.getList().size() != 0) {
            compiler.addComment("---------- Instructions :");
        }
        insts.verifyListInst(compiler, env, null, compiler.environmentType.VOID);
        LOG.debug("verify Main: end");
    }

    @Override
    public int getNumberDeclVariables() {
        return declVariables.size();
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("");
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("           Code du programme principal            ");
        compiler.addComment("--------------------------------------------------");
        declVariables.codeGenListDeclVar(compiler);
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
