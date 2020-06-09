package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
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
        this.className = className;
        this.classExtension = classExtension;
        this.fields = fields;
        this.methods = methods;
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
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // Règle syntaxe contextuelle : (1.3)
        if (compiler.environmentType.isDeclare(classExtension.getName())) {
            if (!compiler.environmentType.isDeclare(className.getName())) {
                compiler.environmentType.declareClass(compiler, className.getName().getName(), className.getClassDefinition());
                ClassDefinition superClassDef = compiler.environmentType.getClassDefinition(classExtension.getName());
                ClassType classType = new ClassType(className.getName(), getLocation(), superClassDef);
            } else {
                // erreur à créer
                // class déjà déclarée
            }
        } else {
            // erreur à créer
            // classExtension is not declared
        }

        throw new UnsupportedOperationException("not yet implemented");
        // On vérifie que la superclasse existe bien
        superClass.verifyClass(compiler);
        // On récupère la définition de la superClass dans ce contexte également (nécessaire pour la déclaration du type)
        ClassDefinition superClassDef = compiler.getEnvTypes().getClassDef(superClass.getName());
        ClassType classType = new ClassType(name.getName(), getLocation(), superClassDef);
        this.name.setType(classType);

        ClassDefinition classDef = classType.getDefinition();
        name.setDefinition(classDef);

        // On déclare la class dans l'envRoot
        // Erreur si déjà existante
        try {
            compiler.getEnvTypes().declare(name.getName(), name.getClassDefinition());
        } catch (EnvironmentExp.DoubleDefException $e) {
            throw new ContextualError("Class " + name.getName().getName() + " twice declared.", getLocation());
        }

        // On met en place les définitions
        name.verifyClass(compiler);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
    	  // Règle syntaxe contextuelle : (3.5)
        throw new UnsupportedOperationException("not yet implemented");
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
