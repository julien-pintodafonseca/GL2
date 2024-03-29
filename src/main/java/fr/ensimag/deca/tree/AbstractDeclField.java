package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Field declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclField extends Tree {

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the fields members
     * are OK, without looking at field initialization.
     */
    protected abstract void verifyFieldMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Look at field initialization.
     */
    protected abstract void verifyFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError, DecacFatalError;

    protected abstract void codeGenField(DecacCompiler compiler, ClassDefinition currentClass);

    protected abstract void codeGenFieldInit(DecacCompiler compiler, ClassDefinition currentClass) throws DecacFatalError;
}
