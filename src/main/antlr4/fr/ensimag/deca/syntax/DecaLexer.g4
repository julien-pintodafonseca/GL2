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
GT: '>';
LEQ: '<=';
GEQ: '>=';
EXCLAM: '!';
EQUALS: '=';
EQEQ: '==';
NEQ: '!=';
AND: '&&';
OR: '||';
COMMA: ',';
ELSE: 'else';
WHILE: 'while';

SPACES: (' ' | '\t' | '\n' | '\r') { skip(); };

PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
READINT: 'readInt';
READFLOAT: 'readFloat';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
TRUE: 'true';
FALSE: 'false';

fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT: '0' .. '9';
fragment POSITIVE_DIGIT: '1' .. '9';

IDENT: (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;


fragment NUM: (DIGIT)+;
fragment SIGN: ('+' | '-')?;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f')?;
fragment DIGITHEX: DIGIT | LETTER;
fragment NUMHEX: (DIGITHEX)+ ;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;
FLOAT: FLOATDEC | FLOATHEX;

INT: '0' | POSITIVE_DIGIT DIGIT*;

fragment STRING_CAR: ~ ('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';


fragment MONOCOMMENT: '//' (~('\n'| '\r'))*;
fragment MULTICOMMENT:'/*' .*? '*/';
COMMENT:  (MONOCOMMENT | MULTICOMMENT) { skip(); };

// Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
