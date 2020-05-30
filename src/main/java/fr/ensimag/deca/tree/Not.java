package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
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
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        if(getOperand() instanceof BooleanLiteral) {
            // if the son is a boolean
            int i = compiler.getRegisterManager().nextAvailable();
            if (i != -1) {
                compiler.getRegisterManager().take(i);
                getOperand().codeGenInst(compiler, Register.getR(i));
                compiler.addInstruction(new CMP(1, Register.getR(i)));
                if (reverse) {
                    compiler.addInstruction(new BEQ(label));
                } else {
                    compiler.addInstruction(new BNE(label));
                }
                compiler.getRegisterManager().free(i);
            } else {
                // chargement dans la pile de 1 registre
                throw new UnsupportedOperationException("no more available registers : policy not yet implemented");
                // restauration dans le registre
            }
        } else {
            // else the son is any other AbstractExpr
            if (reverse) {
                getOperand().codeGenCMP(compiler, label, false);
            } else {
                getOperand().codeGenCMP(compiler, label, true);
            }
        }
    }

}
