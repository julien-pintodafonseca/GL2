package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractPrint extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractPrint.class);

    private boolean printHex;
    private ListExpr arguments;
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Règles syntaxe contextuelle : (3.21), (3.26), (3.27), (3.30)
        LOG.debug("verify inst: start (abstractPrint)");
        for (AbstractExpr expr : getArguments().getList()) {
            expr.verifyExpr(compiler, localEnv, currentClass);
            if (!expr.getType().isInt() && !expr.getType().isFloat() && !expr.getType().isString()) {
            	throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_PRINT_INCOMPATIBLE_TYPE + expr.getType(), getLocation());
            	
            }
        }
        LOG.debug("verify inst: end (abstractPrint)");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        compiler.addComment("Instruction print" + getSuffix() + " ligne " + getLocation().getLine());
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, getPrintHex());
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        LOG.debug("decompile print: start");
        s.print("print" + getSuffix());
        if (getPrintHex()) {
            s.print("x(");
        } else {
            s.print("(");
        }
        arguments.decompile(s);
        s.print(");");
        LOG.debug("decompile print: end");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }
}
