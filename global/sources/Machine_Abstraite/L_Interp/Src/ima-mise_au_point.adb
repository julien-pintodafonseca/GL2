------------------------------------------------------------------------
-- ima-mise_au_point.adb : partie metteur au point de ima             --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 12/12/94                                        --
------------------------------------------------------------------------

with Types_Base, Lecture_Entiers, Pseudo_Code, Ada.Io_Exceptions;
use  Types_Base, Lecture_Entiers, Pseudo_Code;

separate(ima)
procedure mise_au_point is

  invite : constant string := "(ima) "; -- prompt du metteur au point
  comm : character; -- commande entree
  lg_deb, lg_fin,             -- intervalle de numeros de lignes (pour 'l')
  pt              : positive; -- numero de ligne de point d'arret

  ---------------------------------------------------------------------------

  procedure lire (comm : out character) is
  -- passe les espaces et retour-chariot, et rend le premier caractere
  -- qui n'en est pas un dans comm
    c : character;
  begin
    loop
      while end_of_line loop
        put(invite);
        skip_line;
      end loop;
      get(c);
    exit when c /= ' ';
    end loop;
    skip_line;
    comm := c;
  exception
     when Ada.Io_Exceptions.End_Error =>
        Comm := 'q';
  end lire;

begin
  demarrer;
  menu;
  loop
    put(invite);
    lire(comm);
    case comm is

      when 'a' =>
        begin
          put("Entrez la ligne du point d'arret : ");
          get(pt); 
          ajouter_point(pt);
          skip_line;
        exception
          when constraint_error | numeric_error | data_error |
               erreur_point_d_arret => 
            skip_line;
            put_line("IMA -- Mauvais point d'arret !!");
        end;

      when 'e' =>
        begin
          put("Entrez la ligne du point d'arret : ");
          get(pt); 
          enlever_point(pt);
          skip_line;
        exception
          when constraint_error | numeric_error | data_error |
               erreur_point_d_arret => 
            skip_line;
            set_col(3);
            put_line("IMA -- Mauvais point d'arret !!");
        end;

      when 'd' =>
        begin
          demarrer;
          no_cont := false;
          exec_jusqu_a_point;
        exception
          when fin_du_programme =>
            finir_lecture;
            put_line("IMA -- Fin du programme (normale). " &
                     "Temps d'execution : " &
                     temps_d_exec);
          when Limite_Temps_Ecoulee =>
            finir_lecture;
            put_line("IMA -- Fin du programme : Limite de temps atteinte");
        end;

      when 'c' =>
        begin
          if no_cont then
           raise pas_de_cont;
          end if;
          if etat_ima /= Tourne then
            raise fin_du_programme;
          end if;
          begin
            exec_inst;
            charger_RI;
            exec_jusqu_a_point;
          exception
            when fin_du_programme =>
              finir_lecture;
              put_line("IMA -- Fin normale du programme. " &
                       "Temps d'execution : " &
                       temps_d_exec);
          end;
        exception
          when erreur_exec_inst =>
            set_col(3);
            put_line("IMA ** ERREUR ** Ligne" & 
                     positive'image(num_ligne_cour) & " : ");
            set_col(5);
            put_line(message_erreur_exec_inst);
            no_cont := true;
          when erreur_ligne | erreur_PC =>
            set_col(3);
            put_line("** IMA ** ERREUR ** Plus d'instructions !!");
            no_cont := true;
          when fin_du_programme => 
            set_col(3);
            put_line("IMA -- Programme termine !");
          when pas_de_cont =>
            set_col(3);
            put_line("IMA -- Reinitialisez l'execution");
        end;

      when 's' =>
        demarrer;
        no_cont := false;
        -- set_col(1);
        set_col(35+col);
        put(num_ligne_cour, 1);
        put(" : ");
        afficher_instr;
        new_line;

      when 'x' =>
        begin
          if no_cont then
            raise pas_de_cont;
          end if;
          if etat_ima /= Tourne then
            raise fin_du_programme;
          end if;
          begin
            exec_inst;
            finir_lecture;
            finir_ecriture;
            charger_RI;
            -- set_col(1);
            set_col(35+col);
            put(num_ligne_cour, 1);
            put(" : ");
            afficher_instr;
            new_line;
          exception
            when fin_du_programme =>
              -- set_col(3);
              put_line("IMA -- Fin normale du programme. " &
                       "Temps d'execution : " &
                       temps_d_exec);
          end;
        exception
          when erreur_exec_inst =>
            set_col(3);
              put_line("IMA ** ERREUR ** Ligne" &
                       positive'image(num_ligne_cour) & " : ");
              set_col(5);
              put_line(message_erreur_exec_inst);
            no_cont := true;
          when erreur_ligne | erreur_PC =>
            set_col(3);
              put_line("** IMA ** ERREUR ** Plus d'instructions !!");
            no_cont := true;
          when fin_du_programme => 
            set_col(3);
            put_line("IMA -- Programme termine !");
          when pas_de_cont =>
            set_col(3);
            put_line("IMA -- Reinitialisez l'execution");
        end;

      when 'i' =>
        begin
          if no_cont then
            raise pas_de_cont;
          end if;
          if etat_ima /= Tourne then
            raise fin_du_programme;
          end if;
          -- set_col(1);
          set_col(35+col);
          put(num_ligne_cour, 1);
          put(" : ");
          afficher_instr;
          new_line;
        exception
          when erreur_ligne | erreur_PC =>
              set_col(3);
              put_line("** IMA ** ERREUR ** Plus d'instructions !!");
            no_cont := true;
          when fin_du_programme =>
            set_col(3);
            put_line("IMA -- Programme termine !");
          when pas_de_cont =>
            set_col(3);
            put_line("IMA -- Reinitialisez l'execution");
        end;

      when 'r' =>
        afficher_val_reg;

      when 'p' =>
        begin
          affich_prog(1, nb_lignes);
        exception
          when constraint_error | numeric_error =>
            set_col(3);
            put_line("IMA -- Mauvais numero de ligne ");
        end;

      when 'l' =>
        begin
          put("Premiere ligne (entre 1 et" &
              natural'image(nb_lignes) & ") : ");
          get(lg_deb);
          if lg_deb < 1 or lg_deb > nb_lignes then
            raise numeric_error;
          end if;
          skip_line;
          put("Derniere ligne (entre" &
              positive'image(lg_deb)& " et" &
              natural'image(nb_lignes) & ") : ");
          get(lg_fin);
          if lg_fin < lg_deb or lg_fin > nb_lignes then
            raise numeric_error;
          end if;
          skip_line;
          affich_prog(lg_deb, lg_fin);
        exception 
          when constraint_error | numeric_error | data_error =>
            skip_line;
            set_col(3);
            put_line("IMA -- Mauvais numero de ligne ");
        end;

      when 'm' =>
        declare
          dmin, dmax : deplacement;
        begin
          put("Deplacement min par rapport a GB (> 0) : ");
          lire_entier(dmin);
          skip_line;
          put("Deplacement max par rapport a GB (>" &
              deplacement'image(dmin-1) & ") : ");
          lire_entier(dmax);
          skip_line;
          afficher_val_pile(dmin, dmax);
        exception
          when constraint_error | numeric_error | data_error =>
            skip_line;
            set_col(3);
            put_line("IMA -- Ce n'est pas un deplacement !!");
          when erreur_exec_inst =>
            set_col(3);
            put_line("IMA -- Adresse en dehors de la pile !!");
        end;

      when 'b' =>
         declare
            num : entier;
            R : banalise;
         begin
            put ("Numero de registre (entre 0 et" &
                 Integer'Image (Banalise'Pos (Banalise'Last) -
                                Banalise'Pos (Banalise'First)) & ") : ");
            lire_entier (num);
            skip_line;
            R := Banalise'Val (Integer (num) +
                               Banalise'Pos (Banalise'First));
            afficher_val_obj (R);
         exception 
          when constraint_error | numeric_error | data_error =>
            set_col(3);
            put_line("IMA -- Ce n'est pas un numero de registre !!");
          when erreur_exec_inst =>
            set_col(3);
            put_line
               ("IMA -- Le registre ne contient pas une (base d') adresse de bloc !!");
        end;

      when 't' =>
        begin
          if no_cont then
            raise pas_de_cont;
          end if;
          put_line("Temps d'execution : " & temps_d_exec);
        exception
          when pas_de_cont =>
            set_col(3);
            put_line("IMA -- Reinitialisez l'execution");
        end;

      when '?' | 'h' => 
        menu;

      when 'q' => 
        exit;

      when others =>
        set_col(3);
        put_line("IMA -- Commande inconnue !!");
    end case;
  end loop;
  close(F);
end mise_au_point;
