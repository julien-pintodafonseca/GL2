package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Unary expression.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {
    private AbstractExpr operand;

    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public AbstractExpr getOperand() {
        return operand;
    }

    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        operand.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }
}
