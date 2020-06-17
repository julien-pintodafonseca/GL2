package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.AbstractDeclParam;
import fr.ensimag.deca.tree.TreeList;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.util.zip.CheckedOutputStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify listClassFields : start");
        // Règle syntaxe contextuelle : (2.4)
        for (AbstractDeclField declField : getList()) {
            declField.verifyClassMembers(compiler, currentClass);
        }
        LOG.debug("verify listClassFields: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    protected void verifyListClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.6)
        LOG.debug("verify declFieldInit: start");
        for (AbstractDeclField declField : getList()) {
            declField.verifyClassBody(compiler, localEnv, currentClass);
        }
        LOG.debug("verify declFieldInit : end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()) {
            field.decompile(s);
            s.println();
        }
    }

    public void codeGenFields(DecacCompiler compiler, ClassDefinition currentClass) throws DecacFatalError {
        compiler.addLabel(new Label("init."+currentClass.getType()));
        if (!currentClass.getSuperClass().getType().isObject()) {
            compiler.addInstruction(new TSTO(3), "Test de debordement de pile");
            compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_FULL_STACK))));
            compiler.getErrorLabelManager().addError(ErrorLabelType.LB_FULL_STACK);

            // Initialisation à 0
            for (AbstractDeclField field : this.getList()) {
                field.codeGenField(compiler, currentClass);
            }

            // Appel de l’initialisation des champs hérités
            compiler.addComment("Appel de l’initialisation des champs herites de " + currentClass.getSuperClass().getType());
            compiler.addInstruction(new PUSH(Register.R1)); // on empile l’objet à initialiser
            compiler.addInstruction(new BSR(new Label("init." + currentClass.getSuperClass().getType().toString())));
            compiler.addInstruction(new SUBSP(1)); // on remet la pile dans son état initial

            // Initialisation explicite à la bonne valeur
            for (AbstractDeclField field : this.getList()) {
                field.codeGenFieldInit(compiler, currentClass);
            }
        } else {
            // Initialisation à 0
            for (AbstractDeclField field : this.getList()) {
                field.codeGenField(compiler, currentClass);
            }

            // Initialisation explicite à la bonne valeur
            for (AbstractDeclField field : this.getList()) {
                field.codeGenFieldInit(compiler, currentClass);
            }
        }
        compiler.addInstruction(new RTS());
    }
}