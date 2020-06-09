------------------------------------------------------------------------
-- partie_op-temps.ads : specification de la gestion du temps        --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Historique :                                                       --
--   04/02/2000 : creation                                            --
--   08/12/2000 : modif d'interface                                   --
--     Init -> Reset, plus de preconditions                           --
------------------------------------------------------------------------

private package Partie_Op.Temps is

  procedure Reset;
  -- remet a zero le temps d'execution

  procedure Incr (Val : in Natural);
  -- incremente le temps d'execution

  function Image return String;
  -- retourne une image du temps d'execution
  -- retourne "### DES EONS ###" si incalculable

end Partie_Op.Temps;
