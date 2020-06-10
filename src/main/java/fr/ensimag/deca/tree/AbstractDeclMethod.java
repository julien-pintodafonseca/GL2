package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Class declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclMethod extends Tree {


    public abstract void verifyClassMembers(DecacCompiler compiler, EnvironmentExp superClass, ClassDefinition currentClass) throws ContextualError;
}
