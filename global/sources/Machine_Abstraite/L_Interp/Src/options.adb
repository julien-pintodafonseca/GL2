------------------------------------------------------------------------
-- options.adb : implantation de la gestion des options               --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 16/10/95                                        --
-- Date de derniere modification :                                    --
------------------------------------------------------------------------

with Ada.Command_Line, Ada.Text_IO;
use  Ada.Command_Line, Ada.Text_IO;

with Config_Machine; use Config_Machine;

package body Options is
  
  stats : boolean := false; -- affichage des stats
  deb : boolean := false; -- mise au point
  ret : boolean := false; -- retour chariot

  TP : Natural := Defaut_Taille_Pile; -- taille de pile
  TT : Natural := Defaut_Taille_Tas; -- taille de tas
  LT : Natural := 0; -- Limite en temps d'execution.

  procedure Display_Version is
  begin
     -- when updating this, also update gl-check-config.sh
     Put_Line("ima 2020a");
  end;

  function debug return boolean is
  begin
    return deb;
  end debug;

  function aff_stats return boolean is
  begin
    return stats;
  end aff_stats;

  function beautify return boolean is
  begin
    return ret;
  end beautify;

   function Taille_Pile return Entier is
   begin
      return Entier (TP);
   end Taille_Pile;

   function Taille_Tas return Entier is
   begin
      return Entier (TT);
   end Taille_Tas;

   function Limite_Temps Return Entier is
   begin
      return Entier (LT);
   end Limite_Temps;

   Err_Option : Boolean := False;
   Exit_Immediate : Boolean := False;

   function Mauvaise_Option return Boolean is
   begin
      return Err_Option;
   end Mauvaise_Option;

   function Must_Exit_Immediately return Boolean is
   begin
      return Exit_Immediate;
   end Must_Exit_Immediately;

    i1, i2 : integer;
    mauv_opt : exception;
    i : Positive := 1;

begin
    if Argument_Count = 0 then
      raise mauv_opt;
    end if;
    while i <= Argument_Count loop
      i1 := Argument(i)'first;
      i2 := Argument(i)'last;
      if Argument (i)(i1) /= '-' then
         if I < Argument_Count then
            Put_Line ("Expected an option here, found " & Argument (i));
            raise mauv_opt;
         end if;
      else
         if i2-i1 /= 1 then
            if i2-i1 >= 4 then
               exit when
                  Argument(i)(i2-3) = '.' and
                  Argument(i)(i2-2) = 'a' and
                  Argument(i)(i2-1) = 's' and
                  Argument(i)(i2  ) = 's';
               end if;
            Put_Line ("Long options not accepted: " & Argument(I));
            raise mauv_opt;
         end if;
         case Argument (i) (i2) is
            when 'v' => Display_Version;
               Exit_Immediate := True;
            when 'd' => deb := true;
            when 's' => stats := true;
            when 'r' => ret := true;
            when 'p' =>
               i := i + 1;
               if i = Argument_Count then
                  raise mauv_opt;
               end if;   
               begin
                  TP := Natural'Value (Argument (i));
               exception
                  when Constraint_Error =>
                     raise mauv_opt;
               end;
            when 't' =>
               i := i + 1;
               if i = Argument_Count then
                  raise mauv_opt;
               end if;   
               begin
                  TT := Natural'Value (Argument (i));
               exception
                  when Constraint_Error =>
                     raise mauv_opt;
               end;
            when 'T' =>
               i := i + 1;
               if i = Argument_Count then
                  raise mauv_opt;
               end if;
               begin
                  LT := Natural'Value (Argument (i));
               exception
                  when Constraint_Error =>
                     raise mauv_opt;
               end;
            when others =>
               raise mauv_opt;
         end case;
      end if;
      i := i + 1;
    end loop;
  exception
    when mauv_opt =>
      Put_Line ("Usage : " & Command_Name & " [options] fichier_assembleur");
      Put_Line ("   options (les 3 premieres sont exclusives) :");
      Put_Line ("     -d : appel du metteur au point");
      Put_Line ("     -s : execution avec affichage de statistiques");
      Put_Line ("     -r : execution avec passage a la ligne apres ecriture");
      Put_Line ("     -p nnn : (nnn entier >= 0) nombre de mots de la pile");
      Put_Line ("     -t nnn : (nnn entier >= 0) nombre de mots du tas");
      Put_Line ("     -T nnn : (nnn entier >= 0) limite de temps d'execution");
      Err_Option := true;
end Options;
