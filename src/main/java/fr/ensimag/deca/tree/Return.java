package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Return extends AbstractInst {

    private AbstractExpr argument;

    public AbstractExpr getArgument() {
        return argument;
    }

    public Return(AbstractExpr argument) {
        Validate.notNull(argument);
        this.argument = argument;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        // Règle syntaxe contextuelle : (3.24) -> (3.28)
        if (!returnType.isVoid()) {
            argument.verifyExpr(compiler, localEnv, currentClass);
            if (!compiler.environmentType.assignCompatible(returnType, argument.getType())) {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_RETURN_INCOMPATIBLE_TYPE + argument.getType().getName()
                        + " (" + argument.decompile() + ") au lieu de " + returnType.getName() + ".", getLocation());
            } else if (returnType.isFloat() && argument.getType().isInt()) {
                argument = new ConvFloat(argument);
            } else if (!returnType.sameType(argument.getType())) {
                Cast cast = new Cast(new Identifier(returnType.getName()), argument);
                cast.setType(returnType);
                argument = cast;
            }
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_RETURN_METHOD_VOID_TYPE, getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        argument.codeGenInst(compiler, Register.R0);
        compiler.addInstruction(new BRA(compiler.getLabelManager().getCurrentLabel()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getArgument().decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        argument.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        argument.prettyPrint(s, prefix, true);
    }
}
