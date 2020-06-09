------------------------------------------------------------------------
-- lecture_entiers.adb : lecture d'entiers machine abstraite          --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 28/11/94                           --
--  * suppression de skip_line en fin de lecture                      --
--  * suppression des '_',  pas de signe apres E ou e                 --
------------------------------------------------------------------------

with Ada.Text_IO, Entier_ES;
use  Ada.Text_IO, Entier_ES;

package body LECTURE_ENTIERS is

  S : string(1 .. 80);

  procedure lire_entier(x : out entier) is
  -- On s'appuie sur le fait que l'automate est deux fois le meme.
    type Etat is (debut, apsigne, apchiffre);
    subtype Chiffre is character range '0' .. '9';
    dansmantisse : boolean := true;
    etatcour : Etat := debut;
    carcour : character := ' ';
    indice_dernier : natural := 0;
    pos : positive;

    --------------------------------------------------------------------------

    procedure carsuiv1 is
    begin
      if end_of_line then
        carcour := ' ';
        skip_line;
      else
        get(carcour);
      end if;
    end carsuiv1;

    --------------------------------------------------------------------------

    procedure carsuiv2 is
    -- idem carsuiv1, mais on ne fait pas skip_line sur end_of_line.
    begin
      if end_of_line then
        carcour := ' ';
      else
        get(carcour);
      end if;
    end carsuiv2;

    --------------------------------------------------------------------------

    procedure passer_espaces1 is
    begin
      while carcour = ' ' loop
        carsuiv1;
      end loop;
    end passer_espaces1;

    --------------------------------------------------------------------------

    -- procedure passer_espaces2 is
    -- begin
    --   while not end_of_line and carcour = ' ' loop
    --     get(carcour);
    --   end loop;
    -- end passer_espaces2;

    --------------------------------------------------------------------------

  begin -- lire_entier
    passer_espaces1;
    -- Boucle de lecture
    -- Ca doit se terminer par un espace ou une fin de ligne
    while carcour /= ' ' loop
      case etatcour is
        when debut =>
          case carcour is
            when '+' | '-' =>
              indice_dernier := indice_dernier + 1;
              S(indice_dernier) := carcour;
              etatcour := apsigne;
            when Chiffre =>
              indice_dernier := indice_dernier + 1;
              S(indice_dernier) := carcour;
              etatcour := apchiffre;
            when others =>
              raise DATA_ERROR;
          end case;
        when apsigne => 
          case carcour is
            when Chiffre => 
              indice_dernier := indice_dernier + 1;
              S(indice_dernier) := carcour;
              etatcour := apchiffre;
            when others =>
              raise DATA_ERROR;
          end case;
        when apchiffre => 
          case carcour is
            when Chiffre =>
              indice_dernier := indice_dernier + 1;
              S(indice_dernier) := carcour;
            when 'e' | 'E' =>
              if dansmantisse then
                dansmantisse := false;
                indice_dernier := indice_dernier + 1;
                S(indice_dernier) := carcour;
                etatcour := apsigne;
              else
                raise DATA_ERROR;
              end if;
            when others =>
              raise DATA_ERROR;
          end case;
        when others =>
          raise DATA_ERROR;
      end case;
      carsuiv2;
    end loop;
    -- passer_espaces2;
    -- if end_of_line then
      -- skip_line;
    -- end if;
    -- A la fin, l'etat doit etre apchiffre 
    if etatcour = apchiffre then
      begin
        get(S(1 .. indice_dernier), x, pos);
      exception
        when DATA_ERROR => raise CONSTRAINT_ERROR;
      end;
    else
      raise DATA_ERROR;
    end if;
  end lire_entier;

end Lecture_Entiers;
