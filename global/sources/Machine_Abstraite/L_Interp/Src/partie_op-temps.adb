------------------------------------------------------------------------
-- partie_op-temps.adb : implantation de la gestion du temps          --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Historique :                                                       --
--   04/02/2000 : creation                                            --
--   08/12/2000 : modif d'interface                                   --
--     Init -> Reset, plus de preconditions                           --
------------------------------------------------------------------------

with Options, Types_Base;
use  Options, Types_Base;

package body Partie_Op.Temps is

   Defensif : constant Boolean := True;

   Cour : Natural := 0;

   Valide : Boolean := True;

   -----------
   -- Image --
   -----------

   function Image return String is
   begin
      if Valide then
         return Natural'Image (Cour);
      else
         return ("### DES EONS ###");
      end if;
   end Image;

   ----------
   -- Incr --
   ----------

   procedure Incr (Val : in Natural) is
   begin
      if Valide then
         Cour := Cour + Val;
      end if;
      if Limite_Temps > 0 and
        Entier(Cour) > Limite_Temps then
         Etat_Ima := Termine;
         raise Limite_Temps_Ecoulee;
      end if;
   exception
      when Constraint_Error =>
         Valide := False;
   end Incr;

   -----------
   -- Reset --
   -----------

   procedure Reset is
   begin
      Cour := 0;
      Valide := True;
   end Reset;

end Partie_Op.Temps;
