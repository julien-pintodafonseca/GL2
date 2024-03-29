package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a Deca type (builtin or class).
 *
 * @author Equipe GL2
 * @date 2020
 */
public class TypeDefinition extends Definition {

    public TypeDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "type";
    }

    @Override
    public boolean isExpression() {
        return false;
    }
}
