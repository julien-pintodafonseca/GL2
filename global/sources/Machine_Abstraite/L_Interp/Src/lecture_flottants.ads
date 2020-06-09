------------------------------------------------------------------------
-- lecture_flottants.ads : lecture de flottants machine abstraite     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 28/11/94                                        --
------------------------------------------------------------------------

with Types_Base;
use  Types_Base;

package Lecture_Flottants is

  procedure lire_flottant (x : out Flot_MA);
  -- Lit la representation decimale d'un flottant machine abstraite
  -- eventuellement precede d'un signe '+' ou '-' (sans espace) et le range
  -- dans x.
  --
  -- Le flottant (le signe le cas echeant) peut etre precede d'espaces et/ou de
  -- retour-chariot. La lecture s'arrete sur un espace ou un retour-chariot.
  -- La representation externe du flottant doit tenir sur 80 caracteres.
  -- Sinon, CONSTRAINT_ERROR est levee.
  --
  -- La syntaxe des flottants machine abstraite est la suivante :
  -- CHIFFRE = [0-9]
  -- NUM = {CHIFFRE}+
  -- DEC = {NUM}.{NUM}
  -- EXP = [Ee]([+-]?)
  -- FLOATMA = (DEC) | {DEC}{EXP}(NUM)
  --
  -- CONSTRAINT_ERROR ou NUMERIC_ERROR est levee si le flottant est trop grand.
  -- DATA_ERROR est levee si ce qui est lu n'est pas un flottant.
  -- On n'utilise pas le get de Flottant_ES, car il n'est pas assez restrictif
  -- (par exemple, il accepte les '_')

end Lecture_Flottants;
