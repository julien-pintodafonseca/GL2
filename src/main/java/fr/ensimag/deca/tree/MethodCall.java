package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

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
        // Syntaxe contextuelle : règles (3.71), (3.72), (3.73) et (3.74)
        obj.verifyExpr(compiler, localEnv, currentClass);
        if (obj.getType() != null) {
            Type typeClass = obj.getType();
            if (typeClass.isClass()) {
                ClassDefinition classDef = compiler.environmentType.getClassDefinition(typeClass.getName());
                meth.verifyExpr(compiler, classDef.getMembers(), currentClass);
                Signature signMeth = meth.getMethodDefinition().getSignature();
                int paramNumber = 0;
                int numberOfParam = signMeth.size();
                for (AbstractExpr expr : params.getList()) {
                    if (paramNumber < numberOfParam) {
                        expr.verifyRValue(compiler, localEnv, currentClass, signMeth.paramNumber(paramNumber));
                        paramNumber++;
                    } else if (numberOfParam == 0) {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_NO_PARAM_EXPECTED + meth.getName() + ".", getLocation());
                    } else {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_MORE_OR_LESS_PARAM_EXPECTED + numberOfParam + " paramètre(s) au lieu de " + params.getList().size() + " paramètre(s) : " + meth.getName() + ".", getLocation());
                    }
                }
                if (paramNumber != numberOfParam) {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_MORE_OR_LESS_PARAM_EXPECTED + numberOfParam + " paramètre(s) au lieu de " + params.getList().size() + " paramètre(s) : " + meth.getName() + ".", getLocation());
                }

                setType(meth.getType());
                return meth.getType();
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_EXPR_IS_NOT_CLASS + obj.decompile() + " est de type " + obj.getType() + ".", getLocation());
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHODCALL_WITHOUT_CLASS + meth.getName() + ".", getLocation());
        }
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
