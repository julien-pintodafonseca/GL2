package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Single precision, floating-point literal
 *
 * @author Equipe GL2
 * @date 2020
 */
public class NullLiteral extends AbstractExpr {
    private static Logger LOG = Logger.getLogger(NullLiteral.class);

    public NullLiteral() {

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify expr: start (NullLiteral)");
        // Règles syntaxe contextuelle :(3.38), (3.48)
        Type type = compiler.environmentType.NULL;
        setType(type);
        LOG.debug("verify expr: end (NullLiteral)");
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
            // no child
    }

    @Override
    protected void iterChildren(TreeFunction f) {
            // no child
    }
}
