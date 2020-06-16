package fr.ensimag.deca.codegen;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class StackManager {
    private RegisterManager registerManager;

    private int GB; // Global Stack
    private int LB; // Local Stack
    private int SP; // Stack Pointer

    public StackManager(RegisterManager _registerManager) {
        registerManager = _registerManager;
        GB = 1 ;
        LB = 1;
        // SP = TODO
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
