package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
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
    private ListDeclClass classes;
    private AbstractMain main;
    
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

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError, EnvironmentExp.DoubleDefException, DecacFatalError {
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
        compiler.setStackManager();
        compiler.setLabelManager();
        compiler.setErrorLabelManager();
        compiler.setTSTOManager();
        compiler.getTSTOManager().resetCurrentAndMax();
        Line lineTSTO = new Line(new TSTO(0));
        compiler.add(lineTSTO); // TSTO #d
        compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_FULL_STACK))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_FULL_STACK);

        // Calcul pour ADDSP
        int addsp = main.getNumberDeclVariables() + 2; // +2 car on compte la méthode equals de la classe Object et le pointeur vers la superclass de Object (null)
        for (AbstractDeclClass classe : classes.getList()) {
            addsp = addsp + compiler.environmentType.getClassDefinition(classe.getName()).getNumberOfMethods() + 1; // +1 car on compte le pointeur vers la superclass
        }
        compiler.addInstruction(new ADDSP(addsp));
        compiler.getTSTOManager().addCurrent(addsp);

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("       Construction de la table des méthodes      ");
        compiler.addComment("--------------------------------------------------");
        codeGenMethodTableObject(compiler);

        classes.codeGenMethodTable(compiler);

        compiler.getStackManager().setInClass(false);
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        lineTSTO.setInstruction(new TSTO(compiler.getTSTOManager().getMax()));

        compiler.getStackManager().setInClass(true);
        // Code des méthodes de la classe Object
        codeGenMethodObject(compiler);

        // Code des méthodes des classes créées par l'utilisateur et de l'initialisation des champs
        classes.codeGenMethodAndFields(compiler);

        compiler.getErrorLabelManager().printErrors(compiler);
    }

    protected void declareObject(DecacCompiler compiler) throws EnvironmentExp.DoubleDefException, DecacFatalError {
        ClassDefinition objectClassDef = compiler.environmentType.getClassDefinition(compiler.createSymbol("Object"));

        // Définition de la méthode Equals
        int equalIndex = objectClassDef.incNumberOfMethods();
        Type returnType = compiler.environmentType.BOOLEAN;
        Signature sign = new Signature();
        sign.add(compiler.environmentType.OBJECT);
        MethodDefinition equalMethodDef = new MethodDefinition(returnType, Location.BUILTIN, sign, equalIndex);
        objectClassDef.getMembers().declare(compiler.createSymbol("equals"), equalMethodDef);
    }

    protected void codeGenMethodTableObject(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("Construction de la table des methodes de Object");
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
        Identifier ident = new Identifier(compiler.createSymbol("other"));
        ident.setDefinition(new ParamDefinition(compiler.environmentType.OBJECT, Location.BUILTIN));
        ident.getExpDefinition().setOperand(new RegisterOffset(-3, Register.LB));
        inst.add(new Return(new Equals(new This(false), ident)));
        MethodBody methodBody = new MethodBody(new ListDeclVar(), inst);
        ListDeclParam params = new ListDeclParam();
        AbstractIdentifier paramName = new Identifier(compiler.createSymbol("other"));
        paramName.setDefinition(new ParamDefinition(compiler.environmentType.OBJECT, Location.BUILTIN));
        params.add(new DeclParam(new Identifier(compiler.environmentType.OBJECT.getName()), paramName));
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
