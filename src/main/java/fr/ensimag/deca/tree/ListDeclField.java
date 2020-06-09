package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.AbstractDeclParam;
import fr.ensimag.deca.tree.TreeList;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()) {
            field.decompile(s);
            s.println();
        }
    }
}