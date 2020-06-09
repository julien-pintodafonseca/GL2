    
with Entier_ES, Flottant_ES;
use Entier_ES, Flottant_ES;
separate(Partie_Op)
procedure afficher_val_reg is
  -- chaines associees aux registres 
  ext_reg : constant array(registre) of string(1 .. 6)  :=
    ("SP  : ", "GB  : ", "LB  : ",
     "R0  : ", "R1  : ", "R2  : ", "R3  : ",
     "R4  : ", "R5  : ", "R6  : ", "R7  : ",
     "R8  : ", "R9  : ", "R10 : ", "R11 : ",
     "R12 : ", "R13 : ", "R14 : ", "R15 : ");
  tab : boolean := true;
begin
  put("SP  : ");
  afficher_valeur(le_SP);
  new_line;
  put("GB  : ");
  afficher_valeur(le_GB);
  new_line;
  put("LB  : ");
  afficher_valeur(le_LB);
  new_line;
  for R in banalise loop 
    put(ext_reg(R));
    afficher_valeur(Reg(R));
    if tab then
      set_col(40);
      tab := false;
    else
      new_line;
      tab := true;
    end if;
  end loop;
  put("Codes-condition vrais : ");
  if Z then
    put("EQ ");
    put("LE ");
    put("GE ");
  elsif N then
    put("NE ");
    put("LE ");
    put("LT ");
  else
    put("NE ");
    put("GE ");
    put("GT ");
  end if;
  if OV then
    put("OV");
  end if;
  new_line;
end;
