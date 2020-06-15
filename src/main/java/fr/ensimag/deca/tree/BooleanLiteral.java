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
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class BooleanLiteral extends AbstractExpr {
    private static Logger LOG = Logger.getLogger(BooleanLiteral.class);

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify expr: start (booleanLiteral)");
        // Règles syntaxe contextuelle : (3.38), (3.47)
        Type type = compiler.environmentType.BOOLEAN;
        setType(type);
        LOG.debug("verify expr: end (booleanLiteral)");
        return type;
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) {
    	if (getValue()) {
    		compiler.addInstruction(new LOAD(new ImmediateInteger(1), register));
    	} else {
    		compiler.addInstruction(new LOAD(new ImmediateInteger(0), register));
    	}
    	
    }
    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
    	if (!getValue() && reverse) {
    		compiler.addInstruction(new BRA(label));
    	} else if (getValue() && !reverse) {
    		compiler.addInstruction(new BRA(label));
    	}
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }
}
