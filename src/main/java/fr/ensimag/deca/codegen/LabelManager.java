package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.ima.pseudocode.Label;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class LabelManager {
    public Map<LabelType, Integer> myLabels;
    private Label currentLabel; // label utilisé pour les instructions return

    public LabelManager() {
        myLabels = new HashMap<>();
        for (LabelType lt : LabelType.values()) {
            myLabels.put(lt, 0);
        }
    }

    public void setCurrentLabel(Label currentLabel) {
        this.currentLabel = currentLabel;
    }

    public Label getCurrentLabel() {
        return this.currentLabel;
    }

    public Integer getLabelValue(LabelType lt) throws DecacFatalError {
        verifyLabelType(lt);
        return myLabels.get(lt);
    }

    public void incrLabelValue(LabelType lt) throws DecacFatalError {
        verifyLabelType(lt);
        int v = myLabels.get(lt);
        myLabels.put(lt, v+1);
    }

    private void verifyLabelType(LabelType lt) throws DecacFatalError {
        if (myLabels.get(lt) == null) {
            throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_LABEL_MANAGER_UNKNOWN_LABEL_TYPE);
        }
    }
}
