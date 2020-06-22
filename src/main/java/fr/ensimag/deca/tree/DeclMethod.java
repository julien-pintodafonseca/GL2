package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
    public SymbolTable.Symbol getName() {
        return methodName.getName();
    }

    public AbstractIdentifier getIdentifier() {
        return methodName;
    }

    @Override
    protected void verifyMethodMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Règle syntaxe contextuelle : (2.7)
        Type t = type.verifyType(compiler);
        type.setType(t);

        Signature s = params.verifyListParamMembers(compiler, currentClass,this);
        // on vérifie que, si la méthode est déjà définie dans l'environnement des expressions de la superClass,
        Definition superMethName = currentClass.getSuperClass().getMembers().get(methodName.getName());
        MethodDefinition methDef;
        if (superMethName != null) {

            if (superMethName instanceof MethodDefinition) {
                // les signatures de la méthode et de la méthode héritée sont identiques
                Signature superSignature = ((MethodDefinition) superMethName).getSignature();
                if (!superSignature.equals(s)) {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DIFF_SIGNATURE_REDEFINED_METHOD + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
                }

                // et que le type de retour de la méthode est un sous-type du type de retour de la méthode héritée
                if (!compiler.environmentType.subType(t, superMethName.getType())) {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DIFF_TYPE_REDEFINED_METHOD + methodName.getName() +
                            " (classe " + currentClass.getType() + ") de type " + t.getName() +" au lieu de " + superMethName.getType().getName() + ".", getLocation());
                }

                // Comme on redéfinit une méthode de la superClass, il ne faut définir la méthode en lui donnant l'index de la méthode redéfinie
                methDef = new MethodDefinition(t, getLocation(), s, ((MethodDefinition) superMethName).getIndex());
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_METHOD_OVERRIDING_FIELD + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
            }
        } else {
            currentClass.incNumberOfMethods();
            methDef = new MethodDefinition(t, getLocation(), s, currentClass.getNumberOfMethods());
        }

        methodName.setDefinition(methDef);
        methodName.setType(t);
        try {
            currentClass.getMembers().declare(methodName.getName(), methDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLFIELD_DUPE + methodName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
        }
    }

    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Règle syntaxe contextuelle : (3.11)
        Type returnType = type.getType();
        EnvironmentExp envExpParams = params.verifyListParamBody(compiler);
        envExpParams.setParentEnvironment(localEnv);
        methodBody.verifyMethodBody(compiler, envExpParams, currentClass, returnType);
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler, ClassDefinition currentClass) throws DecacFatalError {
        if (currentClass.getType() == compiler.environmentType.OBJECT) {
            compiler.addComment("---------- Code de la methode " + methodName.getName() + " dans la classe Object");
        } else {
            compiler.addComment("---------- Code de la methode " + methodName.getName() + " dans la classe " + currentClass.getType() + " (ligne " + getLocation().getLine() + ")");
        }
        compiler.addLabel(methodName.getMethodDefinition().getLabel());

        // Set les opérandes pour chaque paramètre
        int i = -3;
        for (AbstractDeclParam param : params.getList()) {
            param.getExpDefinition().setOperand(new RegisterOffset(i, Register.LB));
            i--;
        }

        // Label de fin de la méthode
        Label end = new Label("fin." + currentClass.getType() + "." + methodName.getName());
        compiler.getLabelManager().setCurrentLabel(end);

        methodBody.codeGenMethodBody(compiler, currentClass, methodName.getName(), type.getType()); // Code de la méthode
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
