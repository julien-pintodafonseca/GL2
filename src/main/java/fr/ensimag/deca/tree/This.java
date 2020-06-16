package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class This extends AbstractExpr {
    private boolean impl;

    public This(boolean impl) {
        this.impl = impl;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Syntaxe contextuelle : règle (3.43)
        if (currentClass != null) {
            Type t = currentClass.getType();
            setType(t);
            return t;
        } else if (!impl) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_BAD_USED_THIS, getLocation());
        } else {
            return null;
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (!impl) {
            s.print("this");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
            //no child
    }

    @Override
    protected void iterChildren(TreeFunction f) {
            // no child
    }
}
