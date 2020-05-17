package fr.ensimag.deca;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public final class ErrorMessages {
    // DecacFatalError messages
    public static final String DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER = "Numéro de registre inexistant : ";

    // DecacCompilerError messages
    public static final String DECAC_COMPILER_WRONG_ENTRY = "L'entrée suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_OPTION = "L'option suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_FILE = "Le fichier suivant n'existe pas : ";
    public static final String DECAC_COMPILER_WRONG_R_OPT_VALUE = "Option -r X incorrecte ! Contrainte à respecter : 4 <= X <= 16.";

    // ContextualError messages
    public static final String CONTEXTUAL_ERROR_DECLVAR_NULL = "La variable déclarée est de type void.";
    public static final String CONTEXTUAL_ERROR_DECLVAR_DUPE = "La variable a déjà été déclarée précédemment.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_VAR = "Variable indéfinie.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_TYPE = "Type de variable indéfini.";
    public static final String CONTEXTUAL_ERROR_ASSIGN_INCOMPATIBLE_TYPE = "Type de variable incompatible pour l'affectation.";
}
