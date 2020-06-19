package fr.ensimag.deca.codegen;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class StackManager {
    private RegisterManager registerManager;

    private boolean inClass;

    private int GB; // Global Stack
    private int LB; // Local Stack

    public StackManager(RegisterManager _registerManager) {
        registerManager = _registerManager;
        GB = 1 ;
        LB = 1;
    }

    public void setInClass(boolean inClass) {
        this.inClass = inClass;
    }

    public boolean getInClass() {
        return inClass;
    }

    public void resetLB() {
        this.LB = 1;
    }

    public int getGB() {
        return GB;
    }

    public int getLB() {
        return LB;
    }

    public void incrGB() {
        GB++;
    }

    public void incrLB() {
        LB++;
    }
}
