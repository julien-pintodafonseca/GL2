------------------------------------------------------------------------
-- config_machine.ads : constantes de configuration de la MA          --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 12/2007                                         --
------------------------------------------------------------------------

package Config_Machine is

   --  temps d'exécution des instructions (en nombre de sycles internes)

   T_LOAD : constant := 2;
   T_STORE : constant := 2;
   T_LEA : constant := 0;
   T_PEA : constant := 4;
   T_PUSH : constant := 4;
   T_POP : constant := 2;
   T_NEW : constant := 16;
   T_DEL : constant := 16;
   T_ADDe : constant := 2;   -- entiers
   T_ADDr : constant := 2;   -- flottants
   T_SUBe : constant := 2;   -- entiers
   T_SUBr : constant := 2;   -- flottants
   T_OPPe : constant := 2;   -- entiers
   T_OPPr : constant := 2;   -- flottants
   T_MULe : constant := 20;  -- entiers
   T_MULr : constant := 20;  -- flottants
   T_FMA  : constant := 21;
   T_CMPe : constant := 2;   -- entiers
   T_CMPr : constant := 2;   -- flottants
   T_QUO : constant := 40;
   T_REM : constant := 40;
   T_FLOAT : constant := 4;
   T_Sccv : constant := 3;  -- si vrai
   T_Sccf : constant := 2;  -- si faux
   T_DIV : constant := 40;
   T_INT : constant := 4;
   T_SHL : constant := 2;
   T_SHR : constant := 2;   
   T_BRA : constant := 5;
   T_Bccv : constant := 5;  -- si vrai
   T_Bccf : constant := 4;  -- si faux
   T_BSR : constant := 9;
   T_RTS : constant := 8;
   T_RINT : constant := 16;
   T_RFLOAT : constant := 16;
   T_WINT : constant := 16;
   T_WFLOAT : constant := 16;
   T_WSTR : constant := 16;
   T_WNL : constant := 14;
   T_ADDSP : constant := 4;
   T_SUBSP : constant := 4;
   T_TSTO : constant := 4;
   T_HALT : constant := 1;
   T_SETROUND : constant := 20;
   T_SCLK: constant := 2;
   T_CLK: constant := 16;

   --  temps d'accés aux opérandes (en nombre de sycles internes)

   T_OP_DIR    : constant := 0;  -- Rm
   T_OP_INDIR  : constant := 4;  -- d(XX)
   T_OP_INDEX  : constant := 5;  -- d(XX,Rm)
   T_OP_IMM    : constant := 2;  -- #d
   T_OP_ETIQ   : constant := 2;  -- etiq
   T_OP_CHAINE : constant := 2;  -- "..." (par caractère)

   --  taille par défaut de la pile et du tas (en nombre de mots)

   Defaut_Taille_Pile : constant := 10_000;
   Defaut_Taille_Tas : constant := 10_000;

end Config_Machine;
