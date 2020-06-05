package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        // Règle syntaxe contextuelle : (3.36)
        Type t = compiler.environmentType.FLOAT;
        setType(t);
        return t;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        compiler.addInstruction(new RFLOAT()); // load the read value in the register R1
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new RFLOAT()); // load the read value in the register R1
        compiler.addInstruction(new LOAD(Register.R1, register));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
