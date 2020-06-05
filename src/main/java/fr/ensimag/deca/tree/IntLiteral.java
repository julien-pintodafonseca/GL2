package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author Equipe GL2
 * @date 2020
 */
public class IntLiteral extends AbstractExpr {
    private static Logger LOG = Logger.getLogger(IntLiteral.class);
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        LOG.debug("verify expr: start (intLiteral)");
        // Règle syntaxe contextuelle : (3.44)
        Type type = compiler.environmentType.INT;
        setType(type);
        LOG.debug("verify expr: end (intLiteral)");
        return type;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(getValue()), Register.R1));
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(getValue()), register));
    }

    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
