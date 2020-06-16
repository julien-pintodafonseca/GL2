package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Instruction without operand.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class NullaryInstruction extends Instruction {

    @Override
    void displayOperands(PrintStream s) {
        // no operand
    }
}
