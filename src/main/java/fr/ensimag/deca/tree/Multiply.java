package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) throws DecacFatalError {
        compiler.addInstruction(new MUL(register1, register2));
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()) {
            codeGenError(compiler);
        }
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}
