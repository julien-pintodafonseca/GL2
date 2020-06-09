------------------------------------------------------------------------
-- ma_detiq.adb : implantation du dictionnaire d'etiquettes           --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Modifications :                                                    --
--   05/01/94                                                         --
--     TABLES --> MES_TABLES                                          --
--   27/11/94                                                         --
--     acces_nom --> acces_string                                     --
--     (changement dans tables.ads et pseudo_code.ads)                --
------------------------------------------------------------------------

with Types_Base, Ada.Text_IO, Mes_Tables;
use  Types_Base, Ada.Text_IO;

pragma Elaborate_All (Mes_Tables);
package body MA_Detiq is

  type info is record
    val_etiq : etiq;
    definie : boolean;
    utilisee : boolean;
    val_ligne : ligne;
  end record;

  info_vide : constant info := (null, false, false, null);

  package MA_TABLE is new MES_TABLES(info, info_vide);
  use MA_TABLE;

  detiq : table := creation_table;

  ----------------------------------------------------------------------------

  procedure ins_def_etiq(S : in string; E : out etiq) is
    element : element_table;
    existe  : boolean;
    l_info  : info;
    E1      : etiq;
  begin
    chercher_string(detiq, S, true, existe, element);
    if existe then 
      l_info := acces_info(element);
      if l_info.definie then
        raise MA_double_defetiq;
      else
        E := l_info.val_etiq;
        changer_info(element, (l_info.val_etiq, true, l_info.utilisee,
                               null));
      end if;
    else
      E1 := l_etiq(S);
      changer_info(element, (E1, true, false, null));
      E := E1;
    end if;
  end ins_def_etiq;
      
  ----------------------------------------------------------------------------

  procedure ins_util_etiq(S : in string; E : out etiq) is
    element : element_table;
    existe  : boolean;
    l_info  : info;
    E1      : etiq;
  begin
    chercher_string(detiq, S, true, existe, element);
    if existe then
      l_info := acces_info(element);
      changer_info(element, (l_info.val_etiq, l_info.definie, true,
                             l_info.val_ligne));
      E := l_info.val_etiq;
    else
      E1 := l_etiq(S);
      changer_info(element, (E1, false, true, null));
      E := E1;
    end if;
  end ins_util_etiq;
   
  ----------------------------------------------------------------------------

  procedure changer_ligne_def (E : in out etiq; L : in ligne) is
    S       : constant string := acces_string(E);
    l_info  :          info;
    existe  :          boolean;
    element :          element_table;
  begin
    chercher_string(detiq, S, false, existe, element);
    if existe then 
      l_info := acces_info(element);
      changer_info(element, (l_info.val_etiq, l_info.definie, 
                             l_info.utilisee, L));
    else
      raise MA_etiq_inexistante;
    end if;
  exception
    when erreur_etiq => raise ERREUR_INTERNE_DETIQ;
  end changer_ligne_def;

  ----------------------------------------------------------------------------

  function acces_ligne_def(E : in etiq) return ligne is
    S       : constant string := acces_string(E);
    existe  :          boolean;
    element :          element_table;
  begin
    chercher_string(detiq, S, false, existe, element);
    if existe then
      return acces_info(element).val_ligne;
    else
      raise MA_etiq_inexistante;
    end if;
  end acces_ligne_def;
   
  ----------------------------------------------------------------------------

  procedure imprimer is 
    procedure traiter (e : in element_table) is
      L : ligne;
    begin
      put("L'etiquette : '"); 
      put(acces_string(acces_chaine(e))); 
      put("'");
      if acces_info(e).definie then
        put(" est definie");
      end if;
      L := acces_info(e).val_ligne;
      if L /= null then
        put(" ligne " & positive'image(acces_num_ligne(L)));
      else
        put(" sans ligne !!");
      end if;
      if acces_info(e).utilisee then
        put(" est utilisee");
      end if;
      new_line;
    end traiter;

    procedure mon_parcours is new parcourir_table(traiter);

  begin -- imprimer
    mon_parcours(detiq);
  end imprimer;

  ----------------------------------------------------------------------------

  procedure verif_defs is
    erreur_defs : boolean := false;

    procedure traiter(e : in element_table) is
    begin
      if not acces_info(e).definie then
        put_line("Etiquette non definie : " & acces_string(acces_chaine(e)));
        erreur_defs := true;
      end if;
    end traiter;

    procedure mon_parcours is new parcourir_table(traiter);

  begin -- verif_defs
    mon_parcours(detiq);
    if erreur_defs then 
      raise MA_etiq_non_definie;
    end if;
  end verif_defs;

  ----------------------------------------------------------------------------

  procedure etiq_non_definies is

    procedure traiter(e : in element_table) is
    begin
      if not acces_info(e).definie then
        put_line("Etiquette non definie : " & acces_string(acces_chaine(e)));
     end if;
    end traiter;

    procedure mon_parcours is new parcourir_table( traiter);

  begin -- etiq_non_definies
    mon_parcours(detiq);
  end etiq_non_definies;

  ----------------------------------------------------------------------------

  procedure etiq_non_utilisees is

    procedure traiter(e : in element_table) is
    begin
     if not acces_info(e).utilisee then
      put_line("* WARNING : etiquette non utilisee -- " &
            acces_string(acces_chaine(e)));
     end if;
    end traiter;

    procedure mon_parcours is new parcourir_table(traiter);

  begin -- etiq_non_utilisees
   mon_parcours(detiq);
  end etiq_non_utilisees;

end MA_Detiq;

