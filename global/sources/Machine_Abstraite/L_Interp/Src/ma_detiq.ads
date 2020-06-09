------------------------------------------------------------------------
-- ma_detiq.ads : Dictionnaire d'etiquettes                           --
-- Ce module implemente un dictionnaire permettant d'associer des     --
-- etiquettes a des chaines d'alphanumeriques. Initialement ce        --
-- dictionnaire est vide, c'est-a-dire ne contient aucune association --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 17/11/94                           --
------------------------------------------------------------------------

with Pseudo_Code;
use  Pseudo_Code;

package MA_Detiq is

  procedure ins_def_etiq(S : in string; E : out etiq);
  -- Insere la chaine S dans la table d'etiquettes, en definition.
  -- Rend une etiquette E 
  -- Leve l'exception MA_double_defetiq si elle a deja ete definie

  procedure ins_util_etiq(S : in string; E : out etiq);
  -- Insere la chaine S dans la table d'etiquettes, en utilisation.
  -- Rend une etiquette E

  procedure changer_ligne_def (E : in out etiq; L : in ligne);
  -- Mute la ligne de l'etiquette (pour la faire pointer sur l'instruction
  -- qui la suit)

  function acces_ligne_def(E : in etiq) return ligne;
  -- Rend la ligne de ``definition'' de l'etiquette

  procedure imprimer;
  -- Imprime le dictionnaire dans un ordre quelconque.
  -- Chaque entree est affichee sous la forme :
  -- L'etiquette '...' [est definie] [ligne n] [sans ligne !!] [est utilisee]

  procedure verif_defs;
  -- Verifie que toutes les etiquettes utilisees sont definies
  -- Dans le cas contraire, affiche celles qui sont non definies
  -- et leve MA_etiq_non_definie

  procedure etiq_non_definies;
  -- Affiche les etiquettes non definies

  procedure etiq_non_utilisees;
  -- Affiche les etiquettes non utilisees

  MA_etiq_inexistante, MA_etiq_non_definie, MA_double_defetiq : exception;

  ERREUR_INTERNE_DETIQ : exception;
  -- Exception pour la mise au point, ne devrait jamais etre levee

end MA_Detiq;
