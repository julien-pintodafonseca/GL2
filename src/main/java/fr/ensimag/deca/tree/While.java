package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.codegen.LabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Règle syntaxe contextuelle : (3.25)
        condition.verifyExpr(compiler, localEnv, currentClass);
        if (condition.getType().isBoolean()) {
            body.verifyListInst(compiler, localEnv, currentClass, returnType);
        } else {
            throw new ContextualError(ErrorMessages.CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE +
                    condition.getType(), getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws DecacFatalError {
        int i = compiler.getLabelManager().getLabelValue(LabelType.LB_WHILE);
        Label labelBegin = new Label("while" + i);
        Label labelEnd = new Label("while_end" + i);
        compiler.getLabelManager().incrLabelValue(LabelType.LB_WHILE);

        compiler.addLabel(labelBegin);
        condition.codeGenCMP(compiler, labelEnd, true);

        body.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(labelBegin));
        compiler.addLabel(labelEnd);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        body.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }
}
