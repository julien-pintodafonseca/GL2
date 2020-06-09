-------------------------------------------------------------------------------
--  Pseudo_Code.Table : corps du paquetage
--
--  Auteur : un enseignant du projet GL
--  Affiliation : Grenoble INP (Ensimag)
--
--  Historique :
--     01/01/2020
--        - version initiale
-------------------------------------------------------------------------------

with Ada.Text_IO, Ada.Characters.Handling;
use  Ada.Text_IO, Ada.Characters.Handling;

with Types_Base;
pragma Elaborate_All (Types_Base);

package body Pseudo_Code.Table is

   type St_Cache_El_Table is
      record
         C : Chaine;              -- la chaine associee a l'entree
         I : Etiq;                -- l'etiq associee
         Gauche : Element_Table;  -- le sous-arbre gauche
         Droit : Element_Table;   -- le sous-arbre droit
      end record;

   type Table_Etiq is
      record
         Racine : Element_Table := null;
      end record;

   T : Table_Etiq;

   procedure Mettre_Au_Point (Niveau : in Positive; S : in String) is
      -- plus Niveau_Max est eleve, plus il y a d'affichage
      Niveau_Max : constant Natural := 0;
   begin
      if Niveau <= Niveau_Max then
         Put_Line ("Pseudo_Code.Table : " & S);
      end if;
   end Mettre_Au_Point;

   procedure Chercher (S : in String;
                       A_Creer : in Boolean;
                       Present : out Boolean;
                       E : out Element_Table) is
      Pere, Cour, Nouveau : Element_Table;
      Existe : Boolean;
      M : String := To_Lower (S);

   begin
      Mettre_Au_Point (1, "recherche de la string " & S);
      Pere := null;
      Cour := T.Racine;
      while (Cour /= null) and then (M /= Acces_String (Cour.all.C)) loop
         Pere := Cour;
         if M < Acces_String (Cour.all.C) then
            Cour := Cour.all.Gauche;
         else
            Cour := Cour.all.Droit;
         end if;
      end loop;
      if Cour = null then
         Existe := False;
         if A_Creer then
            Nouveau :=
               new St_Element_Table'
                  (new St_Cache_El_Table' (Creation (M), null, null, null));
            if Pere = null then
               T.Racine := Nouveau;
            elsif M < Acces_String (Pere.all.C) then
               Pere.all.Gauche := Nouveau;
            else
               Pere.all.Droit := Nouveau;
            end if;
            E := Nouveau;
         else
            E := null;
         end if;
      else
         Existe := True;
         E := Cour;
      end if;
         Present := Existe;
         if Existe then
            Mettre_Au_Point (1, "        ... string trouvee");
         else
            Mettre_Au_Point (1, "        ... string non trouvee");
         end if;
   end Chercher;

   function Acces_Chaine (E : not null access St_Element_Table) return Chaine is
   begin
      return E.all.C;
   end Acces_Chaine;

   function Acces_Etiq (E : not null access St_Element_Table) return Etiq is
   begin
      return E.all.I;
   end Acces_Etiq;

   procedure Changer_Etiq (E : not null access St_Element_Table; I : in Etiq) is
   begin
      E.all.I := I;
   end Changer_Etiq;

   --  Initialisation des noms reserves


   procedure Ranger_Nom (S : in String; C : out Chaine) is
      Existe : Boolean;
      E : Element_Table;
   begin
      Chercher (S, True, Existe, E);
      Changer_Etiq (E, new St_Etiq' (True, Creation (S), null));
      C := Acces_Etiq (E).Nom;
   end Ranger_Nom;

   procedure Ranger_Nom_Registre (S : in String) is
      C : Chaine;
   begin
      Ranger_Nom (S, C);
   end Ranger_Nom_Registre;

   procedure Ranger_Nom_Codop (Code : in Code_Operation; S : in String) is
      C : Chaine;
   begin
      Ranger_Nom (S, C);
      Table_Codop (Code) := C;
   end Ranger_Nom_Codop;


begin
   for R in Registre loop
      Ranger_Nom_Registre (Registre'Image (R));
   end loop;

   for I in Code_Operation loop
      declare
         S : constant String := Code_Operation'Image (I);
      begin
         -- S est de la forme : "CODE_HALT"
         Ranger_Nom_Codop (I, S (S'First + 5 .. S'Last));
      end;
   end loop;

end Pseudo_Code.Table;
