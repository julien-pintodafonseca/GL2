package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Syntaxe contextuelle : règles (3.29), (3.33)
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getLeftOperand().verifyCondition(compiler, localEnv, currentClass);
        getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyCondition(compiler, localEnv, currentClass);
        Type t = compiler.environmentType.BOOLEAN;
        setType(t);
        return t;
    }
}
