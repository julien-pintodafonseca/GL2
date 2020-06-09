-------------------------------------------------------------------------------
--  Entier_ES : spécification du paquetage
--
--  Auteur : un enseignant du projet GL
--  Affiliation : Grenoble INP (Ensimag)
--
--  Historique :
--     01/01/2020
--        - version initiale
-------------------------------------------------------------------------------

--|  Entrées/sorties sur le type de base Entier

with Types_Base, Ada.Text_IO;
use  Types_Base, Ada.Text_IO;

package Entier_ES is new Integer_IO (Entier);
