------------------------------------------------------------------------
-- ma_lexico.ads : interface de l'analyseur lexical de la MA          --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 17/11/94                           --
------------------------------------------------------------------------

with MA_Syntax_Tokens;
use  MA_Syntax_Tokens;

package MA_Lexico is

   function yylex return token;
   -- Lit une une unite lexicale et retourne le code de l'unite reconnue.
   -- Met a jour la variable yylval (definie dans Syntax_Tokens) :
   --   -  dans tous les cas, le champ num_ligne_xxx adequat contient
   --      le numero de ligne de l'unite
   --   -  pour le code ETIQ_lex, le champ val_etiq contient une copie
   --      de la chaine alphanumerique denotant l'etiquette
   --   -  pour les codes CONSTENTPOS_lex et CONSTENTNEG_lex, le champ 
   --      val_ent contient la valeur numerique de la constante
   --   -  pour le code CONSTREEL_lex, le champ val_reel contient la
   --      valeur numerique de la constante
   --   -  pour le code REGB_lex, le champ num_regb contient le
   --      numero du registre banalise
   --   -  pour le code CONSTCHAINE_lex, le champ val_chaine contient
   --      l'interpretation de la chaine de caracteres
   --      Par exemple, la chaine '"abc""de"' s'interprete en 'abc"de'

   function num_ligne return positive;
   -- Retourne le numero de ligne dans le fichier source
   -- de la derniere unite lexicale reconnue.

   MA_erreur_lexicale, MA_erreur_conversion : exception;
   -- MA_erreur_lexicale est levee en cas de reconnaissance impossible d'unite.
   -- MA_erreur_conversion est levee en cas d'impossibilite de calculer
   -- la valeur d'une constante entiere ou reelle.

end MA_Lexico;
