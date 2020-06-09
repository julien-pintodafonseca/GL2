------------------------------------------------------------------------
-- ma_lexico.l : source Aflex de l'analyseur lexical du langage       --
--               d'assemblage de la machine abstraite                 --
--                                                                    --
-- Auteur : X. Nicollin                                               --
-- Modifs : R. Groz: litteraux flottants hexadecimaux                 --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 04/07/2016                         --
------------------------------------------------------------------------

BLANC       [ \t]
COMMENT     (";".*)
SEPAR       ({BLANC}|{COMMENT})

CHIFFRE     [0-9]
CHIFFREHEX  [0-9A-Fa-f]

NUM         ({CHIFFRE}+)
NUMHEX      ({CHIFFREHEX}+)
INTPOS      ("+"{NUM})
INTNAT      {NUM}
INTNEG      ("-"{NUM})
IMMPOS      (#{INTPOS})
IMMNAT      (#{INTNAT})
IMMNEG      (#{INTNEG})

SIGNE       ([+-]?)
EXP         ([Ee]{SIGNE}{NUM})
DEC         (#{SIGNE}{NUM}"."{NUM})
IMMFLOADEC  ({DEC}|{DEC}{EXP})
IMMFLOAHEX  (#{SIGNE}0x(1|0).{NUMHEX}p{SIGNE}{NUM})
IMMFLOA     ({IMMFLOADEC}|{IMMFLOAHEX})

IMMNULL     #null

LETTRE      [a-zA-Z]
IDENT       ({LETTRE}({LETTRE}|{CHIFFRE}|[_.])*)

CHAINE_CAR  [^\n\"]
CHSIMPLE    (\"{CHAINE_CAR}*\")
CHAINE      (\"({CHAINE_CAR}|\"\")+\")


%%


\n         {
  yylval := (lex_autre, ligne_courante);
  ligne_courante := ligne_courante + 1;
  return NL_lex;   }


{SEPAR}    {
  null; }


{IDENT}    {
  declare
    texte  : string(yytext'range) := yytext;
    att    : chaine;
    code   : token                := ETIQ_lex;
  begin
    normaliser(texte);
    mise_a_jour(texte, code, att);
    case code is
      when ETIQ_lex =>
        yylval := (lex_etiq, ligne_courante, att);
      when REGB_lex =>
        yylval := (lex_regb, ligne_courante,
                   natural'value(texte( texte'first+1 ..  texte'last)));
      when others =>
        yylval := (lex_autre, ligne_courante);
    end case;
    return code;
  end; }


{INTPOS}   {
  begin
    yylval := (lex_entier, ligne_courante,
               entier'value(yytext(yytext'first+1 .. yytext'last)));
    return DEPL_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end ; }


{INTNAT}   {
  begin
    yylval := (lex_entier, ligne_courante, entier'value(yytext));
    return DEPL_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end ; }


{INTNEG}   {
  begin
    yylval := (lex_entier, ligne_courante,
               entier'value(yytext(yytext'first .. yytext'last)));
    return DEPL_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end; }


{IMMPOS}   {
  begin
    yylval := (lex_entier, ligne_courante,
               entier'value(yytext(yytext'first+2 .. yytext'last)));
    return CONSTENTPOS_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end; }


{IMMNAT}   {
  begin
    yylval := (lex_entier, ligne_courante,
               entier'value(yytext(yytext'first+1 .. yytext'last)));
    return CONSTENTPOS_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end; }


{IMMNEG}   {
  begin
    yylval := (lex_entier, ligne_courante,
               entier'value(yytext(yytext'first+1 .. yytext'last)));
    return CONSTENTNEG_lex;
  exception
    when others =>
      erreur("depassement de capacite d'entier") ;
      raise MA_erreur_conversion;
  end; }


{IMMFLOA}  {
  declare
    val_Flot_MA : Flot_MA; f: float;
    long     : positive;
    decal : natural :=0 ;
    temp     : string(yytext'first+1 .. yytext'last);
  begin
     temp := yytext(yytext'first+1 .. yytext'last);
     if temp(yytext'first+1) = '+' or temp(yytext'first+1) = '-'
        then decal := 1;
     end if;
    if temp(yytext'first+2+decal) /= 'x' then
       get(temp, f, long);
       val_Flot_MA := Flot_MA(f);
    else
       val_Flot_MA := flot_hex_vers_Flot_MA(temp);
    end if;
    yylval := (lex_flottant, ligne_courante, val_flot_MA);
    return (CONSTFLOA_lex);
  exception
    when others =>
      erreur("depassement de capacite de flottant") ;
      raise MA_erreur_conversion;
  end; }

{IMMNULL}     {
   yylval := (lex_autre, ligne_courante);
   return NULL_lex; }

"("        {
  yylval := (lex_autre, ligne_courante);
  return '('; }


")"        {
  yylval := (lex_autre, ligne_courante);
  return ')'; }


","        {
  yylval := (lex_autre, ligne_courante);
  return ','; }


{CHSIMPLE} { --  Les chaines simples sont plus faciles a convertir.
  yylval := (lex_chaine, ligne_courante, valeur_chaine(yytext));
  return CONSTCHAINE_lex; }


{CHAINE}   { -- Il faut convertir les chaines dans le cas general
  yylval := (lex_chaine, ligne_courante, valeur_chaine2(yytext));
  return CONSTCHAINE_lex; }


":"        {
  yylval := (lex_autre, ligne_courante);
  return ':'; }


.          {
  erreur("caractere " & yytext(1) & " non permis");
  raise MA_erreur_lexicale ; }

%%

with MA_DICT, TYPES_BASE, FLOTTANT_ES;
use  MA_DICT, TYPES_BASE, FLOTTANT_ES;

pragma Elaborate_All (MA_Dict);
package body MA_LEXICO is

  subtype Chiffre is Character range '0' .. '9';
  chiffres       : constant array(Chiffre) of natural :=
                                (0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    -- pour convertir un chiffre en sa valeur numerique

  ligne_courante :          positive := 1; -- : numero de ligne courante

  -----------------------------------------------------------------------------

  function num_ligne return positive is
  begin
     return ligne_courante;
  end num_ligne;

  -----------------------------------------------------------------------------

  function valeur_chaine (S : string) return chaine is
  -- retourne une copie protegee de la constante chaine de caracteres
  begin
    return creation(S(S'first+1..S'last-1));
  end valeur_chaine;

  -----------------------------------------------------------------------------

  function valeur_chaine2 (S : string) return chaine is
  -- retourne une copie protegee de la constante chaine de caracteres
    temp         : string(S'range);
    indice_copie : integer         := temp'first;
    indice       : integer         := S'first + 1;
  begin
    while indice <= S'last-1 loop
      temp(indice_copie) := S(indice);
      indice_copie := indice_copie+1;
      if S(indice) /= '"' then
        indice := indice + 1;
      else
        indice := indice + 2;
      end if;
    end loop;
    return creation(temp(temp'first .. indice_copie-1));
  end valeur_chaine2;

  -----------------------------------------------------------------------------

  procedure erreur(S : in string) is
  -- affichage de message d'erreur
  begin
    put_line ("LIGNE " & positive'image(ligne_courante) & " -- ");
    put_line ("   Erreur : " & S);
  end erreur;

  -----------------------------------------------------------------------------

  procedure normaliser  (S : in out string) is
  -- convertit une chaine d'alphanumeriques en majuscules
    diff : constant := character'pos('A')-character'pos('a');
  begin
    for i in S'range loop
      if S(i) in 'a'..'z' then
        S(i) := character'val(character'pos(S(i)) + diff);
      end if;
    end loop;
  end normaliser;

  -----------------------------------------------------------------------------

  function flot_hex_vers_Flot_MA (S : in string) return Flot_MA is
  -- convertit une chaine representant un litteral flottant hex en flot_MA
--    diff : constant := character'pos('A')-character'pos('a');
     Longueur : Natural := S'Length; Deb : Natural := S'First;
     f, g : Flot_MA := 1.0;
     Index_P : Natural;
     exp : Integer := 0;
     Hex : character;
     chif: Natural;
     negatif : boolean := false ;
     Nb_Bits : integer := 23; -- nombre de bits à extraire de la mantisse
  begin
     if S(Deb)='-' or S(Deb)='+'  then
         negatif := S(Deb)='-'; Deb := Deb+1; Longueur := Longueur-1 ;
     end if ;
     if Longueur <7 or ((S(Deb..Deb+3)) /= "0x1." and (S(Deb..Deb+3)) /= "0x0.") then
        erreur("erreur lexicale interne 1 sur flottant hexadecimal: " & S);
        raise MA_Erreur_Lexicale;
     end if;
     if S(Deb..Deb+3) = "0x0." then
        F := 0.0;
     end if;
     Index_P := Deb + 4;
     while Index_P < S'Last and (S(Index_P) /= 'p') loop
        Index_P := Index_P + 1;
     end loop;
     
     -- On tolere ici l'absence d'exposant, meme si la lexico ne le permet pas
     if Index_P=S'Last then return f;
     elsif (S(Index_P)) /= 'p' then
        erreur("erreur lexicale interne 3 sur flottant hexadecimal: " & S);
        raise MA_Erreur_Lexicale;
     -- On extrait l'exposant qu'on convertit en entier
     else
        exp := Integer'Value(S(Index_P + 1..S'Last));
        if exp > 127 then raise Constraint_Error; end if;
     end if;
     
     -- Pour les dénormalisés, le nombre de bits significatifs est plus petit.
     if Exp <= -127 then
        Nb_Bits := Nb_Bits + (126 + Exp);
     end if;
     
     -- Extraction de la mantisse, et calcul de sa representation flottante
     --  comme un nombre compris entre 1.0 et 2.0, accumule dans f
     for M in Deb + 4 .. Index_P - 1 loop
        exit when Nb_Bits <= 0;
        Hex := S(M);
        if Hex in '0'..'9' then
           chif := character'pos(Hex) - character'pos('0');
        elsif Hex in 'A'..'F' then
           chif := character'pos(Hex) - character'pos('A') + 10;
        elsif Hex in 'a'..'f' then
           chif := character'pos(Hex) - character'pos('a') + 10;
        else
           erreur("erreur lexicale interne 2 sur flottant hexadecimal, chiffre: " & Hex);
           raise MA_Erreur_Lexicale;
        end if;
        if Nb_Bits >= 4 then
           g := g / 16.0;
           f := f + (g * Flot_MA(chif));
           nb_bits := nb_bits - 4;
        else
           g := g / Flot_Ma(2**Nb_Bits);
           f := f + (g * Flot_MA(Chif / 2**(4-Nb_Bits)));
           Nb_Bits := 0;
        end if;
        -- Si m>6 les chiffres supplementaires ne sont pas pris en compte
        -- On ne garde que les 23 premiers bits,
        --   pas d'arrondi sur les bits suivants
     end loop;
     
     -- prise en compte de l'exposant
     if Exp > -127 then f := f * (2.0 ** exp);
     -- Attention, Ada gere mal les calculs avec les denormalises
     -- pour la precision demandee
     -- donc la formule ci-dessus n'est plus adaptee
     else
           -- Attention, (2.0 ** exp) est trop grand (+infini),
           -- on fait la division en deux fois pour éviter le débordement.
           Exp := Exp + 126;
           f := f / (2.0**(-exp));
           f := f / (2.0**126);
     end if;
     
     if negatif then f := -f; end if;
     
     return f;
  end flot_hex_vers_Flot_MA;

  -----------------------------------------------------------------------------

  function sup_esp(S : string) return string is
  -- supprime l'espace en seconde position de S
    S1 :         string(1 .. S'length-1);
  begin
    S1(1) := S(S'first);
    S1(2 .. S1'last) := S(S'first + 2 .. S'last);
    return S1;
  end sup_esp;

  -----------------------------------------------------------------------------

  procedure init_dico is
  -- initialise le dictionnaire avecles ``identificateurs'' autorises :
  -- codes op, LB et GB et SP et le registres banalises
  begin
    ins_tok ("LOAD",    LOAD_lex);
    ins_tok ("STORE",   STORE_lex);
    ins_tok ("LEA",     LEA_lex);
    ins_tok ("PEA",     PEA_lex);
    ins_tok ("PUSH",    PUSH_lex);
    ins_tok ("POP",     POP_lex);
    ins_tok ("ADDSP",   ADDSP_lex);
    ins_tok ("SUBSP",   SUBSP_lex);
    ins_tok ("ADD",     ADD_lex);
    ins_tok ("SUB",     SUB_lex);
    ins_tok ("OPP",     OPP_lex);
    ins_tok ("MUL",     MUL_lex);
    ins_tok ("QUO",     QUO_lex);
    ins_tok ("CMP",     CMP_lex);
    ins_tok ("REM",     REM_lex);
    ins_tok ("FLOAT",   FLOAT_lex);
    ins_tok ("NEW",     NEW_lex);
    ins_tok ("DEL",     DEL_lex);
    ins_tok ("SEQ",     SEQ_lex);
    ins_tok ("SNE",     SNE_lex);
    ins_tok ("SGT",     SGT_lex);
    ins_tok ("SLT",     SLT_lex);
    ins_tok ("SGE",     SGE_lex);
    ins_tok ("SLE",     SLE_lex);
    ins_tok ("SOV",     SOV_lex);
    ins_tok ("DIV",     DIV_lex);
    ins_tok ("INT",     INT_lex);
    ins_tok ("BRA",     BRA_lex);
    ins_tok ("BEQ",     BEQ_lex);
    ins_tok ("BNE",     BNE_lex);
    ins_tok ("BGT",     BGT_lex);
    ins_tok ("BLT",     BLT_lex);
    ins_tok ("BGE",     BGE_lex);
    ins_tok ("BLE",     BLE_lex);
    ins_tok ("BOV",     BOV_lex);
    ins_tok ("BSR",     BSR_lex);
    ins_tok ("RTS",     RTS_lex);
    ins_tok ("RINT",    RINT_lex);
    ins_tok ("RUTF8",   RUTF8_lex);
    ins_tok ("RFLOAT",  RFLOAT_lex);
    ins_tok ("WINT",    WINT_lex);
    ins_tok ("WUTF8",   WUTF8_lex);
    ins_tok ("WFLOAT",  WFLOAT_lex);
    ins_tok ("WFLOATX", WFLOATX_lex);
    ins_tok ("WSTR",    WSTR_lex);
    ins_tok ("WNL",     WNL_lex);
    ins_tok ("TSTO",    TSTO_lex);
    ins_tok ("SETROUND_TONEAREST", SETROUND_TONEAREST_lex);
    ins_tok ("SETROUND_UPWARD", SETROUND_UPWARD_lex);
    ins_tok ("SETROUND_DOWNWARD", SETROUND_DOWNWARD_lex);
    ins_tok ("SETROUND_TOWARDZERO", SETROUND_TOWARDZERO_lex);
    ins_tok ("HALT",    HALT_lex);
    ins_tok ("ERROR",   ERROR_lex);
    ins_tok ("SHL",     SHL_lex);
    ins_tok ("SHR",     SHR_lex);
    ins_tok ("CLK",     CLK_lex);
    ins_tok ("SCLK",    SCLK_lex);
    ins_tok ("LB",      LB_lex);
    ins_tok ("GB",      GB_lex);
    ins_tok ("SP",      SP_lex);

    ins_tok ("FMA",     FMA_lex);

    for i in Num_reg_banalise loop
      ins_tok(sup_esp("R" & Num_reg_banalise'image(i)), REGB_lex);
    end loop;
  end init_dico;

function YYLex_Wrapper return Token is
-- Emplacement de YYLex généré par Aflex
##
begin
   return YYLex;
exception
   when NULL_IN_INPUT =>
      Put_Line("Ligne" & Integer'Image(Ligne_Courante) & " : caractère nul non autorisé dans un fichier source");
      raise MA_Erreur_Lexicale;
end YYLex_Wrapper;

-- fonction YYLex appelée par Ayacc
function YYLex return Token renames YYLex_Wrapper;

begin
  init_dico;
end MA_LEXICO;
