package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;

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
            ClassDefinition currentClass) throws ContextualError, DecacFatalError {
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
        if (getOperand() instanceof BooleanLiteral) {
            // Si l'attribut "operand" est un booleanLiteral
            getOperand().codeGenInst(compiler, Register.R0);
            compiler.addInstruction(new CMP(1, Register.R0));
            if (reverse) {
                compiler.addInstruction(new BEQ(label));
            } else {
                compiler.addInstruction(new BNE(label));
            }
        } else {
            // sinon si l'attribut "operand" est un autre objet de type AbstractExpr de type booléen
            if (reverse) {
                getOperand().codeGenCMP(compiler, label, false);
            } else {
                getOperand().codeGenCMP(compiler, label, true);
            }
        }
    }
}
