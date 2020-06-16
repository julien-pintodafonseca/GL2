package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class StringType extends Type {

    public StringType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isString();
    }
}
