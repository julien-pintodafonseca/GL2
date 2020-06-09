------------------------------------------------------------------------
-- ma_syntax.ads : interface de l'analyseur syntaxique de la MA       --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 17/11/94                           --
------------------------------------------------------------------------

with Pseudo_Code, MA_Syntax_Tokens, MA_Lexico, MA_Detiq;
use  Pseudo_Code; 

package MA_Syntax is

  procedure analyser_construire_liste_lignes(L : out ligne; 
                                             nblignes : out natural;
                                             nbinsts : out natural);
  -- Analyse syntaxique et construction de la liste d'instructions du programme
  -- En cas d'erreur syntaxique leve l'exception MA_erreur_syntaxe.
  -- En cas d'etiquette doublement definie, leve MA_double_def_etiq

  MA_double_def_etiq : exception renames MA_Detiq.MA_double_defetiq;
  MA_erreur_syntaxe : exception renames MA_syntax_tokens.syntax_error;
  MA_erreur_lexicale : exception renames MA_Lexico.MA_erreur_lexicale;
  MA_erreur_conversion : exception renames MA_Lexico.MA_erreur_conversion;

end MA_Syntax;
