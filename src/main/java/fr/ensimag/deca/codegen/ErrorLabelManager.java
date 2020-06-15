package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class ErrorLabelManager {
    public Map<ErrorLabelType, Boolean> myLabels;

    public ErrorLabelManager() {
        myLabels = new HashMap<>();
        for (ErrorLabelType lt : ErrorLabelType.values()) {
            myLabels.put(lt, false);
        }
    }

    public void addError(ErrorLabelType lt) throws DecacFatalError {
        verifyLabelType(lt);
        myLabels.put(lt, true);
    }

    public String errorLabelName(ErrorLabelType lt) {
        switch (lt) {
            case LB_ARITHMETIC_OVERFLOW:
                return "arithmetic_overflow";
            case LB_READINT_BAD_ENTRY:
                return "read_error_int";
            case LB_READFLOAT_BAD_ENTRY:
                return "read_error_float";
            case LB_FULL_HEAP:
                return "tas_plein";
            case LB_FULL_STACK:
                return "pile_pleine";
            default:
                return null;
        }
    }

    public void printErrors(DecacCompiler compiler) {
        for (ErrorLabelType lt : myLabels.keySet()) {
            if (myLabels.get(lt)) {
                printError(compiler, lt);
            }
        }
    }

    private void printError(DecacCompiler compiler, ErrorLabelType lt){
        switch(lt) {
            case LB_ARITHMETIC_OVERFLOW:
                compiler.addComment("--------------------------------------------------");
                compiler.addComment("    Message d’erreur : dépassement arithmétique   ");
                compiler.addComment("        sur les \"float\" ou division par zéro      ");
                compiler.addComment("--------------------------------------------------");
                compiler.addLabel(new Label(errorLabelName(ErrorLabelType.LB_ARITHMETIC_OVERFLOW)));
                compiler.addInstruction(new WSTR(ErrorMessages.CODEGEN_ERROR_ARITHMETIC_OVERFLOW_OR_DIVISION_BY_ZERO));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                break;
            case LB_READINT_BAD_ENTRY:
                compiler.addComment("--------------------------------------------------");
                compiler.addComment("    Message d’erreur : dépassement ou erreur de   ");
                compiler.addComment("         syntaxe lors de la lecture de la         ");
                compiler.addComment("               saisie de l'utilisateur            ");
                compiler.addComment("--------------------------------------------------");
                compiler.addLabel(new Label(errorLabelName(ErrorLabelType.LB_READINT_BAD_ENTRY)));
                compiler.addInstruction(new WSTR(ErrorMessages.CODEGEN_ERROR_READINT_ERROR));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                break;
            case LB_READFLOAT_BAD_ENTRY:
                compiler.addComment("--------------------------------------------------");
                compiler.addComment("    Message d’erreur : dépassement ou erreur de   ");
                compiler.addComment("         syntaxe lors de la lecture de la         ");
                compiler.addComment("               saisie de l'utilisateur            ");
                compiler.addComment("--------------------------------------------------");
                compiler.addLabel(new Label(errorLabelName(ErrorLabelType.LB_READFLOAT_BAD_ENTRY)));
                compiler.addInstruction(new WSTR(ErrorMessages.CODEGEN_ERROR_READFLOAT_ERROR));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                break;
            case LB_FULL_HEAP:
                compiler.addComment("--------------------------------------------------");
                compiler.addComment("          Message d’erreur : allocation           ");
                compiler.addComment("              impossible, tas plein               ");
                compiler.addComment("--------------------------------------------------");
                compiler.addLabel(new Label(errorLabelName(ErrorLabelType.LB_FULL_HEAP)));
                compiler.addInstruction(new WSTR(ErrorMessages.CODEGEN_ERROR_FULL_HEAP));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                break;
            case LB_FULL_STACK:
                compiler.addComment("--------------------------------------------------");
                compiler.addComment("         Message d’erreur : pile pleine           ");
                compiler.addComment("--------------------------------------------------");
                compiler.addLabel(new Label(errorLabelName(ErrorLabelType.LB_FULL_STACK)));
                compiler.addInstruction(new WSTR(ErrorMessages.CODEGEN_ERROR_FULL_STACK));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
                break;
        }
    }

    private void verifyLabelType(ErrorLabelType lt) throws DecacFatalError {
        if (!myLabels.containsKey(lt)) {
            throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_LABEL_MANAGER_UNKNOWN_LABEL_TYPE);
        }
    }
}
