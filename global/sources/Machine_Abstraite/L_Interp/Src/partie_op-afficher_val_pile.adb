
with Entier_ES, Flottant_ES;
use Entier_ES, Flottant_ES;
separate(Partie_Op)
procedure afficher_val_pile(dmin, dmax : deplacement) is
  t : natural;
  prec : boolean;
  nbpr : natural range 0 .. 2;
begin
  t := adresse_pile'image(dmax)'length - 1;
  for a in reverse dmin .. dmax loop
    prec := false;
    nbpr := 0;
    if a = le_LB.v_adr_mem.depl then
      put("LB ");
      prec := true;
      nbpr := nbpr + 1;
    end if;
    if a = le_SP.v_adr_mem.depl then
      put("SP ");
      prec := true;
      nbpr := nbpr + 1;
    end if;
    if prec then
      for i in nbpr .. 1 loop
        put("---");
      end loop;
      put("-> ");
    else
      put("         "); -- 9 espaces = 3("SP ") + 3("LB ") + 3("-> ")
    end if;
    put(entier(a), t);
    put(" | ");
    afficher_valeur(Mem ((P, a)));
    new_line;
  end loop;
exception
  when constraint_error =>
    raise erreur_exec_inst; -- nom peu explicite ici, mais c'est 
                            -- pour ne pas avoir a nommer une nouvelle
                            -- exception juste pour ce cas, qui serait
                            -- traitee exactement de la meme maniere que
                            -- erreur_exec_inst.
end;
