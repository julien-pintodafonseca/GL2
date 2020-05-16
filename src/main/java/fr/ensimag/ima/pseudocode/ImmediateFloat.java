package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand containing a float value.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }
}
