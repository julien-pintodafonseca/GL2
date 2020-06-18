package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class VoidType extends Type {

    public VoidType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isVoid();
    }
}
