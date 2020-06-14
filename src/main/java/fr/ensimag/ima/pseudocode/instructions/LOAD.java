package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    public LOAD(Label label, GPRegister register) {
        super(label, register);
    }
}
