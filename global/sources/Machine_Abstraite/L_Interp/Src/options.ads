------------------------------------------------------------------------
-- options.ads : interface de recuperation et gestion des options     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 16/10/95                                        --
-- Date de derniere modification :                                    --
------------------------------------------------------------------------

with Types_Base; use Types_Base;

package Options is
  
  function debug return boolean;
  -- vrai si option mise au point

  function aff_stats return boolean;
  -- vrai si option d'affichage des statistiques (temps d'execution
  -- et nombre d'instructions)

  function beautify return boolean;
  -- vrai si option de retour a la ligne apres ecriture

  function Taille_Pile return Entier;
  -- taille de la pile (modifiable par option -p)

  function Taille_Tas return Entier;
  -- taille du tas (modifiable par option -t)

  function Limite_Temps return Entier;
  -- limite temps (modifiable par option -T)

  function Mauvaise_Option return Boolean;
  -- Vrai si pas la bonne option, ou options incompatibles.

  function Must_Exit_Immediately return Boolean;
  -- Vrai si le programme doit quitter immédiatement après le parsing
  -- de la ligne de commande.


end Options;
