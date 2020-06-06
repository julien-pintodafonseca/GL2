package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) {
        compiler.addInstruction(new SUB(register1, register2));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
