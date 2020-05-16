package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class UnaryInstructionImmInt extends UnaryInstruction {

    protected UnaryInstructionImmInt(ImmediateInteger operand) {
        super(operand);
    }

    protected UnaryInstructionImmInt(int i) {
        super(new ImmediateInteger(i));
    }

}
