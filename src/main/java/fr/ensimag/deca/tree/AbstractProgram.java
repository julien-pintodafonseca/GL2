package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author Equipe GL2
 * @date 2020
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError, EnvironmentExp.DoubleDefException, DecacFatalError;
    public abstract void codeGenProgram(DecacCompiler compiler) throws DecacFatalError;
}
