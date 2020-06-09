package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import org.apache.commons.lang.Validate;

import javax.management.ListenerNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class MethodCall extends AbstractExpr {
    AbstractExpr obj;
    AbstractIdentifier meth;
    ListExpr params;

    public MethodCall (AbstractExpr obj, AbstractIdentifier meth, ListExpr params) {
        Validate.notNull(meth);
        Validate.notNull(params);
        this.obj = obj;
        this.meth = meth;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (obj != null) {
            obj.decompile(s);
        }
        s.print('.');
        meth.decompile(s);
        s.print('(');
        params.decompile(s);
        s.print(')');
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (obj != null) {
            obj.prettyPrint(s, prefix, false);
        }
        meth.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (obj != null) {
            obj.iter(f);
        }
        meth.iter(f);
        params.iter(f);
    }
}
