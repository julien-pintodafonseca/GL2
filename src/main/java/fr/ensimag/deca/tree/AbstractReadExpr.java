package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;

/**
 * read...() statement.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    protected abstract void codeGenError(DecacCompiler compiler) throws DecacFatalError;
}
