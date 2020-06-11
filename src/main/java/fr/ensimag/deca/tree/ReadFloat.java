package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
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
    public void codeGenPrint(DecacCompiler compiler, boolean printHex) throws DecacFatalError {
        compiler.addInstruction(new RFLOAT()); // load the read value in the register R1
        codeGenError(compiler);
        super.codeGenPrint(compiler, printHex);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        compiler.addInstruction(new RFLOAT()); // load the read value in the register R1
        codeGenError(compiler);
        compiler.addInstruction(new LOAD(Register.R1, register));
    }

    @Override
    public void codeGenError(DecacCompiler compiler) throws DecacFatalError {
        // test de l'entrée saisie par l'utilisateur : dépassement ou erreur de syntaxe
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_READFLOAT_BAD_ENTRY);
        compiler.addInstruction(new BOV(new Label("" + compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_READFLOAT_BAD_ENTRY))), "Overflow check for previous operation");
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
