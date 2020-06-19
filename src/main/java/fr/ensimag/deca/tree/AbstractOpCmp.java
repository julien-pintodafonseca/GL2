package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

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
            ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règle (3.33)
        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        // Syntaxe contextuelle : signature des opérateurs
        if ((t1.isInt() || t1.isFloat()) && (t2.isInt() || t2.isFloat())) {
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
        } else if (this instanceof AbstractOpExactCmp && (((t1.isClass() || t1.isNull()) && (t2.isClass() || t2.isNull())) || (t1.isBoolean() && t2.isBoolean()))) {
                // TODO
                Type t = compiler.environmentType.BOOLEAN;
                setType(t);
                return t;
        } else {
            throw new ContextualError( ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE + t1 + " (pour " +
                    getLeftOperand().decompile() + ") et " + t2 + " (pour " + getRightOperand().decompile() + ").", getLocation());
        }
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
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
                int k = compiler.getRegisterManager().getSize()-1;
                if (k == i) {
                    k = k-1;
                }
                compiler.addInstruction(new PUSH(Register.getR(k))); // chargement dans la pile de 1 registre
                compiler.getTSTOManager().addCurrent(1);
                getRightOperand().codeGenInst(compiler, Register.getR(k));
                compiler.addInstruction(new CMP(Register.getR(k), Register.getR(i)));
                compiler.addInstruction(new POP(Register.getR(k))); // restauration du registre
                compiler.getTSTOManager().addCurrent(-1);
            }

            compiler.getRegisterManager().free(i);
        } else {
            int j = compiler.getRegisterManager().getSize() -1;
            int k = j-1;
            compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 2 registres
            compiler.addInstruction(new PUSH(Register.getR(k)));
            compiler.getTSTOManager().addCurrent(2);

            getRightOperand().codeGenInst(compiler, Register.getR(k));
            compiler.addInstruction(new CMP(Register.getR(k), Register.getR(j)));

            compiler.addInstruction(new POP(Register.getR(k))); // restauration des registres
            compiler.addInstruction(new POP(Register.getR(j)));
            compiler.getTSTOManager().addCurrent(-2);
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        Label lb = new Label(LabelType.LB_RETURN_TRUE.toString() + compiler.getLabelManager().getLabelValue(LabelType.LB_RETURN_TRUE));
        compiler.getLabelManager().incrLabelValue(LabelType.LB_RETURN_TRUE);
        codeGenCMP(compiler, lb, false);

        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        if(compiler.getLabelManager().getCurrentLabel() != null) {
            compiler.addInstruction(new BRA(compiler.getLabelManager().getCurrentLabel()));
        }
        compiler.addLabel(lb);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
    }
}
