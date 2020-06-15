package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Method declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclMethod extends Tree {

    public abstract SymbolTable.Symbol getName();

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the methods) are OK,
     * without looking at method body.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the method are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;
}
