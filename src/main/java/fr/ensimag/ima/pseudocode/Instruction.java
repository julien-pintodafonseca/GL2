package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class Instruction {

    String getName() {
        return this.getClass().getSimpleName();
    }

    abstract void displayOperands(PrintStream s);

    void display(PrintStream s) {
        s.print(getName());
        displayOperands(s);
    }
}
