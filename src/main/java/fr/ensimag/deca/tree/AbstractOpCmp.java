package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import static fr.ensimag.deca.ErrorMessages.CONTEXTUAL_ERROR_COMPARAISON_INCOMPATIBLE_TYPE;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Syntaxe contextuelle : règle (3.33)
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t1 = getLeftOperand().getType();
        Type t2 = getRightOperand().getType();

        // Syntaxe contextuelle : signature des opérateurs
        if ( this instanceof AbstractOpExactCmp ) {
            Type t = compiler.environmentType.BOOLEAN;
            setType(t);
            return t;
        } else if ( (t1.isInt() || t1.isFloat()) && (t2.isInt() || t2.isFloat()) ) {
            Type t = compiler.environmentType.BOOLEAN;
            setType(t);
            return t;
        } else {
            throw new ContextualError(CONTEXTUAL_ERROR_COMPARAISON_INCOMPATIBLE_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }

    }

}
