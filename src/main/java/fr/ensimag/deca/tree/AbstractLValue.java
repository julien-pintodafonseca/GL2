package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.ima.pseudocode.DAddr;


/**
 * Left-hand side value of an assignment.
 * 
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractLValue extends AbstractExpr {

    protected abstract DAddr codeGenOperandAssign(DecacCompiler compiler) throws DecacFatalError;
}
