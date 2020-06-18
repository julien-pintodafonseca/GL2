package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.ErrorMessages;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author Equipe GL2
 * @date 2020
 */
public class EnvironmentType {
    private final Map<Symbol, TypeDefinition> envTypes;

    public EnvironmentType(DecacCompiler compiler) {
        envTypes = new HashMap<>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ObjectType(objectSymb);
        envTypes.put(objectSymb, new ClassDefinition(OBJECT, Location.BUILTIN, null));
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public void declareClass(Symbol classSymb, TypeDefinition typeDefClass) {
        envTypes.put(classSymb, typeDefClass);
    }

    public boolean isDeclare(Symbol symbol) {
        return envTypes.containsKey(symbol);
    }

    public ClassDefinition getClassDefinition(Symbol symbol) throws DecacFatalError {
        if (isDeclare(symbol)) {
            if (envTypes.get(symbol) instanceof ClassDefinition) {
                return (ClassDefinition) envTypes.get(symbol);
            } else {
                throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_ENVIRONNEMENT_TYPE_NOT_CLASS_DEFINITION + symbol.getName());
            }
        } else {
            throw new DecacFatalError(ErrorMessages.DECAC_FATAL_ERROR_SYMBOL_IN_ENVIRONNEMENT_TYPE_NOT_DECLARE + symbol.getName());
        }
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType NULL;
    public final ObjectType OBJECT;

    /**
     *
     * @param t
     * @param type
     * @return true si t est un sous-type de type
     */
    public boolean subType(Type t, Type type) throws DecacFatalError {
        // Règles de sous-typage
        if (t.isNull() || t == type || type.isObject()) {
            return true;
        } else if (t.isObject() || !t.isClass()) {
            return false;
        } else {
            Type typeSuperClass = getClassDefinition(t.getName()).getSuperClass().getType();
            return subType(typeSuperClass, type);
        }
    }

    /**
     *
     * @param t1
     * @param t2
     * @return true si on peut affecter à un object de type t1 un objet de type t2
     */
    public boolean assignCompatible(Type t1, Type t2) throws DecacFatalError {
        // Règles d'affectation
        if (t1.isFloat() && t2.isInt()) {
            return true;
        } else {
            return subType(t2, t1);
        }
    }
}
