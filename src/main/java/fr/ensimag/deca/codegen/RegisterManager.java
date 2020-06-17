package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class RegisterManager {
    private Map<Integer, Boolean> registers; // Value is true if the register is used
    private int size; // R0 ... R{size-1}

    public RegisterManager(int n) {
        Validate.isTrue(n >= 4);
        Validate.isTrue(n <= 16);

        size = n;
        registers = new HashMap<>();

        // We don't deal with scratch registers R0 & R1
        for (int i=2; i<=(size-1); i++) {
            registers.put(i, false);
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isTaken(int regNumber) throws DecacFatalError {
        verifyRegNumber(regNumber);
        return registers.get(regNumber);
    }

    public void take(int regNumber) throws DecacFatalError {
        verifyRegNumber(regNumber);
        registers.put(regNumber, true);
    }

    public void free(int regNumber) throws DecacFatalError {
        verifyRegNumber(regNumber);
        registers.put(regNumber, false);
    }

    public int nextAvailable() {
        for (int i=2; i<=(size-1); i++) {
            if (!registers.get(i)) {
                return i;
            }
        }
        return -1; // If -1 is returned, there is no more available register
    }

    private void verifyRegNumber(int regNumber) throws DecacFatalError {
        boolean verifyMinVal = regNumber >= 2; // We don't deal with scratch registers R0 & R1
        boolean verifyMaxVal = regNumber <= (size-1);
        if (!verifyMinVal || !verifyMaxVal) {
            throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER+regNumber);
        }
    }
}
