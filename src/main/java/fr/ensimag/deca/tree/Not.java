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
    public void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        if(getOperand() instanceof BooleanLiteral) {
            // Si l'attribut "operand" est un booleanLiteral
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
            // sinon si l'attribut "operand" est un autre objet de type AbstractExpr
            if (reverse) {
                getOperand().codeGenCMP(compiler, label, false);
            } else {
                getOperand().codeGenCMP(compiler, label, true);
            }
        }
    }

}
