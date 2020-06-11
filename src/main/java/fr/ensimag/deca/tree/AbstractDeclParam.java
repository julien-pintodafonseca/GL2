package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Parameter declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclParam extends Tree {

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the type of
     * the params of a method are OK.
     */
    public abstract Type verifyParamMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethode) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle].
     */
    public abstract void verifyParamBody(DecacCompiler compiler, EnvironmentExp envExpParams) throws ContextualError;
}
