package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;

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
            if (argument.getType() != returnType) {
                // erreur à créer
                // règle (3.28) à implémenter
                // faire les cas de cast
            }
        } else {
            // erreur à créer
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        throw new UnsupportedOperationException("not yet implemented");
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
