package fr.ensimag.deca;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public final class ErrorMessages {
    // DecacCompilerError messages
    public static final String DECAC_COMPILER_WRONG_ENTRY = "L'entrée suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_OPTION = "L'option suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_FILE = "Le fichier suivant n'existe pas : ";

    // ContextualError messages
    public static final String CONTEXTUAL_ERROR_DECLVAR_NULL = "La variable déclarée est de type void.";
    public static final String CONTEXTUAL_ERROR_DECLVAR_DUPE = "La variable a déjà été déclarée précédemment.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_VAR = "Variable indéfinie.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_TYPE = "Type de variable indéfini.";
    public static final String CONTEXTUAL_ERROR_ASSIGN_INCOMPATIBLE_TYPE = "Type de variable incompatible pour l'affectation.";
}
