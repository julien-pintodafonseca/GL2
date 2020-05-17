package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.Register;
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
        // Règle syntaxe contextuelle : (3.32)
        Type leftType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr rightExpr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        setRightOperand(rightExpr);
        setType(leftType);
        LOG.debug("verify expr: end (assign)");
        return leftType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        AbstractExpr leftOp = getLeftOperand();
        if (leftOp instanceof Identifier) {
            VariableDefinition varDef = ((Identifier) leftOp).getVariableDefinition();
            getRightOperand().codeGenInst(compiler);
            compiler.addInstruction(new STORE(Register.R1, varDef.getOperand()));
        } else {
            super.codeGenInst(compiler);
        }

    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
