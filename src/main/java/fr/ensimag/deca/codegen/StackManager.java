package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.GPRegister;

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
        initGB();
        initLB();
        // SP = TODO
    }

    public int getGB() {
        return GB;
    }

    public int getLB() {
        return LB;
    }

    public void initGB() {
        GB = 1;
    }

    public void initLB() {
        LB = 1;
    }

    public void incrGB() {
        GB++;
    }

    public void incrLB() {
        LB++;
    }

    public GPRegister getAvailableGBRegister() throws DecacFatalError {
        int size = registerManager.getSize();
        int nextAvailableRegister = registerManager.nextAvailable();
        if (nextAvailableRegister != -1) {
            registerManager.take(nextAvailableRegister);
            return GPRegister.getR(nextAvailableRegister);
        } else {
            return null; // If null is returned, there is no more available register
        }
    }
}
