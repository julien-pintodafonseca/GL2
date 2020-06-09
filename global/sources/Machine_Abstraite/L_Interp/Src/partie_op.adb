------------------------------------------------------------------------
-- partie_op.adb : corps de la "partie operative"                     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 15/10/95                           --
-- Historique                                                         --
--   04/02/2000 : utilisation du paquetage enfant Temps               --
--   08/12/2000 : prise en compte de la modif de Temps                --
------------------------------------------------------------------------

with Ada.Text_IO, Ada.Integer_Text_IO, Assembleur, Types_Base;
use  Ada.Text_IO, Ada.Integer_Text_IO, Assembleur, Types_Base;

with Ada.Unchecked_Deallocation;

with Options; use Options;

pragma Elaborate_All (Options);

with Partie_Op.Temps; use Partie_Op.Temps;
package body Partie_Op is

  ----------------------------------------------------------------------------
  -- Types des valeurs des objets de la machine abstraite

  subtype depl_mem is deplacement;

   type bloc_mem;
   type p_bloc_mem is access bloc_mem;

   type adresse_mem is record
      base : p_bloc_mem;   -- null pour #null
      depl : depl_mem := 0;
   end record;

   adr_null : constant adresse_mem := (base => null, depl => 0);

  type type_val is (typ_indef, typ_entier, typ_flottant,
                    typ_adr_mem, typ_adr_code);

  type valeur(T : type_val := typ_indef) is record
    case T is
      when typ_indef =>
        null;
      when typ_entier =>
        v_entier : entier;
      when typ_flottant =>
        v_flottant : Flot_MA;
      when typ_adr_mem =>
        v_adr_mem : adresse_mem;
      when typ_adr_code =>
        v_adr_code : adresse_code;
    end case;
  end record;

  subtype val_adresse_mem is valeur(typ_adr_mem);

   type bloc_mem is array (depl_mem range <>) of valeur;

   procedure Liberer_Bloc is new Ada.Unchecked_Deallocation
      (Object => bloc_mem, Name => p_bloc_mem);

   function "+" (a : adresse_mem; d : depl_mem) return adresse_mem is
   begin
      return (base => a.base, depl => a.depl + d);
   end "+";

   function "-" (a : adresse_mem; d : depl_mem) return adresse_mem is
   begin
      return (base => a.base, depl => a.depl - d);
   end "-";

  -- "Bits" du registre instruction
  N : boolean := false;
  Z : boolean := false;
  OV : boolean := false;

  type message is access string;

  le_message_err_inst : message;

  lect_pas_finie : boolean := false;

  ecr_a_finir : boolean := false;

  ----------------------------------------------------------------------------
  -- Ce qui concerne les registres GB, LB et banalises

  type val_reg is array(registre) of valeur;

  Reg : val_reg; -- Les registres

  le_GB : val_adresse_mem renames Reg(GB);
  le_LB : val_adresse_mem renames Reg(LB);

  ----------------------------------------------------------------------------
  -- Ce qui concerne la pile et le tas

  taille_pile : constant depl_mem := Options.Taille_Pile;

  subtype adresse_pile is depl_mem range 1 .. taille_pile;

  subtype Pile is bloc_mem (adresse_pile);

  P : p_bloc_mem; -- La pile

  le_SP : val_adresse_mem renames Reg(SP); -- Pointeur de pile

  Max_Alloc : constant Depl_Mem := Options.Taille_Tas;

  Nb_Alloc : Depl_Mem;

  procedure init_mem is
  begin
    -- P := new bloc_mem (adresse_pile);
    P := new Pile;
    for I in P'Range loop
      P (I) := (T => typ_indef);
    end loop;
    Reg := (GB        => (typ_adr_mem, (P, 0)),
            LB        => (typ_adr_mem, (P, 0)),
            SP        => (typ_adr_mem, (P, 0)),
            R0 .. R15 => (T => typ_indef));
    Nb_Alloc := 0;
  end;

   function Mem (A : adresse_mem) return Valeur is
   begin
      return A.base (A.depl);
   end Mem;

   procedure Store (A : in adresse_mem; V : in valeur) is
   begin
      A.base (A.depl) := V;
   end Store;

  ----------------------------------------------------------------------------
  -- Ce qui concerne les codes condition
  -- Ce qui suit devrait etre local a exec_inst...

  function EQ return boolean is
  begin
    return Z;
  end;

  function NE return boolean is
  begin
    return not Z;
  end;

  function GT return boolean is
  begin
    return not Z and then not N;
  end;

  function LT return boolean is
  begin
   return N;
  end;

  function GE return boolean is
  begin
    return not N;
  end;

  function LE return boolean is
  begin
    return Z or else N;
  end;

  procedure pos_CC_ent (V : entier) is
  begin
    N := V < 0;
    Z := V = 0;
  end;

  procedure pos_CC_flottant(V : Flot_MA) is
  begin
    N := V < 0.0;
    Z := V = 0.0;
  end;

  procedure pos_CC_adr_mem (V : adresse_mem) is
  begin
    Z := V.base = null;
    N := not Z;
  end pos_CC_adr_mem;

  procedure pos_CC(V : valeur) is
  begin
    case V.T is
      when typ_entier =>
        pos_CC_ent(V.v_entier);
      when typ_flottant =>
        pos_CC_flottant(V.v_flottant);
      when typ_adr_mem =>
        pos_CC_adr_mem (V.v_adr_mem);
      when others =>
        null;
    end case;
  end;

  ----------------------------------------------------------------------------

  function mess_typ_val (T : type_val) return string is
  begin
    case T is
      when typ_indef    => return "indefini";
      when typ_entier   => return "entier";
      when typ_flottant => return "flottant";
      when typ_adr_mem  => return "adresse memoire";
      when typ_adr_code => return "adresse code";
    end case;
  end;

  procedure erreur(S : in string) is
  begin
    finir_lecture;
    le_message_err_inst := new string'(S);
    raise erreur_exec_inst;
  end erreur;

  function message_erreur_exec_inst return string is
  begin
    return le_message_err_inst.all;
  end;

  ----------------------------------------------------------------------------

  function calc_val(dval : operande) return valeur is separate;
  -- Remarque : en principe, cette procedure devrait etre locale a exec_inst

  function calc_adr_mem (dadr : operande) return adresse_mem is separate;
  -- Remarque : en principe, cette procedure devrait etre locale a exec_inst

  procedure exec_inst is separate;

  ----------------------------------------------------------------------------

  procedure afficher_valeur(V : valeur) is separate;
  -- procedure utilisee par les deux suivantes.

  procedure afficher_val_reg is separate;

  procedure afficher_val_pile(dmin, dmax : deplacement) is separate;

  procedure afficher_val_obj(R : in registre) is separate;

  ----------------------------------------------------------------------------

  procedure init_temps renames Reset;

  function temps_d_exec return string renames Image;

  procedure finir_lecture is
  begin
    if lect_pas_finie then
      if not end_of_line and then Not End_Of_File Then
	skip_line;
      end if;
      lect_pas_finie := false;
    end if;
  end finir_lecture;

  procedure finir_ecriture is
  begin
    if ecr_a_finir then
      new_line;
      ecr_a_finir := false; -- ajoute 15/10/95
    end if;
  end finir_ecriture;

end Partie_Op;
