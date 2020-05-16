package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 * 
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 * 
 * @author Equipe GL2
 * @date 2020
 */
public interface TreeFunction {
    void apply(Tree t);
}
