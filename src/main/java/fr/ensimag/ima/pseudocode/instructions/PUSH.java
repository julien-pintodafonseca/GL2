package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class PUSH extends UnaryInstruction {
    public PUSH(Register op1) {
        super(op1);
    }
}
