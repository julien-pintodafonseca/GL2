package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

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
    public Type verifyClassMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethod) throws ContextualError {
        // Règle syntaxe contextuelle : (2.9)
        Type t = varType.verifyType(compiler);
        varType.setType(t);
        if (t.isVoid()) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_VOID_TYPE_PARAM + varName.getName() + " (classe " + currentClass.getType() + ", méthode "+ currentMethod.methodName.getName() + ").", getLocation());
        } else {
            return t;
        }
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