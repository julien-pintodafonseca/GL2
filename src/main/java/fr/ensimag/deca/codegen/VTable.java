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
    private Map<Integer, AbstractDeclMethod> vTable;

    public VTable() {
        vTable = new HashMap<>();
    }

    public void addMethod(AbstractDeclMethod method) {
        this.vTable.put(method.getIdentifier().getMethodDefinition().getIndex(), method);
    }

    public boolean containKey(int i) {
        return vTable.containsKey(i);
    }

    public AbstractDeclMethod getMethod(int i) {
        return vTable.get(i);
    }

    public MethodDefinition getMethodDef(int i) {
        return vTable.get(i).getIdentifier().getMethodDefinition();
    }
}
