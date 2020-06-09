------------------------------------------------------------------------
-- assembleur.adb : corps du paquetage Assembleur                     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 27/11/94                           --
--   acces_nom --> acces_string (changement dans pseudo_code.ads)     --
------------------------------------------------------------------------

with MA_Detiq, Ada.Integer_Text_IO, MA_Syntax;
use MA_Detiq, Ada.Integer_Text_IO, MA_Syntax;

package body Assembleur is
  debug : constant boolean := false;
  
  subtype nb_lig is natural range 0 .. max_lg_prog;
  subtype num_lig is positive range 1 .. max_lg_prog;


  type point_arret is record
    lig : ligne;
    stop : boolean := false;
  end record;

  type table_points is array(num_lig range <>) of point_arret;

  type Points_D_Arret is access table_points;

  P : Points_d_arret;

  L : ligne := null;
  nbl : nb_lig;
  nbi : natural;

  -- Lcour : ligne;

  PC : ligne;
  RI : ligne; -- le registre instruction 
              -- (en fait une ligne pour pouvoir avoir son numero)
  IRI : inst; -- l'instruction du RI

  -----------------------------------------------------------------------------

  function acces_adresse_code (n : in positive) return adresse_code is
  begin
    return adresse_code (n);
  end acces_adresse_code;

  -----------------------------------------------------------------------------

  procedure initialiser_table_points(n : in nb_lig := 0) is
  begin
    P := new table_points (1 .. n);
  end;

  -----------------------------------------------------------------------------

  procedure positionner_instruction(L : in ligne) is
  begin
    P (acces_num_ligne(L)).lig := L;
  end;

  -----------------------------------------------------------------------------

  procedure ajouter_point(n : in positive) is
    i : positive := n;
  begin
    while P (i).lig = null loop
      i := i + 1;
    end loop;
    if debug then
      put_line(" Instruction trouvee en ligne " & positive'image(i));
    end if;
    P (i).stop := true;
  exception
    when constraint_error => 
      raise erreur_point_d_arret;
  end;

  -----------------------------------------------------------------------------

  procedure enlever_point(n : in positive) is
    i : positive := n;
  begin
    while P (i).lig = null loop
      i := i + 1;
    end loop;
    if debug then
      put_line(" Point trouve en ligne " & positive'image(i));
    end if;
    P (i).stop := false;
  exception
    when constraint_error => 
      raise erreur_point_d_arret;
  end;

  -----------------------------------------------------------------------------

  function est_point return boolean is
  begin
    return P (acces_num_ligne(RI)).stop;
  exception
    when constraint_error => 
      raise erreur_point_d_arret;
      return false;
  end;

  -----------------------------------------------------------------------------

  function est_point(n : positive) return boolean is
  begin
    return P (n).stop;
  exception
    when constraint_error => 
      raise erreur_point_d_arret;
      return false;
  end;

  -----------------------------------------------------------------------------

  procedure afficher_instr is
  begin
    afficher(IRI);
  exception
    when constraint_error | erreur_ligne => 
      raise erreur_PC;
  end;

  -----------------------------------------------------------------------------

  function num_ligne_cour return positive is
  begin
    return acces_num_ligne(RI);
  exception
    when constraint_error | erreur_ligne =>
      raise erreur_PC;
  end;

  -----------------------------------------------------------------------------

  procedure afficher_instr(A : in adresse_code) is
  begin
    afficher(acces_inst(P (positive(A)).lig));
  exception
    when constraint_error =>
      raise erreur_inst;
  end;

  -----------------------------------------------------------------------------

  function num_ligne(A : adresse_code) return positive is
  begin
    return positive(A);
  end;

  -----------------------------------------------------------------------------

  procedure afficher_programme is
  begin
    for i in P'Range loop
      if P (i).lig /= null then 
        put(i, width => 3);
        if P (i).stop then
          put( " ** ");
        else
          put( "    ");
        end if;
        if debug then
          put_line("Affichage d'une ligne");
        end if;
        afficher(P (i).lig);
      end if;
    end loop;
  end afficher_programme;

  -----------------------------------------------------------------------------

  function acces_inst_suiv(L : in ligne) return ligne is
    -- Precondition : L n'est pas vide
    L1 : ligne := acces_suivant(L);
  begin
    if debug then
      put_line("entree de ACCES_INST_SUIV");
    end if;
    while L1 /= null loop
      if debug then
        afficher(L1);
      end if;
      if acces_inst(L1) /= null then
        if debug then
          put_line("sortie de ACCES_INST_SUIV");
        end if;
        return L1;
      end if;
      L1 := acces_suivant(L1);
    end loop;
    if debug then
      put_line("sortie de ACCES_INST_SUIV, fin du texte");
    end if;
    return L1;
  end;

  -----------------------------------------------------------------------------

  procedure traiter_liste is
    Lcour : ligne := L;
    Linst, Linstsuiv : ligne;
    E : etiq;
  begin
    Lcour := L;
    if acces_inst(L) = null then
      Linst := acces_inst_suiv(L);
      L := Linst;
    else 
      Linst := L;
    end if;
    -- L et Linst contiennent la premiere instruction effective
    -- Lcour est la premiere ligne 
    if L = null then
      put_line("Pas d'instructions !!");
      raise erreur_syntaxe;
    end if;
    if debug then
      put_line("Premiere instruction : ");
      afficher(L);
    end if;
    loop
      -- On redirige les etiquettes entre Lcour et Linst sur Linst
      -- Remarque : si Linst est vide, ca met toutes les lignes
      -- des etiquettes de fin de texte a vide.
      if debug then
        put_line(" parcours avec LCOUR ");
      end if;
      while Lcour /= Linst loop
          if debug then
            afficher(Lcour);
          end if;
        E := acces_etiq(Lcour);
        if E /= null then
          if debug then
            put_line("trouve une etiquette");
            afficher(Lcour);
          end if;
          changer_ligne_def(E, Linst);
          if debug then
            put_line("On la met a ");
            if acces_ligne(E) = null then
              put_line("La ligne_vide");
            else
              afficher(acces_ligne(E));
            end if;
          end if;
        end if;
        Lcour := acces_suivant(Lcour);
      end loop;
      if debug then
        put_line(" fin du parcours ");
      end if;
    exit when Linst = null;
      positionner_instruction(Linst);
      -- on passe a l'instruction suivante
      Lcour := acces_suivant(Linst);
      -- on cherche l'instruction suivant Linst
      Linstsuiv := acces_inst_suiv(Linst);
      -- on indique que c'est la suivante de Linst
      changer_suivant(Linst, Linstsuiv);
      Linst := Linstsuiv;
    end loop;
    -- on a traite toutes les instructions
    -- et toutes les etiquettes
  end traiter_liste;

  -----------------------------------------------------------------------------

  procedure assembler_charger(F : in file_type) is
  begin
    if debug then
      put_line("entree de ASSEMBLER_CHARGER");
    end if;
    set_input(F);
    if debug then
      put_line("PHASE D'ANALYSE");
    end if;
    begin
      analyser_construire_liste_lignes(L,nbl, nbi);
    exception
      when constraint_error =>
        raise erreur_lignes;
    end;
    set_input(standard_input);
    verif_defs;
    if debug then
      put_line("PHASE D'ASSEMBLAGE");
    end if;
    if L = null then 
      put_line("Programme vide !!");
      raise erreur_syntaxe;
    end if;
    initialiser_table_points(nbl);
    traiter_liste;
    if debug then
      put_line("sortie de ASSEMBLER_CHARGER");
    end if;
  exception
    when MA_erreur_lexicale | MA_erreur_conversion 
       | MA_erreur_syntaxe | MA_etiq_non_definie 
       | MA_etiq_inexistante | MA_double_defetiq => 
      raise erreur_syntaxe;
  end;

  -----------------------------------------------------------------------------

  function nb_lignes return natural is
  begin
    return nbl;
  end;

  -----------------------------------------------------------------------------

  function nb_insts return natural is
  begin
    return nbi;
  end;

  -----------------------------------------------------------------------------

  procedure init_PC is
  begin
    if L = null then 
      put_line("Assemblez d'abord le programme !!!");
      raise erreur_PC;
    end if;
    PC := L;
  end;

  -----------------------------------------------------------------------------

  procedure incr_PC is
  begin
    PC := acces_suivant(PC);
  exception
    when constraint_error | erreur_ligne => 
      raise erreur_PC;
  end;

  -----------------------------------------------------------------------------

  procedure brancher (E : in etiq) is
  begin
    PC := acces_ligne_def(E);
    if PC = null then
      put_line("Pas d'instruction apres l'etiquette " &
               acces_string(E));
      raise erreur_PC;
    end if;
  exception
    when erreur_inst =>
      put_line("ERREUR INTERNE IMA : Probleme dans instruction etiquetee");
      raise erreur_inst;
  end;

  -----------------------------------------------------------------------------

  function code_op return code_operation is
  begin
    return acces_code_operation(IRI);
  exception
    when erreur_inst =>
      put_line("ERREUR INTERNE IMA : Probleme dans code_op");
      raise erreur_inst;
  end;

  -----------------------------------------------------------------------------

  function oper1 return operande is
  begin
    return acces_op1(IRI);
  exception
    when erreur_inst =>
      put_line("ERREUR INTERNE IMA : Probleme dans oper1");
      raise erreur_inst;
  end;

  -----------------------------------------------------------------------------

  function oper2 return operande is
  begin
    return acces_op2(IRI);
  exception
    when erreur_inst =>
      put_line("ERREUR INTERNE IMA : Probleme dans oper2");
      raise erreur_inst;
  end;
  
  -----------------------------------------------------------------------------

  function  contenu_PC return adresse_code is
  begin
    return adresse_code(acces_num_ligne(PC));
  end;

  -----------------------------------------------------------------------------

  procedure charger_PC(A : in adresse_code) is
  begin
    PC := P (positive(A)).lig;
  exception
    when constraint_error => raise erreur_PC;
  end;

  -----------------------------------------------------------------------------

  procedure charger_RI is
  begin
    RI := PC;
    IRI := acces_inst(RI);
  exception
    when constraint_error | erreur_ligne =>
      raise erreur_PC;
  end;

end Assembleur;
