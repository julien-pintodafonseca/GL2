------------------------------------------------------------------------
-- lecture_entiers.ads : lecture d'entiers machine abstraite          --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 21/11/94                           --
------------------------------------------------------------------------

with Types_Base;
use  Types_Base;

package Lecture_Entiers is

  procedure lire_entier(x : out entier);
  -- Lit la representation decimale d'un entier machine abstraite
  -- eventuellement precede d'un signe '+' ou '-' (sans espace) et le range
  -- dans x.
  -- 
  -- L'entier (le signe le cas echeant) peut etre precede d'espaces et/ou de
  -- retour-chariot. La lecture s'arrete sur un espace ou un retour-chariot.
  -- La representation externe de l'entier doit tenir sur 80 caracteres.
  -- Sinon, CONSTRAINT_ERROR est levee.
  --
  -- La syntaxe des entiers machine abstraite est la suivante :
  -- CHIFFRE = [0-9]
  -- NUM = {CHIFFRE}+
  -- ENTMA = (NUM) | {NUM}E(NUM) | {NUM}e{NUM}
  --
  -- CONSTRAINT_ERROR ou NUMERIC_ERROR est levee si l'entier est trop grand.
  -- DATA_ERROR est levee si ce qui est lu n'est pas un entier.
  -- On procede caractere par caractere, car le get de Entier_ES
  -- leve DATA_ERROR si ce qui est lu est trop grand pour etre un entier.
  -- Et puis de toutes facons le get de Entier_ES n'est pas assez restrictif
  -- (accepte les '_' par exemple)

end Lecture_Entiers;
