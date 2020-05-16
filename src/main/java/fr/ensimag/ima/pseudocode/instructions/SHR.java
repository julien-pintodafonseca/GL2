package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class SHR extends UnaryInstructionToReg {
    public SHR(GPRegister op1) {
        super(op1);
    }
}
