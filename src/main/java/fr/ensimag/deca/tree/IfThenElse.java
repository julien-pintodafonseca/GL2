package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyExpr(compiler, localEnv, currentClass);
        if (condition.getType().isBoolean()) {
            thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
            elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE +
                    condition.getType(), getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        int i = compiler.getLabelManager().getLabelValue(LabelType.LB_ELSE);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_ELSE);
        Label labelBegin = new Label("else" + i);
        Label labelEnd = new Label("end_if" + i);

        condition.codeGenCMP(compiler, labelBegin, true);

        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(labelEnd));

        compiler.addLabel(labelBegin);
        elseBranch.codeGenListInst(compiler);
        compiler.addLabel(labelEnd);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
