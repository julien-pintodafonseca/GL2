package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * @author @AUTHOR@
 * @date @DATE@
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(DeclVar.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.17)
        LOG.debug("verify declVar: start");
        Type typeVar = type.verifyType(compiler);
        type.setDefinition(new TypeDefinition(typeVar, getLocation()));

        // Condition typeVar != void
        if (typeVar.isVoid()) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLVAR_NULL, getLocation());
        }

        //initialization.verifyInitialization(compiler, typeVar, localEnv, currentClass);

        // Déclaration de variable
        try {
            localEnv.declare(varName.getName(), new VariableDefinition(typeVar, getLocation()));
        } catch (Exception e) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLVAR_DUPE, getLocation());
        }

        //varName.verifyExpr(compiler, localEnv, currentClass);
        varName.setType(typeVar);
        //varName.setDefinition();
        LOG.debug("verify declVar: end");
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
