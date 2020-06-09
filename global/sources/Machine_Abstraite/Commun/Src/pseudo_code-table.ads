-------------------------------------------------------------------------------
--  Pseudo_Code.Table : specification du paquetage
--
--  Auteur : un enseignant du projet GL
--  Affiliation : Grenoble INP (Ensimag)
--
--  Historique :
--     01/01/2020
--        - version initiale
-------------------------------------------------------------------------------

private package Pseudo_Code.Table is

   pragma Elaborate_Body;

   -- Un element de la table est un couple (Chaine, etiq).

   type St_Element_Table (<>) is limited private;
   type Element_Table is access all St_Element_Table;

   procedure Chercher (S : in String;
                       A_Creer : in Boolean;
                       Present : out Boolean;
                       E : out Element_Table);
   -- Recherche S dans la table
   -- si elle y est deja, Present := True
   -- sinon, si A_Creer = True, un nouvel element E est ajoute
   -- tel que Acces_Chaine (E) = S et Acces_Etiq (E) = null.

   function Acces_Chaine (E : not null access St_Element_Table) return Chaine;
   -- Retourne la chaine associee a E.

   function Acces_Etiq (E : not null access St_Element_Table) return Etiq;
   -- Retourne l'etiq associee a E.

   procedure Changer_Etiq (E : not null access St_Element_Table; I : in Etiq);
   -- Modifie l'etiq associee a l'element de table E.

   Table_Codop : array (Code_Operation) of Chaine;

private

   type St_Cache_El_Table;
   type St_Element_Table is access all St_Cache_El_Table;

end Pseudo_Code.Table;
