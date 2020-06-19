package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Field declaration.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(DeclField.class);
    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier varName;
    private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        // Règle syntaxe contextuelle : (2.5)
        Type t = type.verifyType(compiler);
        type.setType(t);

        if (t.isVoid()) {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_VOID_TYPE_FIELD + varName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
        } else {
            // on vérifie que, si la variable est déjà définie dans l'environnement des expressions de la superClass,
            // il s'agit bien d'un identificateur de champ.
            Definition superVarName = currentClass.getSuperClass().getMembers().get(varName.getName());
            if (superVarName != null) {
                if (!superVarName.isField()) {
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_FIELD_OVERRIDING_METHOD + varName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
                }
            }
            currentClass.incNumberOfFields();
            FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), visibility, currentClass, currentClass.getNumberOfFields());
            varName.setDefinition(fieldDef);
            varName.setType(t);
            try {
                currentClass.getMembers().declare(varName.getName(), fieldDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_DECLFIELD_DUPE + varName.getName() + " (classe " + currentClass.getType() + ").", getLocation());
            }
        }
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError, DecacFatalError {
        // Règle syntaxe contextuelle : (3.7)
        LOG.debug("verify declFieldInit: start");
        initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
        LOG.debug("verify declFieldInit : end");
    }

    @Override
    protected void codeGenField(DecacCompiler compiler, ClassDefinition currentClass) {
        compiler.addComment("Initialisation de " + currentClass.getType() + "." + varName.getName());
        if(type.getType().isInt() || type.getType().isBoolean()) { // par défaut à la valeur 0 (qui signifie false dans le cas d'un booléen)
            compiler.addInstruction(new LOAD(0, Register.R0));
        } else if (type.getType().isFloat()) { // par défaut a la valeur 0.0
            compiler.addInstruction(new LOAD(0, Register.R0));
            compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
        } else { // par défaut à la valeur null pour toute classe
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        }
        varName.getFieldDefinition().setOperand(new RegisterOffset(varName.getFieldDefinition().getIndex(), Register.R1));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1)); // R1 contient l’adresse de l’objet
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(varName.getFieldDefinition().getIndex(), Register.R1))); // R1 contient l’adresse de l’objet
    }

    @Override
    protected void codeGenFieldInit(DecacCompiler compiler, ClassDefinition currentClass) throws DecacFatalError {
        if (initialization instanceof Initialization) {
            compiler.addComment("Initialisation explicite de " + currentClass.getType() + "." + varName.getName());
            initialization.codeGenInitialization(compiler, new RegisterOffset(varName.getFieldDefinition().getIndex(), Register.R1)); // R1 contient l’adresse de l’objet
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (visibility == Visibility.PROTECTED) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iterChildren(f);
        varName.iterChildren(f);
        initialization.iterChildren(f);
    }
}
