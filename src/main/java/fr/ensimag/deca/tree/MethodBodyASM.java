package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Line;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.List;

/**
 * Body of method
 *
 * @author Equipe GL2
 * @date 2020
 */
public class MethodBodyASM extends AbstractMethodBody {
    private StringLiteral asm;

    public MethodBodyASM(StringLiteral asm) {
        Validate.notNull(asm);
        this.asm = asm;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        // Règle syntaxe contextuelle : (3.15)
        asm.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, ClassDefinition currentClass, SymbolTable.Symbol methodName, Type type) throws DecacFatalError {
        compiler.add(new InlinePortion(asm.getValue()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("asm(");
        asm.decompile(s);
        s.println(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asm.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        asm.iter(f);
    }
}
