package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.UnaryInstructionImmInt;

/**
 * Add a value to stack pointer.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class ADDSP extends UnaryInstructionImmInt {

    public ADDSP(ImmediateInteger operand) {
        super(operand);
    }

    public ADDSP(int i) {
        super(i);
    }

}
