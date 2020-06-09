-------------------------------------------------------------------------------
--  Types_Base : spécification du paquetage
--
--  Auteur : un enseignant du projet GL
--  Affiliation : Grenoble INP (Ensimag)
--
--  Historique :
--     01/01/2020
--        - version initiale
-------------------------------------------------------------------------------

--|  Définition des types des littéraux Deca : Entier, Flottant, Chaine
--|  Pour éviter la confusion entre le type prédéfini "string" et
--|  le type abstrait "chaine" dans le terme "chaîne de caractères",
--|  nous utilisons le franglais "string".

package Types_Base is

   -- ***** TYPES *****

   -- Les entiers acceptables dans un programme Deca : 32 bits signés.
   -- On a toujours :
   --    Entier'First = Valmin
   --    Entier'Last  = Valmax

   type Entier is range -(2 ** 31) .. (2 ** 31) - 1;

   Valmin : constant Entier := Entier'First;
   Valmax : constant Entier := Entier'Last;

   -- Type abstrait permettant de gérer facilement
   -- des strings constantes de taille quelconque

   type St_Chaine (<>) is limited private;
   type Chaine is access all St_Chaine;

   -- Les flottants acceptables dans un programme Deca : 32 bits norme IEEE 754
   -- C'est la machine abstraite qui traitera les flottants IEEE754
   -- Dans le compilateur, on gardera en revanche les littéraux flottants comme des chaines.

   type Flot_MA is digits 6 range -16#0.FF_FFFF#e32 ..  16#0.FF_FFFF#e32;

   type Flottant is new Chaine;

   -- ***** CONSTRUCTEUR *****

   function Creation (S : String) return Chaine;
   -- Crée une nouvelle chaine de mêmes caractères que S

   -- ***** SELECTEURS *****

   function Acces_String (C : not null access St_Chaine) return String;
   -- Retourne la string équivalente à C


   -- ***** DIVERS *****

   function Longueur (C : not null access St_Chaine) return Natural;
   -- Retourne le nombre de caractères de C

private

   type St_Chaine is new String;

end Types_Base;
