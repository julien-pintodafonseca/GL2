------------------------------------------------------------------------
-- mes_tables.ads : specification du type abstrait table              --
-- Une table permet d'associer des informations a une string          --
-- et de retrouver ces informations.                                  --
--                                                                    --
-- Remarque : c'est quasi le meme fichier que tables.ads              --
--            mais l'adb n'est pas implante par table de hachage,     --
--            pour que les etudiants ne puissent pas le recuperer.    --
--            De plus, on ne met pas les procedures de suppression.   --
--            C'est pourquoi on a un autre paquetage.                 --
--                                                                    --
-- Auteur : X. Nicollin, pompe dans Outils/Prog !                     --
--                                                                    --
-- Date de creation : 05/01/95                                        --
-- Date de derniere modification :                                    --
------------------------------------------------------------------------

with Types_Base ;
use Types_Base ;
generic

   type info is private ;
        ----
   -- L'information associee a une chaine

   info_vide : in info ;
   ---------
   -- La valeur par defaut de l'information associee a une chaine


package Mes_Tables is  

   -- ***** TYPES *****

   -- Le type abstrait table.

   type table is private ;
        -----
   table_vide : constant table ;
   ----------

   -- Un element de la table est un couple (chaine, info).

   type element_table is private ;
        -------------
   element_vide : constant element_table ;
   ------------


   -- ***** CONSTRUCTEURS *****

   function creation_table return table ;
            --------------
   -- Delivre une nouvelle table ;


   -- ***** SELECTEURS-MUTATEURS *****


   procedure chercher_string(T : in table ;
             ---------------
                             S : in string ;
                             a_creer : in boolean ;
                             present : out boolean ;
                             E : out element_table) ;
   -- Recherche la string S dans la table T
   -- si elle y est deja, present := true
   -- sinon, si a_creer=true, un nouvel element E est ajoute dans T
   -- tel que acces_chaine(E)=S et acces_info(E)=info_vide.
   -- Precondition : S possede au moins un caractere.
   -- Leve : erreur_table si S est vide.

   procedure chercher_chaine(T : in table ;
             ---------------
                             C : in chaine ;
                             a_creer : in boolean ;
                             present : out boolean ;
                             E : out element_table) ;
   -- Recherche la chaine C dans la table T
   -- si elle y est deja, present := true
   -- sinon, si a_creer=true, un nouvel element E est ajoute dans T
   -- tel que acces_chaine(E)=acces_chaine(C) et acces_info(E)=info_vide.


   -- ***** SELECTEURS *****


   function acces_chaine(E : element_table) return chaine ;
            ------------
   -- Retourne la chaine associee a l'element de table E.

   function acces_info(E : element_table) return info ;
            ----------
   -- Retourne l'info associee a l'element de table E.


   -- ***** MUTATEURS *****

   procedure changer_info(E : in element_table ; I : in info) ;
             ------------
   -- Modifie l'info associee a l'element de table E.


   -- ***** ITERATEURS *****

   generic

      with procedure traiter(E : in element_table) ;
                     -------
   procedure parcourir_table(T : in table) ;
             ---------------
   -- Appelle traiter sur chacun des elements de la table T.
   -- L'ordre des differents appels a traiter n'est pas defini.


   -- ***** EXCEPTIONS *****

   erreur_table : exception ;
   ------------
   -- Levee par chercher_string

private

   type structure_table ;
   type table is access structure_table ;
   table_vide : constant table := null ;
   type structure_element_table ;
   type element_table is access structure_element_table ;
   element_vide : constant element_table := null ;
  
end Mes_Tables ;
