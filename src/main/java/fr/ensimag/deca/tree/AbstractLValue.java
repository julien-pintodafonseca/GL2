package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractLValue extends AbstractExpr {

    public abstract DAddr codeGenOperandAssign(DecacCompiler compiler) throws DecacFatalError;
}
