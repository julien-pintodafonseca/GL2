package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class DeclClass extends AbstractDeclClass {
    private AbstractIdentifier className;
    private AbstractIdentifier classExtension;
    private ListDeclField fields;
    private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier classExtension, ListDeclField fields, ListDeclMethod methods) {
        Validate.notNull(className);
        Validate.notNull(classExtension);
        this.className = className;
        this.classExtension = classExtension;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // Règles syntaxe contextuelle : (1.3) et (2.3)
        if (compiler.environmentType.isDeclare(classExtension.getName())) {
            if (!compiler.environmentType.isDeclare(className.getName())) {
                ClassDefinition superClassDef = compiler.environmentType.getClassDefinition(classExtension.getName());
                classExtension.setDefinition(superClassDef);
                classExtension.setType(superClassDef.getType());

                ClassType classType = new ClassType(className.getName(), getLocation(), superClassDef);
                className.setType(classType);
                className.setDefinition(classType.getDefinition());
                compiler.environmentType.declareClass(className.getName(), className.getClassDefinition());
            } else {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLCLASS_DUPE + className.getName(), getLocation());
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_UNREGISTRED_CLASS + classExtension.getName(), getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        ClassDefinition superClassDef = classExtension.getClassDefinition();
        className.getClassDefinition().setNumberOfFields(superClassDef.getNumberOfFields());
        className.getClassDefinition().setNumberOfMethods(superClassDef.getNumberOfMethods());

        fields.verifyListClassMembers(compiler, superClassDef.getMembers(), className.getClassDefinition());
        methods.verifyListClassMembers(compiler, superClassDef.getMembers(), className.getClassDefinition());


    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // Règle syntaxe contextuelle : (3.5)
        fields.verifyListClassBody(compiler, className.getClassDefinition().getMembers(), className.getClassDefinition());
        //methods.verifyListClassBody();
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        className.decompile(s);
        s.print(" extends ");
        classExtension.decompile(s);

        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        classExtension.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iterChildren(f);
        classExtension.iterChildren(f);
        fields.iterChildren(f);
        methods.iterChildren(f);
    }

}
