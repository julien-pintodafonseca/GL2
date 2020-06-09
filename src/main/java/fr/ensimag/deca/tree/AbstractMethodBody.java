package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;

/**
 * Body of method
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractMethodBody extends Tree {
    protected abstract void codeGenMethodBody(DecacCompiler compiler) throws DecacFatalError;

    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler) throws ContextualError;
}
