package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Instruction with a single operand.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class UnaryInstruction extends Instruction {
    private Operand operand;

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand);
    }

    protected UnaryInstruction(Operand operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public Operand getOperand() {
        return operand;
    }
}
