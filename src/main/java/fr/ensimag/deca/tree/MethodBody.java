package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.codegen.ErrorLabelType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Body of method
 *
 * @author Equipe GL2
 * @date 2020
 */
public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        // Règle syntaxe contextuelle : (3.14) -> (3.18)
        declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
        if(insts.getList().size() != 0) {
            compiler.addComment("---------- Instructions :");
        }
        insts.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, ClassDefinition currentClass, SymbolTable.Symbol methodName, Type type) throws DecacFatalError {
        compiler.getTSTOManager().resetCurrentAndMax();
        Line lineTSTO = new Line(new TSTO(0));
        compiler.add(lineTSTO); // TSTO #d
        compiler.addInstruction(new BOV(new Label(compiler.getErrorLabelManager().errorLabelName(ErrorLabelType.LB_FULL_STACK))));
        compiler.getErrorLabelManager().addError(ErrorLabelType.LB_FULL_STACK);

        if(declVariables.size() != 0) {
            compiler.addInstruction(new ADDSP(declVariables.size()));
            compiler.getTSTOManager().addCurrent(declVariables.size());
        }

        // Code de la sauvegarde des registres
        compiler.addComment("Sauvegarde des registres");
        List<Line> saveRegisters = new ArrayList<>();
        for(int j=2; j<(compiler.getRegisterManager().getSize()); j++) {
            Line line = new Line(null, null, null);
            saveRegisters.add(line);
            compiler.add(line);
        }

        compiler.getRegisterManager().resetNbMaxRegistersUsed();

        declVariables.codeGenListDeclVar(compiler);
        insts.codeGenListInst(compiler);

        // Maintenant qu'on connait le nombre de registre max utilisé, on peut générer le code de la sauvegarde des registres
        int nb_register = compiler.getRegisterManager().getNbMaxRegistersUsed();
        compiler.getTSTOManager().addCurrent(nb_register);
        for(int j=2; j<nb_register+2; j++) {
            saveRegisters.get(j-2).setInstruction(new PUSH(Register.getR(j)));
            compiler.getRegisterManager().free(j);
        }

        // si la méthode n'est pas de type void, on s'assure qu'elle comprend bien une instruction de retour
        if(!type.isVoid()) {
            compiler.addInstruction(new WSTR("Erreur : sortie de la methode " + currentClass.getType() + "." + methodName + " sans instruction return."));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        compiler.addLabel(compiler.getLabelManager().getCurrentLabel());

        // Code de la restauration des registres
        compiler.addComment("Restauration des registres");
        for(int j=nb_register+1; j>1; j--) {
            compiler.addInstruction(new POP(Register.getR(j)));
            compiler.getRegisterManager().free(j);
        }
        compiler.getTSTOManager().addCurrent(-nb_register);

        compiler.addInstruction(new RTS());
        lineTSTO.setInstruction(new TSTO(compiler.getTSTOManager().getMax()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
}
