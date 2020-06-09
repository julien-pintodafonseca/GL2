------------------------------------------------------------------------
-- ma_dict.adb : implantation du dictionnaire de tokens               --
--                                                                    --
-- Auteur : X. Nicollin (d'apres le dictionnaire de Syntaxe)          --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Modifications :                                                    --
--   05/01/94                                                         --
--     TABLES --> MES_TABLES                                          --
--   27/11/94                                                         --
--     acces_nom --> acces_string                                     --
--     (changement dans tables.ads et pseudo_code.ads)                --
------------------------------------------------------------------------

with Ada.Text_IO, MA_Token_IO, Mes_Tables;
use  Ada.Text_IO, MA_Token_IO;

pragma Elaborate_All (Mes_Tables);
package body MA_Dict is

  package Ma_Table is new Mes_Tables(token, ETIQ_lex);
  use Ma_Table;

  dictio : table := creation_table;

  ----------------------------------------------------------------------------

  procedure mise_a_jour(S : in string; code : in out token; C : out chaine) is
    element : element_table;
    existe  : boolean;
  begin
    chercher_string(dictio, S, true, existe, element);
    if not existe then
      changer_info(element, code);
    end if;
    code := acces_info(element);
    C := acces_chaine(element);
  end mise_a_jour;

  ----------------------------------------------------------------------------

  procedure ins_tok(S : in string; code : in token) is
    element : element_table;
    existe  : boolean;
  begin
    chercher_string(dictio, S, true, existe, element);
    if not existe then
      changer_info(element, code);
    end if;
  end ins_tok;

  ----------------------------------------------------------------------------

  procedure imprimer is 

    procedure traiter (e : in element_table) is
    begin
      put("a la chaine : "); 
      put( acces_string(acces_chaine(e))); 
      put(" est associe le code : ");
      put(acces_info(e));
      new_line;
    end traiter;

    procedure mon_parcours is new parcourir_table(traiter);

  begin -- imprimer
    mon_parcours(dictio);
  end imprimer;

end MA_Dict;

