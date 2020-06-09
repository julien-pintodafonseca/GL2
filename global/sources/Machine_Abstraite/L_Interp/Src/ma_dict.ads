------------------------------------------------------------------------
-- ma_dict.ads                                                        --
-- Ce module implemente un dictionnaire permettant d'associer des     --
-- codes (tokens) a des chaines d'alphanumeriques. Initialement ce    --
-- dictionnaire est vide, c'est-a-dire ne contient aucune association --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 17/11/94                           --
------------------------------------------------------------------------

with Types_Base, MA_Syntax_Tokens;
use  Types_Base, MA_Syntax_Tokens;

package MA_Dict is

  procedure mise_a_jour(S : in string; code : in out token; C : out chaine);
  -- cherche si la chaine S est deja dans le dictionnaire,
  -- si oui, en sortie code contient le token associe a S
  -- sinon, la chaine S est rentree dans le dictionnaire
  -- avec comme token la valeur d'entree de code.
  -- Dans tous les cas C est une copie de la chaine S.

  procedure ins_tok(S : in string; code : in token);
  -- insere la chaine S et le token code dans le dictionnaire, sans verification

  procedure imprimer;
  -- imprime le dictionnaire dans un ordre quelconque.
  -- Chaque entree est affichee sous la forme :
  -- A la chaine : ... est associe le code : ...

end MA_Dict;

