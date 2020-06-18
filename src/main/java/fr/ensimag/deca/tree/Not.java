package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
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
                compiler.getRegisterManager().free(i);
            } else {
                int j = compiler.getRegisterManager().getSize() -1 ;
                compiler.addInstruction(new PUSH(Register.getR(j))); // chargement dans la pile de 1 registre
                compiler.getTSTOManager().addCurrent(1);
                getOperand().codeGenInst(compiler, Register.getR(j));
                compiler.addInstruction(new CMP(1, Register.getR(j)));
                compiler.addInstruction(new POP(Register.getR(j))); // restauration du registre
                compiler.getTSTOManager().addCurrent(-1);
            }
            if (reverse) {
                compiler.addInstruction(new BEQ(label));
            } else {
                compiler.addInstruction(new BNE(label));
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
