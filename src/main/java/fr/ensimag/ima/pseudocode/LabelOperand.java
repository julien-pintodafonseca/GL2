package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

/**
 * Label used as operand
 *
 * @author Equipe GL2
 * @date 2020
 */
public class LabelOperand extends DVal {
    public Label getLabel() {
        return label;
    }

    private Label label;
    
    public LabelOperand(Label label) {
        super();
        Validate.notNull(label);
        this.label = label;
    }

    @Override
    public String toString() {
        return label.toString();
    }

}
