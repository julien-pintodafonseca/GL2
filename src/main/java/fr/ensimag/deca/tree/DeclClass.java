package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
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
    public SymbolTable.Symbol getName() {
        return className.getName();
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError, DecacFatalError {
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
            throws ContextualError, DecacFatalError {

        ClassDefinition superClassDef = classExtension.getClassDefinition();
        className.getClassDefinition().setNumberOfFields(superClassDef.getNumberOfFields());
        className.getClassDefinition().setNumberOfMethods(superClassDef.getNumberOfMethods());

        fields.verifyListClassMembers(compiler, className.getClassDefinition());
        methods.verifyListClassMembers(compiler, className.getClassDefinition());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError, DecacFatalError {
        // Règle syntaxe contextuelle : (3.5)
        fields.verifyListClassBody(compiler, className.getClassDefinition().getMembers(), className.getClassDefinition());
        methods.verifyListClassBody(compiler, className.getClassDefinition().getMembers(), className.getClassDefinition());
    }

    @Override
    protected void codeGenMethodTable(DecacCompiler compiler) {
        compiler.addComment("Construction de la table des methodes de " + className.getName());

        // Récupération de l'adresse de la classe supérieure
        compiler.addInstruction(new LEA(classExtension.getClassDefinition().getOperand(),Register.R0));

        RegisterOffset classOperand = new RegisterOffset(compiler.getStackManager().getGB(), Register.GB);
        compiler.getStackManager().incrGB();

        compiler.addInstruction(new STORE(Register.R0, classOperand));
        className.getClassDefinition().setOperand(classOperand);

        // Construction de la table des étiquettes et de la table des méthodes
        className.getClassDefinition().initVTable();

        // Ajout de la méthode equals
        for (AbstractDeclMethod method : methods.getList()) {
            MethodDefinition methodDef = (MethodDefinition) className.getClassDefinition().getMembers().get(method.getName());
            methodDef.setLabel(new Label("code." + className.getName() + "." + method.getName()));

            className.getClassDefinition().getVTable().addMethod(method);
        }

        // Ajout des méthodes de la superclasse
        int i = 1;
        while (classExtension.getClassDefinition().getVTable().containKey(i) || className.getClassDefinition().getVTable().containKey(i) ) {
            if (!className.getClassDefinition().getVTable().containKey(i)) {
                className.getClassDefinition().getVTable().addMethod(classExtension.getClassDefinition().getVTable().getMethod(i));
            }

            // génération du code assembleur pour la table des méthodes
            compiler.addInstruction(new LOAD(className.getClassDefinition().getVTable().getMethodDef(i).getLabel(), Register.R0));

            RegisterOffset methodOperand = new RegisterOffset(compiler.getStackManager().getGB(), Register.GB);
            compiler.getStackManager().incrGB();

            compiler.addInstruction(new STORE(Register.R0, methodOperand));
            className.getClassDefinition().getVTable().getMethodDef(i).setOperand(classOperand);
            i++;
        }

    }

    @Override
    protected void codeGenMethodAndFields(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("");
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("                Classe " + className.getName());
        compiler.addComment("--------------------------------------------------");
        fields.codeGenFields(compiler, className.getClassDefinition());
        methods.codeGenMethod(compiler, className.getClassDefinition());
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
