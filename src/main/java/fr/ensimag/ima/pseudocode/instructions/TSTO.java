package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.UnaryInstructionImmInt;

/**
 * @author Equipe GL2
 * @date 2020
 */
public class TSTO extends UnaryInstructionImmInt {
    public TSTO(ImmediateInteger i) {
        super(i);
    }

    public TSTO(int i) {
        super(i);
    }
}
