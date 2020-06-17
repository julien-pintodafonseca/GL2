package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Class declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclClass extends Tree {

    public abstract SymbolTable.Symbol getName();

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Construction of the Virtual Method Table
     *
     * @param compiler
     */
    protected abstract void codeGenMethodTable(DecacCompiler compiler);

    /**
     * Generate the code of the initialization of variables and the code of the methods
     * @param compiler
     */
    protected abstract void codeGenMethodAndFields(DecacCompiler compiler) throws DecacFatalError;
}
