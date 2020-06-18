package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.33)
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t1 = getLeftOperand().getType();
        Type t2 = getRightOperand().getType();

        // Syntaxe contextuelle : signature des opérateurs
        if (t1.isInt() && t2.isInt()) {
            Type t = compiler.environmentType.INT;
            setType(t);
            return t;
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }
    }

    @Override
    public void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) throws DecacFatalError {
        compiler.addInstruction(new REM(register1, register2));
        codeGenError(compiler);
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
