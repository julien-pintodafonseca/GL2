package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
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
    public void verifyClassMembers(DecacCompiler compiler, EnvironmentExp superClass, ClassDefinition currentClass) throws ContextualError {
        // Règle syntaxe contextuelle : (2.7)
        Type t = type.verifyType(compiler);
        type.setType(t);

        Signature s = params.verifyListClassMembers(compiler, currentClass,this);
            // on vérifie que, si la méthode est déjà définie dans l'environnement des expressions de la superClass,
            Definition superMethName = superClass.get(methodName.getName());
            if (superMethName != null) {

                if (superMethName instanceof MethodDefinition) {
                    // les signatures de la méthode et de la méthode héritée sont identiques
                    Signature superSignature = ((MethodDefinition) superMethName).getSignature();
                    if(!superSignature.equals(s)) {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DIFF_SIGNATURE_REDEFINED_METHOD + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
                    }

                    // et que le type de retour de la méthode est un sous-type du type de retour de la méthode héritée
                    if (!compiler.environmentType.subType(t, superMethName.getType())) {
                        throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DIFF_TYPE_REDEFINED_METHOD + methodName.getName() +
                                " (classe " + currentClass.getType() + ") de type " + t.getName() +" au lieu de " + superMethName.getType().getName() + ".", getLocation());
                    }
                } else {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHOD_OVERRIDING_FIELD + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
                }
            }
            currentClass.incNumberOfFields();

            MethodDefinition methDef = new MethodDefinition(t, getLocation(), s, currentClass.getNumberOfFields());

            try {
                currentClass.getMembers().declare(methodName.getName(), methDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLFIELD_DUPE + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
            }

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
