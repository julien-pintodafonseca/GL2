package fr.ensimag.deca.tree;

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
        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // Syntaxe contextuelle : signature des opérateurs
        if (this instanceof AbstractOpExactCmp) {
            Type t = compiler.environmentType.BOOLEAN;
            setType(t);
            return t;
        } else if ((t1.isInt() || t1.isFloat()) && (t2.isInt() || t2.isFloat())) {
            if (t1.isInt() && t2.isFloat()) {
                ConvFloat conv = new ConvFloat(getLeftOperand());
                conv.setType(t2);
                setLeftOperand(conv);
            } else if (t1.isFloat() && t2.isInt()) {
                ConvFloat conv = new ConvFloat(getRightOperand());
                conv.setType(t1);
                setRightOperand(conv);
            }
            Type t = compiler.environmentType.BOOLEAN;
            setType(t);
            return t;
        } else {
            throw new ContextualError( ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }
    }

    @Override
    public void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            getLeftOperand().codeGenInst(compiler, Register.getR(i));

            int j = compiler.getRegisterManager().nextAvailable();
            if (j != -1) {
                compiler.getRegisterManager().take(j);
                getRightOperand().codeGenInst(compiler, Register.getR(j));
                compiler.addInstruction(new CMP(Register.getR(j), Register.getR(i)));
                compiler.getRegisterManager().free(j);
            } else {
                // chargement dans la pile de 1 registre
                throw new UnsupportedOperationException("no more available registers : policy not yet implemented");
                // restauration dans le registre
            }

            compiler.getRegisterManager().free(i);
        } else {
            // chargement dans la pile de 2 registres
            throw new UnsupportedOperationException("no more available registers : policy not yet implemented");
            // restauration dans les registres
        }
    }
    
}
