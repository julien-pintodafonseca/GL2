package fr.ensimag.deca.tree;


import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 * Method declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DeclMethod extends AbstractDeclMethod {
    AbstractIdentifier type;
    AbstractIdentifier methodName;
    ListDeclParam params;
    AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params, AbstractMethodBody methodBody) {
        this.type = type;
        this.methodName = methodName;
        this.params = params;
        this.methodBody = methodBody;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.println(") ");
        methodBody.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        params.iter(f);
        methodBody.iter(f);
    }
}
