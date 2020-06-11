package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInstArith(DecacCompiler compiler, GPRegister register1, GPRegister register2) throws DecacFatalError {
        if (getType().isInt()) {
            compiler.addInstruction(new QUO(register1, register2));
        } else {
            compiler.addInstruction(new DIV(register1, register2));
        }
        codeGenError(compiler);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
