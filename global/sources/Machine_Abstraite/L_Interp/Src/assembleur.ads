------------------------------------------------------------------------
-- assembleur.ads : interface de l'assembleur de la machine abstraite --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 17/11/94                           --
------------------------------------------------------------------------

with Pseudo_Code, Ada.Text_IO;
use Pseudo_Code, Ada.Text_IO;

package Assembleur is

  max_lg_prog : constant := 500_000;

  procedure assembler_charger(F : in file_type);
  -- Effectue l'assemblage et le ``chargement'' du
  -- fichier assembleur F. Ne positionne pas PC
  -- Peut lever les exceptions erreur_fichier, erreur_syntaxe... 

  function nb_lignes return natural;
  -- Retourne le nombre de lignes du dernier fichier charge

  function nb_insts return natural;
   -- Retourne le nombre d'instructions du dernier fichier charge

  type adresse_code is private;
  -- Le type des adresses d'instructions. 
  -- On doit pouvoir les empiler

  function acces_adresse_code (n : in positive) return adresse_code;

  function contenu_PC return adresse_code;
  -- comme son nom l'indique...

  procedure charger_PC(A : in adresse_code);
  -- Comme son nom l'indique
  -- Leve erreur_PC si pas adresse du programme

  procedure init_PC;
  -- Positionne PC sur la premiere instruction
  -- Leve erreur_PC si pas d'instruction

  procedure incr_PC;
  -- Avance PC a l'instruction suivante
  -- Leve erreur_PC si plus d'instruction

  procedure charger_RI;
  -- charge le registre d'instruction avec l'instruction
  -- pointee par PC

  procedure brancher(E : in etiq);
  -- Positionne PC sur l'instruction etiquetee par E
  -- Leve erreur_PC si pas d'instruction

  function code_op return code_operation;
  -- Retourne le code_op de l'instruction chargee dans RI

  function oper1 return operande;
  -- Retourne le premier operande de l'instruction
  -- Leve erreur_operande si inexistant

  function oper2 return operande;
  -- Retourne le second operande de l'instruction
  -- Leve erreur_operande si inexistant

  procedure ajouter_point(n : in positive);
  -- Ajoute un point d'arret en ligne n

  procedure enlever_point(n : in positive);
  -- Enleve un point d'arret en ligne n

  function est_point return boolean;
  -- Indique si l'instruction contenue dans RI est un point d'arret

  function est_point(n : positive) return boolean;
  -- Indique si la ligne n contient une instruction a un point d'arret

  procedure afficher_instr;
  -- Affiche l'instruction contenue dans RI

  function num_ligne_cour return positive;
  -- Numero de ligne de l'instruction contenue dans RI

  procedure afficher_instr(A : in adresse_code);
  -- Affiche l'instruction situee a l'adress A 

  function num_ligne(A : adresse_code) return positive;
  -- Numero de ligne de l'instruction situee a l'adresse A

  procedure afficher_programme;
  -- Affiche la liste des instructions avec leurs numeros de ligne
  -- du source et les points d'arret

  erreur_PC             : exception;
  erreur_point_d_arret  : exception;
  erreur_inst           : exception renames Pseudo_Code.erreur_inst;
  erreur_syntaxe        : exception;
  erreur_fichier        : exception;
  erreur_lignes         : exception;

private
  type adresse_code is new positive;
end Assembleur;
