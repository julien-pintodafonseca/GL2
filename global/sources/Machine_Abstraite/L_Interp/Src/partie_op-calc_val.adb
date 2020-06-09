------------------------------------------------------------------------
-- partie_op-calc_val.adb : calcul de la valeur d'un operande         --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 15/10/95                           --
------------------------------------------------------------------------

with Partie_Op.Temps; use Partie_Op.Temps;
with Config_Machine; use Config_Machine;
with MA_Detiq; use MA_Detiq;
separate(Partie_Op)
function calc_val(dval : operande) return valeur is
  A          :          adresse_mem;
  AXX        :          adresse_mem;
  ERm        :          entier;
begin
  case acces_nature(dval) is
    when op_direct =>
      Incr (T_OP_DIR);
      return Reg(acces_registre(dval));
    when op_indirect =>
      begin
        AXX := Reg(acces_base(dval)).v_adr_mem;
        if AXX.base = null then -- #null
           raise Constraint_Error;
        end if;
      exception
        when constraint_error =>
          erreur("Adressage indirect : la base ne contient pas une adresse");
          return (T => typ_indef); -- n'importe quoi pour eviter un warning
      end;
      begin
        A := AXX + acces_deplacement(dval);
        Incr (T_OP_INDIR);
        return Mem (A);
      exception
        when constraint_error =>
          erreur("Adressage indirect : pas une adresse valide");
          return (T => typ_indef); -- n'importe quoi pour eviter un warning
      end;
    when op_indexe =>
      begin
        AXX := Reg(acces_base(dval)).v_adr_mem;
        if AXX.base = null then -- #null
           raise Constraint_Error;
        end if;
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : " &
                        "la base ne contient pas une adresse");
          return (T => typ_indef); -- n'importe quoi pour eviter un warning
      end;
      begin
        ERm := Reg(acces_index(dval)).v_entier;
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : " &
                 "l'index ne contient pas un entier");
          return (T => typ_indef); -- n'importe quoi pour eviter un warning
      end;
      begin
        A := AXX + (ERm + acces_deplacement(dval));
        Incr (T_OP_INDEX);
        return Mem (A);
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : pas une adresse " &
                 "d'un mot adressable");
          return (T => typ_indef); -- n'importe quoi pour eviter un warning
      end;
    when op_entier =>
      Incr (T_OP_IMM);
      return (typ_entier, acces_entier(dval));
    when op_flottant =>
      Incr (T_OP_IMM);
      return (typ_flottant, Acces_Flot_MA(dval));
    when op_null =>
      Incr (T_OP_IMM);
      return (typ_adr_mem, adr_null);
    when op_etiq =>
      Incr (T_OP_ETIQ);
      declare
         L : Ligne;
      begin
         L := Acces_Ligne_Def (Acces_Etiq (dval));
         if L = null then
            put_line("Pas d'instruction apres l'etiquette " &
                     acces_string(Acces_Etiq (dval)));
            raise erreur_PC;
         end if;
         return (typ_adr_code, acces_adresse_code (acces_num_ligne (L)));
       end;
    when op_chaine =>
      erreur("ERREUR IMPOSSIBLE !!!");
      return (T => typ_indef); -- n'importe quoi pour eviter un warning
  end case;
end calc_val;
