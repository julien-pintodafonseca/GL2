package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(DeclVar.class);

    private final AbstractIdentifier type;
    private final AbstractIdentifier varName;
    private final AbstractInitialization initialization;

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
        VariableDefinition defVar = new VariableDefinition(typeVar, getLocation());

        // Condition typeVar != void
        if (typeVar.isVoid()) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLVAR_NULL + typeVar.getName(), getLocation());
        }

        varName.setType(typeVar);
        varName.setDefinition(defVar);

        // Déclaration de variable
        try {
            localEnv.declare(varName.getName(), defVar);
        } catch (Exception e) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLVAR_DUPE + varName.getName(), getLocation());
        }

        initialization.verifyInitialization(compiler, typeVar, localEnv, currentClass);
        LOG.debug("verify declVar: end");
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) throws DecacFatalError {
        int offset = compiler.getStackManager().getGB();
        compiler.getStackManager().incrGB();
        RegisterOffset addr = new RegisterOffset(offset, Register.GB);
        initialization.codeGenInitialization(compiler, addr);
        varName.getVariableDefinition().setOperand(addr);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
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
