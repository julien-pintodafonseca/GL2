package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) {
        compiler.addInstruction(new ADD(register1, register2));
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
}
