package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class Initialization extends AbstractInitialization {
    private static final Logger LOG = Logger.getLogger(Initialization.class);

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.8)
        LOG.debug("verify initialization: start");
        AbstractExpr rightExpr = getExpression().verifyRValue(compiler, localEnv, currentClass, t);
        setExpression(rightExpr);
        LOG.debug("verify initialization: end");
    }

    @Override
    protected void codeGenInitialization(DecacCompiler compiler, DAddr addr) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            getExpression().codeGenInst(compiler, Register.getR(i));
            compiler.addInstruction(new STORE(Register.getR(i), addr));
            compiler.getRegisterManager().free(i);
        } else {
            // chargement dans la pile de 1 registres
            throw new UnsupportedOperationException("no more available registers : policy not yet implemented");
            // restauration dans le registre
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
