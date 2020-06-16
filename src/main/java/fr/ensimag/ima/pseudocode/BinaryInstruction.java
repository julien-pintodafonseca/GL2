package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Base class for instructions with 2 operands.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class BinaryInstruction extends Instruction {
    private Operand operand1, operand2;

    public Operand getOperand1() {
        return operand1;
    }

    public Operand getOperand2() {
        return operand2;
    }

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand1);
        s.print(", ");
        s.print(operand2);
    }

    protected BinaryInstruction(Operand op1, Operand op2) {
        Validate.notNull(op1);
        Validate.notNull(op2);
        this.operand1 = op1;
        this.operand2 = op2;
    }
}
