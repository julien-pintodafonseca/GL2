package fr.ensimag.deca.tools;

/**
 * Internal error of the compiler. Should never happen.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DecacInternalError extends RuntimeException {
    private static final long serialVersionUID = -7489681854632778463L;

    public DecacInternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public DecacInternalError(String message) {
        super(message);
    }
}
