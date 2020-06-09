with Ada.Text_IO;
use  Ada.Text_IO;

with Pseudo_Code;
use  Pseudo_Code;

procedure Test_Affichage is
   L : Ligne;
   Debut : Ligne;
begin
   Debut := Creation(Com => "Debut du programme.");
   L := Debut;
   
   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_WINT)));
   L := Acces_Suivant(L);

   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_SETROUND_TONEAREST)));
   L := Acces_Suivant(L);
   
   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_SETROUND_DOWNWARD)));
   L := Acces_Suivant(L);
   
   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_SETROUND_UPWARD)));
   L := Acces_Suivant(L);
   
   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_SETROUND_TOWARDZERO)));
   L := Acces_Suivant(L);

   Changer_Suivant(L, Creation(I => Creation_Inst2(Code_FMA,
                                                   Le_Registre(R2),
                                                   Le_Registre(R3))));
   L := Acces_Suivant(L);

   Changer_Suivant(L, Creation(I => Creation_Inst0(Code_WFLOATX)));
   L := Acces_Suivant(L);

   Changer_Suivant(L, Creation(Com => "Fin du programme"));
   L := Acces_Suivant(L);
   
   Afficher_Programme(Debut);
   
end test_Affichage;
