package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        Label lb = new Label(LabelType.LB_RETURN_TRUE.toString() + compiler.getLabelManager().getLabelValue(LabelType.LB_RETURN_TRUE));
        compiler.getLabelManager().incrLabelValue(LabelType.LB_RETURN_TRUE);
        codeGenCMP(compiler, lb, false);

        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        if (compiler.getLabelManager().getCurrentLabel() != null) {
            compiler.addInstruction(new BRA(compiler.getLabelManager().getCurrentLabel()));
        }
        compiler.addLabel(lb);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
    }

}
