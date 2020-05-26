package fr.ensimag.deca.tree;

import fr.ensimag.deca.CLIException;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

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
            throw new ContextualError( ErrorMessages.CONTEXTUAL_ERROR_COMPARAISON_INCOMPATIBLE_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }

    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label) throws DecacFatalError { // exp == expr
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            getLeftOperand().codeGenInst(compiler);
            int j = compiler.getRegisterManager().nextAvailable();
            if (j != -1) {
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new CMP(Register.getR(j), Register.getR(i)));
                compiler.getRegisterManager().free(j);
            } else {
                throw new DecacFatalError("not yet implemented");
            }
            compiler.getRegisterManager().free(i);
        } else {
            throw new DecacFatalError("not yet implemented");
        }
    }
}
