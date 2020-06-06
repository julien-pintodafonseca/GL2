package fr.ensimag.deca;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public final class ErrorMessages {
    // DecacFatalError messages
    public static final String DECAC_FATAL_ERROR_REGISTER_MANAGER_WRONG_REG_NUMBER = "Numéro de registre inexistant : ";
    public static final String DECAC_FATAL_ERROR_LABEL_MANAGER_UNKNOWN_LABEL_TYPE = "Un type de label inexistant a été utilisé.";

    // DecacCompilerError messages
    public static final String DECAC_COMPILER_WRONG_ENTRY = "L'entrée suivante n'est pas reconnue : "; 
    public static final String DECAC_COMPILER_WRONG_OPTION = "L'option suivante n'est pas reconnue : "; 
    public static final String DECAC_COMPILER_WRONG_FILE = "Le fichier suivant n'existe pas : "; 
    public static final String DECAC_COMPILER_WRONG_R_OPT_VALUE = "Option -r X incorrecte ! Contrainte à respecter : 4 <= X <= 16.";
    public static final String DECAC_COMPILER_INCOMPATIBLE_OPTIONS = "Les options -p et -v sont incompatibles.";
    public static final String DECAC_COMPILER_ERROR_B_OPT_TOO_MANY_ARGS = "L'option -b ne peut pas être utilisée avec une autre option et ne prend pas de paramètre.";

    // ContextualError messages
    public static final String CONTEXTUAL_ERROR_DECLVAR_NULL = "La variable suivante est de type void : "; 
    public static final String CONTEXTUAL_ERROR_DECLVAR_DUPE = "La variable suivante a déjà été déclarée précédemment : ";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_VAR = "La variable suivante est indéfinie : ";
    public static final String CONTEXTUAL_ERROR_IDENT_NULL_TYPE = "Le type de variable suivant est indéfini : ";
    public static final String CONTEXTUAL_ERROR_INCOMPATIBLE_ASSIGN_TYPE = "Le type de variable suivant est incompatible pour l'affectation : ";
    public static final String CONTEXTUAL_ERROR_INCOMPATIBLE_COMPARISON_TYPE = "Deux expressions ne peuvent pas être comparées si leurs types sont les suivants : ";
    public static final String CONTEXTUAL_ERROR_CONDITION_BOOLEAN_INCOMPATIBLE_TYPE = "L'expression dans la condition booléenne doit être de type booléen. Or, elle est de type : ";
    public static final String CONTEXTUAL_ERROR_UNARY_MINUS_INCOMPATIBLE_TYPE = "L'opérateur moins unaire (symbole \"-\") doit s'appliquer sur une expression de type int ou float. Or, il est appliqué sur une expression de type : ";
    public static final String CONTEXTUAL_ERROR_ARITHMETIC_OPERATION_INCOMPATIBLE_TYPE = "Deux expressions ne peuvent pas être additionnées, soustraites, divisées ou multipliées si elles ne sont pas de type int ou float. Or, leurs types sont les suivants : ";
    public static final String CONTEXTUAL_ERROR_MODULO_INCOMPATIBLE_TYPE = "L'opérateur % n'est utilisable qu'avec deux expressions de type int. Or, il est actuellement utilisé avec des expressions de type : ";

    // CodegenError messages
    public static final String CODEGEN_ERROR_ARITHMETIC_OVERFLOW_OR_DIVISION_BY_ZERO = "Erreur : dépassement arithmétique ou division par zéro";

}
