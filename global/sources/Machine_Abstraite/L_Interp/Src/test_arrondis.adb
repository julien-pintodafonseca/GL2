-- activer les assertions
pragma Assertion_Policy(Check);

with Ada.Text_IO, Arrondis, Types_Base;
use  Ada.Text_IO, Arrondis, Types_Base;


procedure Test_Arrondis is
   function Computation return Flot_MA is
      X : Flot_MA;
   begin
      X := 1.0;
      X := X / 3.0;
      X := X * 3.0;
      return X;
   end;
   function Computation_Neg return Flot_MA is
      X : Flot_MA;
   begin
      X := -1.0;
      X := X / 3.0;
      X := X * 3.0;
      return X;
   end;
begin
   Positionner_Arrondi_Flottant(FE_TONEAREST);
   Put_Line(Flot_MA'Image(Computation - 1.0));
   pragma Assert(Computation = 1.0,
                 "arrondi au plus proche KO");
   pragma Assert(Computation_Neg = -1.0,
                 "arrondi au plus proche négatif KO");

   Positionner_Arrondi_Flottant(FE_DOWNWARD);
   Put_Line(Flot_MA'Image(Computation - 1.0));
   pragma Assert(Computation < 1.0,
                 "arrondi en dessous KO");
   pragma Assert(Computation_Neg < -1.0,
                 "arrondi en dessous négatif KO");

   Positionner_Arrondi_Flottant(FE_UPWARD);
   Put_Line(Flot_MA'Image(Computation - 1.0));
   pragma Assert(Computation > 1.0,
                 "arrondi en dessus KO");
   pragma Assert(Computation_Neg > -1.0,
                 "arrondi en dessus négatif KO");

   Positionner_Arrondi_Flottant(FE_TOWARDZERO);
   Put_Line(Flot_MA'Image(Computation - 1.0));
   pragma Assert(Computation < 1.0,
                 "arrondi en vers 0 KO");
   pragma Assert(Computation_Neg > -1.0,
                 "arrondi en vers 0 négatif KO");
   Put_Line("Reglages d'arrondis OK");
end;
