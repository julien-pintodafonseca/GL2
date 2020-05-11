lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

OBRACE: '{';
CBRACE: '}';
SEMI: ';';
OPARENT: '(';
CPARENT: ')';
IF: 'if';
LT: '<';
EQUALS: '=';
COMMA: ',';
ELSE: 'else';

SPACES: (' ' | '\t' | '\n' | '\r') { skip(); };

PRINT: 'print';
PRINTLN: 'println';
READINT: 'readInt';

fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT: '0' .. '9';
fragment POSITIVE_DIGIT: '1' .. '9';

IDENT: (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;
INT: '0' | POSITIVE_DIGIT DIGIT*;

fragment STRING_CAR: ~ ('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
