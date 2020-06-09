
with Entier_ES, Flottant_ES;
use Entier_ES, Flottant_ES;
separate(Partie_Op)
procedure afficher_val_obj(R : in registre) is
  base : p_bloc_mem;
  amin, amax : deplacement;
  t : natural;
begin
   base := Reg (R).v_adr_mem.base;
   if base = P then
      raise erreur_exec_inst;
   end if;
   amin := 0;
   amax := base'last;
   t := deplacement'image(amax)'length - 1;
  for a in amin .. amax loop
    put(entier(a), t);
    put(" | ");
    afficher_valeur(Mem ((base, a)));
    new_line;
  end loop;
exception
  when constraint_error =>
    raise erreur_exec_inst; -- nom peu explicite ici, mais c'est 
                            -- pour ne pas avoir a nommer une nouvelle
                            -- exception juste pour ce cas, qui serait
                            -- traitee exactement de la meme maniere que
                            -- erreur_exec_inst.
end afficher_val_obj;
