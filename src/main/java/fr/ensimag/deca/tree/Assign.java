package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
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
        AbstractExpr leftOp = getLeftOperand();
        if (leftOp instanceof Identifier) {
            // gestion de la règle LValue -> Identifier
            ExpDefinition varDef = ((Identifier) leftOp).getExpDefinition();
            getRightOperand().codeGenInst(compiler, register);
            compiler.addInstruction(new STORE(register, varDef.getOperand()));
        } else {
            // gestion de la règle LValue -> Selection[expr identifier]
            throw new UnsupportedOperationException("rule LValue -> Selection[expr identifier] in Assign : not yet implemented");
        }
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
