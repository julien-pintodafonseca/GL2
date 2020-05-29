package fr.ensimag.deca.tree;


import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        int i = 0;
        for (AbstractExpr expr : this.getList()) {
            if (i > 0) {
                s.print(",");
            }
            expr.decompile(s);
            i++;
        }
    }
}
