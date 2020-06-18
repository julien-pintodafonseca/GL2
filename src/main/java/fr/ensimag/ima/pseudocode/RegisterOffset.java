package fr.ensimag.ima.pseudocode;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Equipe GL2
 * @date 2020
 */
public class RegisterOffset extends DAddr {
    private final int offset;
    private final Register register;

    public RegisterOffset(int offset, Register register) {
        super();
        this.offset = offset;
        this.register = register;
    }

    public int getOffset() {
        return offset;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return offset + "(" + register + ")";
    }
}
