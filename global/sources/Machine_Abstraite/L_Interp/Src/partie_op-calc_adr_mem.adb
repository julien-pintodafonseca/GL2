------------------------------------------------------------------------
-- partie_op-calc_adr_mem.adb : calcul de l'adresse d'un operande         --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Date de derniere modification : 21/11/94                           --
------------------------------------------------------------------------

with Partie_Op.Temps; use Partie_Op.Temps;
with Config_Machine; use Config_Machine;
separate(Partie_Op)
function calc_adr_mem(dadr : operande) return adresse_mem is
  AXX : adresse_mem;
  ERm : entier;
begin
  case acces_nature(dadr) is
    when op_indirect =>
      begin
        AXX := Reg(acces_base(dadr)).v_adr_mem;
        if AXX.base = null then -- #null
           raise Constraint_Error;
        end if;
      exception
        when constraint_error =>
          erreur("Adressage indirect : la base ne contient pas une adresse");
          return (null, 0);  --  n'importe quoi pour eviter un warning
      end;
      begin
        Incr (T_OP_INDIR);
        return AXX + acces_deplacement(dadr);
      exception
        when constraint_error =>
          erreur("Adressage indirect : pas une adresse memoire");
          return (null, 0);  --  n'importe quoi pour eviter un warning
      end;
    when op_indexe =>
      begin
        AXX := Reg(acces_base(dadr)).v_adr_mem;
        if AXX.base = null then -- #null
           raise Constraint_Error;
        end if;
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : " & 
                 "la base ne contient pas une adresse");
          return (null, 0);  --  n'importe quoi pour eviter un warning
      end;
      begin
        ERm := Reg(acces_index(dadr)).v_entier;
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : " & 
                 "l'index ne contient pas un entier");
          return (null, 0);  --  n'importe quoi pour eviter un warning
      end;
      begin
        Incr (T_OP_INDEX);
        return AXX + ERm + acces_deplacement(dadr);
      exception
        when constraint_error =>
          erreur("Adressage indirect indexe : pas une adresse memoire");
          return (null, 0);  --  n'importe quoi pour eviter un warning
      end;
    when others =>
      put_line("IMPOSSIBLE !!!");
      raise erreur_exec_inst;
      return (null, 0);  --  n'importe quoi pour eviter un warning
  end case;
end;
