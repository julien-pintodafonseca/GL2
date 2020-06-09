package Arrondis is
   type Mode_Arrondi is (FE_TONEAREST, FE_UPWARD, FE_DOWNWARD, FE_TOWARDZERO);
   procedure Positionner_Arrondi_Flottant(Mode : Mode_Arrondi);
   Fe_Error : exception;
end Arrondis;
