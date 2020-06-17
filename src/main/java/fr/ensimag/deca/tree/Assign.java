package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Assign.class);

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify expr: start (assign)");
        // Règles syntaxe contextuelle : (3.32), (3.34)
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr rightExpr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        setRightOperand(rightExpr);
        setType(leftType);
        LOG.debug("verify expr: end (assign)");
        return leftType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        getRightOperand().codeGenInst(compiler, register);
        compiler.addInstruction(new STORE(register, getLeftOperand().codeGenOperandAssign(compiler)));
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }
}
