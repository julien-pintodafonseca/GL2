package body Arrondis is
   function Fesetround_FE_TONEAREST return Integer;
   pragma Import
     (Convention    => C,
      Entity        => Fesetround_FE_TONEAREST,
      External_Name => "fesetround_FE_TONEAREST");

   function Fesetround_FE_UPWARD return Integer;
   pragma Import
     (Convention    => C,
      Entity        => Fesetround_FE_UPWARD,
      External_Name => "fesetround_FE_UPWARD");

   function Fesetround_FE_DOWNWARD return Integer;
   pragma Import
     (Convention    => C,
      Entity        => Fesetround_FE_DOWNWARD,
      External_Name => "fesetround_FE_DOWNWARD");

   function Fesetround_FE_TOWARDZERO return Integer;
   pragma Import
     (Convention    => C,
      Entity        => Fesetround_FE_TOWARDZERO,
      External_Name => "fesetround_FE_TOWARDZERO");

   procedure Positionner_Arrondi_Flottant(Mode : Mode_Arrondi) is
      Res : Integer;
   begin
      case Mode is
         when FE_TONEAREST  => Res := Fesetround_FE_TONEAREST;
         when FE_DOWNWARD   => Res := Fesetround_FE_DOWNWARD;
         when FE_UPWARD     => Res := Fesetround_FE_UPWARD;
         when FE_TOWARDZERO => Res := Fesetround_FE_TOWARDZERO;
      end case;
      if Res /= 0 then
         raise Fe_Error;
      end if;
   end;
end Arrondis;
