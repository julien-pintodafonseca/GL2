------------------------------------------------------------------------
-- partie_op-aff_val.adb : affichage de valenurs                      --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 21/11/94                           --
------------------------------------------------------------------------
with Entier_ES, Flottant_ES;
use Entier_ES, Flottant_ES;
separate(Partie_Op)
procedure afficher_valeur(V : valeur) is
begin
  case V.T is
    when typ_indef =>
      put ("<indefini>");
    when  typ_entier =>
      put(V.v_entier, 1);
    when typ_flottant =>
      put(Float(V.v_flottant));
    when typ_adr_mem =>
      if V.v_adr_mem.base = null then
         put ("null");
      elsif V.v_adr_mem.base = P then
         put("@ zpile : ");
         put(entier(V.v_adr_mem.depl), 1);
         put("(GB) , ");
         put(entier(V.v_adr_mem.depl - le_LB.v_adr_mem.depl), 1);
         put("(LB)");
      else
         put("@ bloc (taille ");
         put(entier(V.v_adr_mem.base'length), 1);
         put(") depl ");
         put(entier(V.v_adr_mem.depl), 1);
      end if;
    when typ_adr_code =>
      put("@ code ligne ");
      put(num_ligne(V.v_adr_code), 1);
      -- put(" : ");
      -- afficher_instr(V.v_adr_code);
    when others =>
      put("ERREUR VALEUR REGISTRE");
  end case;
end;
