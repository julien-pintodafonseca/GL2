package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing a string.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class ImmediateString extends Operand {
    private String value;

    public ImmediateString(String value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
