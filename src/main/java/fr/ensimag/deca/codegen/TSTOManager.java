package fr.ensimag.deca.codegen;

/**
 * Calcul of TSTO
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TSTOManager {
    private int current;
    private int max;

    public void resetCurrentAndMax() {
        current = 0;
        max = 0;
    }

    public void addCurrent(int i) {
        current += i;
        if (current > max) {
            max = current;
        }
    }

    public int getMax() { return max; }
}
