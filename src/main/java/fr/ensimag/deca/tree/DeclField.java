package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Field declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DeclField extends AbstractDeclField {
    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler, EnvironmentExp superClass, ClassDefinition currentClass) throws ContextualError{
        // Règle syntaxe contextuelle : (2.5)
        Type t = type.verifyType(compiler);
        type.setType(t);
        if (t.isVoid()) {
            //erreur à créer
        } else {
            currentClass.incNumberOfFields();
            FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), visibility, currentClass, currentClass.getNumberOfFields());
            try {
                currentClass.getMembers().declare(varName.getName(), fieldDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                // erreur à créer
            }

        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (visibility == Visibility.PROTECTED) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iterChildren(f);
        varName.iterChildren(f);
        initialization.iterChildren(f);
    }

}
