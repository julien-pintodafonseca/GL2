package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.ima.pseudocode.Label;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TestLabelManager {
    private final DecacFatalError expectedLabelTypeEx =
            new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_LABEL_MANAGER_UNKNOWN_LABEL_TYPE);

    @Test
    public void testLabelType() {
        // Tous les labels nécessaires sont présents dans l'enum "LabelType"
        assertNotNull(LabelType.valueOf("LB_WHILE"));
        assertNotNull(LabelType.valueOf("LB_OR"));
        assertNotNull(LabelType.valueOf("LB_ELSE"));
    }

    @Test
    public void testCurrentLabel() {
        Label lb1 = new Label("test.1");
        Label lb2 = new Label("TEST2");
        Label lb3 = new Label("test3");
        Label lb4 = new Label("t");

        LabelManager lm = new LabelManager();

        lm.setCurrentLabel(lb1);
        assertThat(lm.getCurrentLabel(), is(lb1));
        assertThat(lm.getCurrentLabel(), is(lb1)); // faire un get ne supprime pas la valeur

        lm.setCurrentLabel(lb2);
        assertThat(lm.getCurrentLabel(), is(lb2));

        lm.setCurrentLabel(lb3);
        assertThat(lm.getCurrentLabel(), is(lb3));

        lm.setCurrentLabel(lb4);
        assertThat(lm.getCurrentLabel(), is(lb4));
    }

    @Test
    public void testGetLabelValueAndVerifyLabelType() throws DecacFatalError {
        LabelManager lm = new LabelManager();

        // Les différents labelTypes sont présents dans l'attribut "myLabels"
        for (LabelType lt : LabelType.values()) {
            assertEquals(lm.getLabelValue(lt), Integer.valueOf(0));
        }

        // On vérifie le cas où le labelType est incorrect
        DecacFatalError result = assertThrows(DecacFatalError.class, () -> {lm.getLabelValue(null);});
        assertThat(result.getMessage(), is(expectedLabelTypeEx.getMessage()));
    }

    @Test
    public void testIncrLabelValue() throws DecacFatalError {
        LabelManager lm = new LabelManager();

        // La map "myLabels" est actualisée : on augmente la valeur de 1 en 1
        assertEquals(Integer.valueOf(0), lm.myLabels.get(LabelType.LB_WHILE));
        lm.incrLabelValue(LabelType.LB_WHILE);
        assertEquals(Integer.valueOf(1), lm.myLabels.get(LabelType.LB_WHILE));
        lm.incrLabelValue(LabelType.LB_WHILE);
        assertEquals(Integer.valueOf(2), lm.myLabels.get(LabelType.LB_WHILE));
        lm.incrLabelValue(LabelType.LB_WHILE);
        assertEquals(Integer.valueOf(3), lm.myLabels.get(LabelType.LB_WHILE));
    }
}
