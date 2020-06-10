package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;

/**
 * Parameter declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public abstract class AbstractDeclParam extends Tree {

    public abstract Type verifyClassMembers(DecacCompiler compiler, ClassDefinition currentClass, DeclMethod currentMethode) throws ContextualError;
}
