package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType) throws ContextualError {
        LOG.debug("verify expr: start (abstractExpr)");
        // Règle syntaxe contextuelle : (3.28)
        Type rightType = verifyExpr(compiler, localEnv, currentClass);

        // Condition de types
        if (compiler.environmentType.assignCompatible(expectedType, rightType)) {
            if (rightType.sameType(expectedType)) {
                return this;
            } else if (expectedType.isFloat() && rightType.isInt()) {
                ConvFloat conv = new ConvFloat(this);
                conv.setType(expectedType);
                return conv;
            } else {
                Cast cast = new Cast(new Identifier(expectedType.getName()), this);
                cast.setType(expectedType);
                return cast;
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_INCOMPATIBLE_ASSIGN_TYPE + rightType + " ( type attendue : "+ expectedType + " )",getLocation());
        }
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.20)
        LOG.debug("verify inst: start (abstractExpr)");
        verifyExpr(compiler, localEnv, currentClass);
        LOG.debug("verify inst: end (abstractExpr)");
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	  // Règle syntaxe contextuelle : (3.29)
        if (!type.isBoolean()) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE + type + ".", getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) throws DecacFatalError {
        if(getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else if (getType().isFloat()) {
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        } else {
            throw new UnsupportedOperationException("not yet implemented");
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        int i = compiler.getRegisterManager().nextAvailable();
        if (i != -1) {
            compiler.getRegisterManager().take(i);
            codeGenInst(compiler, Register.getR(i));
            compiler.getRegisterManager().free(i);
        } else {
            // chargement dans la pile de 1 registre
            throw new UnsupportedOperationException("no more available registers : policy not yet implemented");
            // restauration dans le registre
        }
    }

    /**
     * Generate assembly code for the instruction and put the result of the instruction in the register.
     *
     * @param compiler
     * @param register
     */
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        // nothing for the program like "hello";
    }

    /**
     * Generate code to verify that a condition is respected
     *
     * @param compiler
     * @param label : label where the program must continue if the condition is not respected
     * @param reverse : if reverse is true, we compare instructions to have (expr1 op expr2) => true
     *                if reverse is false, we compare instructions to have (expr1 op expr2) => false
     *                with op a comparison operator
     * @throws DecacFatalError
     */
    public void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
