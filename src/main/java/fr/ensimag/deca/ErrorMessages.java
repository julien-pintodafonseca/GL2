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
    public static final String CONTEXTUAL_ERROR_PRINT_INCOMPATIBLE_TYPE = "La fonction print n'affiche que des expressions de type int, float et string. Or vous essayez d'afficher une expression de type : ";
    public static final String CONTEXTUAL_ERROR_DECLCLASS_DUPE = "La classe suivante a déjà été déclarée précédemment : ";
    public static final String CONTEXTUAL_ERROR_UNREGISTRED_CLASS = "La classe suivante n'est pas définie : ";
    public static final String CONTEXTUAL_ERROR_INCOMPATIBLE_TYPE_FOR_NEW = "L'opérateur new instancie une variable de type classe. Or, vous essayez d'instancier une variable de type : ";
    public static final String CONTEXTUAL_ERROR_DECLFIELD_DUPE = "L'attribut suivant a déjà été déclaré précédemment dans la classe : ";
    public static final String CONTEXTUAL_ERROR_VOID_TYPE_FIELD = "Le type de l'attribut suivant est void : ";
    public static final String CONTEXTUAL_ERROR_VOID_TYPE_PARAM = "Le type du paramètre suivant est void : ";
    public static final String CONTEXTUAL_ERROR_DIFF_SIGNATURE_REDEFINED_METHOD = "Vous essayez de redéfinir la méthode suivante avec une signature différente : ";
    public static final String CONTEXTUAL_ERROR_METHOD_OVERRIDING_FIELD = "La méthode suivante possède le même nom qu'un des attributs hérités : ";
    public static final String CONTEXTUAL_ERROR_FIELD_OVERRIDING_METHOD = "L'attribut suivant possède le même nom qu'une des méthodes héritées : ";
    public static final String CONTEXTUAL_ERROR_DIFF_TYPE_REDEFINED_METHOD = "Vous essayez de redéfinir la méthode suivante avec une type de retour différent : ";
    public static final String CONTEXTUAL_ERROR_METHOD_DECLPARAM_DUPE =    "Une méthode prend des paramètres avec un nom unique. Or, le paramètre suivant existe déjà : ";
    public static final String CONTEXTUAL_ERROR_BAD_USED_THIS = "Le mot-clef this ne peut être utilisé qu'au sein d'une classe. Or, vous l'utilisez dans la fonction main.";
    public static final String CONTEXTUAL_ERROR_SELECTION_BAD_SUBTYPE = "Vous essayez d'accéder à un attribut protégé. Or, le type ";
    public static final String CONTEXTUAL_ERROR_SELECTION_EXPR_IS_NOT_CLASS = "L'accès aux champs d'une classe n'est possible que si le type de l'expression correspond a une classe. Or, l'expression ";
    public static final String CONTEXTUAL_ERROR_METHODCALL_EXPR_IS_NOT_CLASS = "L'accès aux méthodes d'une classe n'est possible que si le type de l'expression correspond a une classe. Or, l'expression ";
    public static final String CONTEXTUAL_ERROR_METHODCALL_WITHOUT_CLASS = "Vous essayez d'accéder à une méthode suivante sans préciser la classe où elle est définie : ";
    public static final String CONTEXTUAL_ERROR_RETURN_METHOD_VOID_TYPE = "La méthode est de type void : elle ne nécessite donc pas d'instruction \"return\"";
    public static final String CONTEXTUAL_ERROR_RETURN_INCOMPATIBLE_TYPE = "L'instruction return renvoie une expression dont le type n'est pas un sous-type du type de retour de la méthode : ";
    public static final String CONTEXTUAL_ERROR_METHODCALL_NO_PARAM_EXPECTED = "La méthode suivante n'attend aucun paramètre en entrée : ";
    public static final String CONTEXTUAL_ERROR_METHODCALL_MORE_OR_LESS_PARAM_EXPECTED = "La méthode suivante prend en entrée "; // x paramètres au lieu de y paramètres : method.getName()
    public static final String CONTEXTUAL_ERROR_INSTANCEOF_NOT_NULL_OR_CLASS = "L'opérateur instanceof n'accepte comme première opérande que l'instance d'une classe ou l'élément null. Or, l'expression suivante est de type ";
    public static final String CONTEXTUAL_ERROR_INSTANCEOF_NOT_CLASS = "L'opérateur instanceof n'accepte comme seconde opérande qu'un type de classe. Or, la seconde opérande est de type : ";
    public static final String CONTEXTUAL_ERROR_CAST_VOID_TYPE = "Vous essayer de caster l'expression suivante qui est de type void : ";
    public static final String CONTEXTUAL_ERROR_CAST_INCOMPATIBLE_TYPE = "L'expression suivante ne peut pas être castée dans le type ";

    // CodegenError messages
    public static final String CODEGEN_ERROR_ARITHMETIC_OVERFLOW_OR_DIVISION_BY_ZERO = "Erreur : dépassement arithmétique sur les flottants ou division par zéro.";
    public static final String CODEGEN_ERROR_READINT_ERROR = "Saisie incorrecte : la saisie est soit incorrecte (nombre entier attendu), soit celle d'un nombre trop grand (dépassement).";
    public static final String CODEGEN_ERROR_READFLOAT_ERROR = "Saisie incorrecte : la saisie est soit incorrecte (nombre réel attendu), soit celle d'un nombre trop grand (dépassement)";
    public static final String CODEGEN_ERROR_FULL_HEAP = "Erreur : allocation impossible, tas plein.";
    public static final String CODEGEN_ERROR_FULL_STACK = "Erreur : pile pleine.";
    public static final String CODEGEN_ERROR_NULL_DEREFERENCEMENT = "Erreur : déréférencement de null." ;
    public static final String CODEGEN_ERROR_CONV_FLOAT = "Erreur : conversion int -> float impossible.";
}
