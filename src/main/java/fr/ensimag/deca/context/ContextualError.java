package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;

/**
 * Exception raised when a contextual error is found.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ContextualError extends LocationException {
    private static final long serialVersionUID = -8122514996569278575L;

    public ContextualError(String message, Location location) {
        super(message, location);
    }
}
