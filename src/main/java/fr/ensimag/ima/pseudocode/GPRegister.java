package fr.ensimag.ima.pseudocode;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class GPRegister extends Register {
    private int number;

    public GPRegister(String name, int number) {
        super(name);
        this.number = number;
    }

    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }
}
