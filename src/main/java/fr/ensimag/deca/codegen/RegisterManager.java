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
    private Map<Integer, Boolean> registers;
    private int size; // R0 ... R{size-1}

    public RegisterManager(int n) {
        Validate.isTrue(n >= 4);
        Validate.isTrue(n <= 16);

        size = n;
        registers = new HashMap<>();

        for(int i=1; i<=(size-1); i++) { // gestion temporaire du registre (i=0 normalement)
            registers.put(i, false);
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isAvailable(int regNumber) throws DecacFatalError {
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
        for(int i=1; i<=(size-1); i++) { // gestion temporaire du registre (i=0 normalement)
            if (!registers.get(i)) {
                return i;
            }
        }
        return -1; // If -1 is returned, there is no more available register
    }

    private void verifyRegNumber(int regNumber) throws DecacFatalError {
        boolean verifyMinVal = regNumber >= 1; // gestion temporaire du registre (i=0 normalement)
        boolean verifyMaxVal = regNumber <= (size-1);
        if (!verifyMinVal || !verifyMaxVal) {
            throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER+regNumber);
        }
    }

}
