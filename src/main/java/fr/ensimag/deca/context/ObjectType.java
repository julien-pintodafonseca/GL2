package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ObjectType extends ClassType {

    public ObjectType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isObject();
    }
}
