package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tree.AbstractDeclMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Virtual Method Table.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class VTable {
    private Map<Integer, MethodDefinition> vTable;

    public VTable() {
        vTable = new HashMap<>();
    }

    public void addMethod(MethodDefinition methodDef) {
        this.vTable.put(methodDef.getIndex(), methodDef);
    }

    public boolean containKey(int i) {
        return vTable.containsKey(i);
    }

    public MethodDefinition getMethodDef(int i) {
        return vTable.get(i);
    }

}