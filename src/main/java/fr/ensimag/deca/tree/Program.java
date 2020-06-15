package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError, EnvironmentExp.DoubleDefException {
        // Règle syntaxe contextuelle : (3.1)
        LOG.debug("verify program: start");
        declareObject(compiler);
        getClasses().verifyListClass(compiler); // Passe 1 : vérifie le nom des classes et la hiérarchie de classes
        getClasses().verifyListClassMembers(compiler); // Passe 2: vérifie les champs et la signature des méthodes des différentes classes
        getClasses().verifyListClassBody(compiler); // Passe 3 : vérifie les blocs, les instructions, les expressions et les initialisations
        getMain().verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) throws DecacFatalError {
        compiler.setRegisterManager(compiler.getCompilerOptions().getRegisters());
        compiler.setStackManager(compiler.getRegisterManager());
        compiler.setLabelManager();
        compiler.setErrorLabelManager();
        // TSTO #d
        // BOV pile_pleine

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("       Construction de la table des méthodes      ");
        compiler.addComment("--------------------------------------------------");
        codeGenMethodTableObject(compiler);

        classes.codeGenMethodTable(compiler);
        compiler.addFirst(new ADDSP(main.getNumberDeclVariables()+compiler.getStackManager().getGB()));

        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        // Code des méthodes de la classe Object
        codeGenMethodObject(compiler);

        // Code des méthodes des classes créées par l'utilisateur
        classes.codeGenMethod(compiler);

        compiler.getErrorLabelManager().printErrors(compiler);
    }

    protected void declareObject(DecacCompiler compiler) throws EnvironmentExp.DoubleDefException {
        ClassDefinition objectClassDef = compiler.environmentType.getClassDefinition(compiler.createSymbol("Object"));

        // Définition de la méthode Equals
        int equalIndex = objectClassDef.incNumberOfMethods();
        Type returnType = compiler.environmentType.BOOLEAN;
        Signature sign = new Signature();
        sign.add(compiler.environmentType.OBJECT);
        MethodDefinition equalMethodDef = new MethodDefinition(returnType, Location.BUILTIN, sign, equalIndex);
        objectClassDef.getMembers().declare(compiler.createSymbol("equals"), equalMethodDef);
    }

    protected void codeGenMethodTableObject(DecacCompiler compiler) {
        compiler.addComment("Code de la table des methodes de Object");
        ClassDefinition objectClassDef = compiler.environmentType.getClassDefinition(compiler.createSymbol("Object"));
        objectClassDef.initVTable();

        // Ajout de la classe Object
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        RegisterOffset classOperand = new RegisterOffset(compiler.getStackManager().getGB(), Register.GB);
        compiler.getStackManager().incrGB();
        compiler.addInstruction(new STORE(Register.R0, classOperand));
        objectClassDef.setOperand(classOperand);

        // Ajout de la méthode equals
        MethodDefinition methodDef = (MethodDefinition) objectClassDef.getMembers().get(compiler.createSymbol("equals"));
        methodDef.setLabel(new Label("code.Object.equals"));

        // public boolean equals (Object other) { return this == other; }
        ListInst inst = new ListInst();
        inst.add(new Return(new Equals(new This(false), new Identifier(compiler.createSymbol("other")))));
        MethodBody methodBody = new MethodBody(new ListDeclVar(), inst);
        ListDeclParam params = new ListDeclParam();
        params.add(new DeclParam(new Identifier(compiler.environmentType.OBJECT.getName()), new Identifier(compiler.createSymbol("other"))));
        AbstractIdentifier type = new Identifier(compiler.environmentType.BOOLEAN.getName());
        type.setType(compiler.environmentType.BOOLEAN);
        DeclMethod method = new DeclMethod(type, new Identifier(compiler.createSymbol("equals")), params, methodBody);
        method.getIdentifier().setDefinition(methodDef);

        objectClassDef.getVTable().addMethod(method);

        compiler.addInstruction(new LOAD(methodDef.getLabel(), Register.R0));
        RegisterOffset methodOperand = new RegisterOffset(compiler.getStackManager().getGB(), Register.GB);
        compiler.getStackManager().incrGB();
        compiler.addInstruction(new STORE(Register.R0, methodOperand));
        objectClassDef.setOperand(classOperand);
    }

    protected void codeGenMethodObject(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("");
        compiler.addComment("--------------------------------------------------");
        compiler.addComment("                Classe Object");
        compiler.addComment("--------------------------------------------------");
        compiler.addLabel(new Label("init.Object"));
        compiler.addInstruction(new RTS());

        ClassDefinition objectClassDef = compiler.environmentType.getClassDefinition(compiler.createSymbol("Object"));
        AbstractDeclMethod method = objectClassDef.getVTable().getMethod(1);
        method.codeGenMethod(compiler, objectClassDef);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
