------------------------------------------------------------------------
-- lecture_flottants.adb : lecture de flottants machine abstraite     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 28/11/94                                        --
-- Modifications :                                                    --
--   04/02/99 :                                                       --
--      utilsation de 'Value ('Image) pour limiter la representation  --
--                                                                    --
------------------------------------------------------------------------

with Ada.Text_IO, Flottant_ES;
use  Ada.Text_IO, Flottant_ES;

package body Lecture_Flottants is

  S : string(1 .. 80);

  procedure lire_flottant(x : out Flot_MA) is
  -- On s'appuie sur le fait que l'automate est presque trois fois le meme.
    type Etat is (debut, apsigne, apchiffre);
    subtype Chiffre is character range '0' .. '9';
    dansmantisse : boolean := true;
    avantpoint : boolean := true;
    etatcour : Etat := debut;
    carcour : character := ' ';
    indice_dernier : natural := 0;
    pos : positive;
    f: Float;

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

  begin -- lire_flottant
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
            when '.' =>
              if avantpoint then
                indice_dernier := indice_dernier + 1;
                S(indice_dernier) := carcour;
                avantpoint := false;
                etatcour := apsigne;
              else
                raise DATA_ERROR;
              end if;
            when 'E' | 'e' =>
              if dansmantisse then
                dansmantisse := false;
                indice_dernier := indice_dernier + 1;
                S(indice_dernier) := carcour;
                etatcour := debut;
              else
                raise DATA_ERROR;
              end if;
            when others =>
              raise DATA_ERROR;
          end case;
      end case;
      carsuiv2;
    end loop;
    -- passer_espaces2;
    -- if end_of_line then
      -- skip_line;
    -- end if;
    -- A la fin, l'etat doit etre apchiffre,
    -- et avantpoint doit etre faux
    if etatcour = apchiffre and not avantpoint then
      begin
        get(S(1 .. indice_dernier), f, pos);
        x := Flot_MA(f);
      exception
        when DATA_ERROR => raise CONSTRAINT_ERROR;
      end;
    else
      raise DATA_ERROR;
    end if;
  end lire_flottant;

end Lecture_Flottants;
