package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Selection extends AbstractLValue {
    AbstractExpr obj;
    AbstractIdentifier field;

    public Selection(AbstractExpr obj, AbstractIdentifier field) {
        Validate.notNull(obj);
        Validate.notNull(field);
        this.obj = obj;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError, DecacFatalError {
        // Syntaxe contextuelle : règles (3.65) et (3.66)
        obj.verifyExpr(compiler, localEnv, currentClass);
        Type typeClass = obj.getType();
        if (typeClass.isClass()) {
            ClassDefinition classDef = compiler.environmentType.getClassDefinition(typeClass.getName());
            field.verifyExpr(compiler, classDef.getMembers(), currentClass);

            if (field.getFieldDefinition().getVisibility() == Visibility.PROTECTED) {
                if (!compiler.environmentType.subType(currentClass.getType(), field.getFieldDefinition().getContainingClass().getType())) { // le type de la classe courante doit être un sous-type de la classe où le champ protégé est déclaré.
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_SELECTION_BAD_SUBTYPE + "de " + obj.decompile() + " (" + obj.getType() + ") n'est pas un sous-type de " + currentClass.getType() + ".", getLocation());
                } else if (!compiler.environmentType.subType(classDef.getType(), currentClass.getType())) { // le type de l’expression doit être un sous-type de la classe courante
                    throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_SELECTION_BAD_SUBTYPE + classDef.getType() + " n'est pas un sous-type de " + currentClass.getType() + ".", getLocation());
                }
            }
            setType(field.getType());
            return field.getType();
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_SELECTION_EXPR_IS_NOT_CLASS + obj.decompile() + " est de type " + obj.getType() + ".", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister register) throws DecacFatalError {
        obj.codeGenInst(compiler, Register.R1);
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_NULL_DEREFERENCEMENT))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_NULL_DEREFERENCEMENT);
        FieldDefinition fieldDef = field.getFieldDefinition();
        compiler.addInstruction(new LOAD(new RegisterOffset(fieldDef.getIndex(), Register.R1), register));
    }

    @Override
    protected void codeGenCMP(DecacCompiler compiler, Label label, boolean reverse) throws DecacFatalError {
        codeGenInst(compiler, Register.R1);
        compiler.addInstruction(new CMP(1, Register.R1));

        if(reverse) { // reverse = true
            compiler.addInstruction(new BEQ(label));
        } else{ // reverse = false
            compiler.addInstruction(new BNE(label));
        }
    }

    @Override
    protected DAddr codeGenOperandAssign(DecacCompiler compiler) throws DecacFatalError {
        obj.codeGenInst(compiler, Register.R1);
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_NULL_DEREFERENCEMENT))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_NULL_DEREFERENCEMENT);
        ClassDefinition def = compiler.environmentType.getClassDefinition(obj.getType().getName());
        ExpDefinition fieldDef = def.getMembers().get(field.getName());
        return fieldDef.getOperand();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        field.iter(f);
    }
}
