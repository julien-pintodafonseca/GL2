package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclParam;
import fr.ensimag.deca.tree.TreeFunction;

import java.io.PrintStream;

/**
 * Parameter declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DeclParam extends AbstractDeclParam {
    AbstractIdentifier varType;
    AbstractIdentifier varName;

    public DeclParam(AbstractIdentifier varType, AbstractIdentifier varName) {
        this.varType = varType;
        this.varName = varName;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        varType.decompile(s);
        s.print(" ");
        varName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varType.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        varType.iterChildren(f);
        varName.iterChildren(f);
    }
}