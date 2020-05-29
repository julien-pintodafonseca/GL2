package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Syntaxe contextuelle : règle (3.37)
        getOperand().verifyExpr(compiler, localEnv, currentClass);
        getOperand().verifyCondition(compiler, localEnv, currentClass);
        Type t = compiler.environmentType.BOOLEAN;
        setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
   
    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label) throws DecacFatalError {
    	int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
        	if(getOperand() instanceof BooleanLiteral) {
        		getOperand().codeGenInst(compiler);
        		compiler.addInstruction(new CMP(0,Register.getR(i)));
                compiler.addInstruction(new BNE(label));
        	} else {
        		getOperand().codeGenCMPNot(compiler, label);
        	}
            compiler.getRegisterManager().free(i);
        } else {
            throw new DecacFatalError("not yet implemented");
        }
    }

    @Override
    protected void codeGenCMPNot(DecacCompiler compiler, Label label) throws DecacFatalError {
        // not( not(expr)) => expr
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            if(getOperand() instanceof BooleanLiteral) {
                getOperand().codeGenInst(compiler);
                compiler.addInstruction(new CMP(1,Register.getR(i)));
                compiler.addInstruction(new BNE(label));
            } else {
                getOperand().codeGenCMP(compiler, label);
            }
            compiler.getRegisterManager().free(i);
        } else {
            throw new DecacFatalError("not yet implemented");
        }
    }
}
