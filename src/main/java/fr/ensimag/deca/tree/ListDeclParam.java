package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListInst.class);

    @Override
    public void decompile(IndentPrintStream s) {
        int i = 0;
        for (AbstractDeclParam param : this.getList()) {
            if (i > 0) {
                s.print(",");
            }
            param.decompile(s);
            i++;
        }
    }
}
