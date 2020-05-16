package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class INT extends BinaryInstructionDValToReg {

    public INT(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

}
