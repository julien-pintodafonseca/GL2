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
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
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

    private AbstractExpr expression;

    public AbstractExpr getExpression() {
        return expression;
    }

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    public void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv,
                                     ClassDefinition currentClass) throws ContextualError, DecacFatalError {
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
            int j = compiler.getRegisterManager().getSize() -1;
            compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 1 registre
            compiler.getTSTOManager().addCurrent(1);
            getExpression().codeGenInst(compiler, Register.getR(j));
            compiler.addInstruction(new STORE(Register.getR(j), addr));
            compiler.addInstruction(new POP(Register.getR(j))); // restauration du registre
            compiler.getTSTOManager().addCurrent(-1);
        }
    }

    @Override
    protected void codeGenInitializationField(DecacCompiler compiler, DAddr addr) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            getExpression().codeGenInst(compiler, Register.getR(i));
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1)); // R1 contient l'adresse de l'objet
            compiler.addInstruction(new STORE(Register.getR(i), addr));
            compiler.getRegisterManager().free(i);
        } else {
            int j = compiler.getRegisterManager().getSize() -1;
            compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 1 registre
            compiler.getTSTOManager().addCurrent(1);
            getExpression().codeGenInst(compiler, Register.getR(j));
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1)); // R1 contient l'adresse de l'objet
            compiler.addInstruction(new STORE(Register.getR(j), addr));
            compiler.addInstruction(new POP(Register.getR(j))); // restauration du registre
            compiler.getTSTOManager().addCurrent(-1);
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
