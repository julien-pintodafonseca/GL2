package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Body of method
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractMethodBody extends Tree {
    protected abstract int getNumberDeclVariables();

    protected abstract void codeGenMethodBody(DecacCompiler compiler) throws DecacFatalError;

    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError;
}
