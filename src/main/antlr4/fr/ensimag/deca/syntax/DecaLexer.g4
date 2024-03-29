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

DOT: '.';
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
ASM: 'asm';

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
CLASS: 'class';
THIS: 'this';
PROTECTED: 'protected';
RETURN: 'return';
NEW: 'new';
NULL: 'null';
EXTENDS: 'extends';
INSTANCEOF: 'instanceof';

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

/* Gestion des chaînes de caractères */
fragment STRING_CAR: ~ ('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

/* Gestion des commentaires */
fragment MONOCOMMENT: '//' (~('\n'| '\r'))*;
fragment MULTICOMMENT:'/*' .*? '*/';
COMMENT:  (MONOCOMMENT | MULTICOMMENT) { skip(); };

/* Gestion du #include */
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"' { doInclude(getText()); };
