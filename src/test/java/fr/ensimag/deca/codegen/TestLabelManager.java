package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import junit.framework.TestCase;
import org.junit.Test;

public class TestLabelManager extends TestCase {

    @Test
    public void testGetLabelValue() throws DecacFatalError {
        LabelManager lm = new LabelManager();
        // Tous les labels nécessaires sont présents dans l'enum "LabelType"
        assertNotNull(LabelType.valueOf("LB_WHILE"));
        assertNotNull(LabelType.valueOf("LB_OR"));
        assertNotNull(LabelType.valueOf("LB_ELSE"));

        // Les différents label types sont présents dans la map "myLabels"
        for (LabelType lt : LabelType.values()) {
            assertNotNull(lm.getLabelValue(lt));
        }

        // La demande d'un label qui ne fait pas parti de l'enum "LabelType" renvoie une erreur.
        // => Ne peut pas être testé car un type enum contient un nombre fini d'élément.
        //    Or, on a vérifié que tous les éléments faisaient parti de la liste "myLabels".

    }

    @Test
    public static void testIncrLabelValue() throws DecacFatalError {
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
