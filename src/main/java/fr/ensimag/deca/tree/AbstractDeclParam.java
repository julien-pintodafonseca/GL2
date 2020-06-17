package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

/**
 * Parameter declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclParam extends Tree {
    protected abstract ExpDefinition getExpDefinition();

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the type of
     * the params of a method are OK.
     */
    protected abstract Type verifyParamMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethode) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle].
     */
    protected abstract void verifyParamBody(DecacCompiler compiler, EnvironmentExp envExpParams) throws ContextualError;
}
