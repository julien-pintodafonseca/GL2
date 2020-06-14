package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("           Building of the method table           ");
        compiler.addComment("--------------------------------------------------");
        codeGenMethodTableObject(compiler);

        classes.codeGenMethodTable(compiler);
        main.codeGenMain(compiler);
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
        compiler.addComment("Code of the method table of Object");
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
        objectClassDef.getVTable().addMethod(methodDef);

        compiler.addInstruction(new LOAD(methodDef.getLabel(), Register.R0));
        RegisterOffset methodOperand = new RegisterOffset(compiler.getStackManager().getGB(), Register.GB);
        compiler.getStackManager().incrGB();
        compiler.addInstruction(new STORE(Register.R0, methodOperand));
        objectClassDef.setOperand(classOperand);
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
