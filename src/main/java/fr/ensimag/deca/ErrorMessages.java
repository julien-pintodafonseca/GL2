package fr.ensimag.deca;

public final class ErrorMessages {
    // DecacCompilerError messages
    public static final String DECAC_COMPILER_WRONG_ENTRY = "L'entrée suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_OPTION = "L'option suivante n'est pas reconnue : ";
    public static final String DECAC_COMPILER_WRONG_FILE = "Le fichier suivant n'existe pas : ";

    // ContextualError messages
    public static final String CONTEXTUAL_ERROR_DECLVAR_NULL = "La variable déclarée est de type void.";
    public static final String CONTEXTUAL_ERROR_DECLVAR_DUPE = "La variable déclarée est dupliquée.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_VAR = "Variable indéfinie.";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_TYPE = "Type de variable indéfini.";
}
