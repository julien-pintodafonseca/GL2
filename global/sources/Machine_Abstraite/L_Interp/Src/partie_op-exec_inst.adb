------------------------------------------------------------------------
-- partie_op-exec_inst.adb : execution des instructions de la MA      --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                           --
-- Dates de modifications :                                           --
--   27/11/94 :                                                       --
--     acces_nom --> acces_string (changement dans tables.ads)        --
--   15/10/95 :                                                       --
--     Changements dans la syntaxe :                                  --
--       PUSH dval --> PUSH Rm                                        --
--       WINT, WFLOAT, RINT et RFLOAT deviennent unaires              --
--   01/11/95 :                                                       --
--     Changement de comportement sur RINT et RFLOAT :                --
--       On ne fait que positionner OV si pas un entier ou pas un     --
--     Le temps de calcul est ajoute meme si OV                       --
--   27/01/99 :                                                       --
--     Correction d'un bug lie a Ada 95 : en absence de               --
--     Machine_Overflows, il n'y a pas de Constraint_Error sur les    --
--     flottants si debordement ou division par 0. On passe par une   --
--     triple conversion                                              --
--     Flottant'Image (Flottant'Value (Flottant'Image (X)),           --
--     avec un test explicite de caractere sur la second              --
--     Flottant'Image   pour retrouver Constraint_Error.              --
--   29/01/99 :                                                       --
--     Simplification de la correction du 27/01/99 : il suffit de     --
--     tester X in Flottant'Range.                                    --
--   03/02/99 :                                                       --
--     Simplification de l'implantation de INT grace a l'attribut     --
--     Flottant'Truncation.                                           --
--   04/02/99 :                                                       --
--     Pour les resultats de calculs sur les Flottants, on passe      --
--     systematiquement par 'Value ('Image) pour tenter de supprimer  --
--     les problemes d'arrondi (voir fonction La_Valeur).             --
--   07/01/2000 :                                                     --
--     Factorisation de l'execution des Scc (procedure Exec_Scc)      --
--     Factorisation de l'execution des Bcc (procedure Exec_Bcc)      --
--     Factorisation de l'execution des operateurs arithmetiques      --
--     binaires (ADD, SUB, MUL, DIV) a l'aide d'un generique          --
--     Exec_Ar_Bin                                                    --
--   04/02/2000 :                                                     --
--     Utilisation du paquetage Partie_Op.Temps                       --
--     Correction d'une erreur sur TSTO (on risquait d'avoir          --
--     Constraint_Error)                                              --
--   26/01/2001 :                                                     --
--     Correction d'une erreur dans Exec_Ar_Bin (guillemets en        --
--     trop dans l'affichage du message d'erreur)                     --
--   25/01/2002 :                                                     --
--     Correction d'une erreur dans code_FLOAT (positionnait CP       --
--     alors que la spec de la MA dit que non)                        --
--   02/11/2004 :                                                     --
--     Modification des temps d'execution (DIV et REM)                --
--     inversion dest <-> source                                      --
--     Correction d'une erreur dans code_TSTO (ne calculait pas       --
--     la valeur de l'operande, et donc le temps d'acces a cet        --
--     operande n'etait pas compte !                                  --
--   06/12/2007 :                                                     --
--     Suppression de La_Valeur                                       --
--     Test_Debord : utilisation de 'Valid                            --
--     Transfert des temps d'exécution dans Config_Machine            --
--   04/07/2016 :                                                     --
--     ajout WUTF8, RUTF8                                             --
------------------------------------------------------------------------

with Config_Machine; use Config_Machine;
with Partie_Op.Temps; use Partie_Op.Temps;

with Arrondis; use Arrondis;

with Lecture_Entiers, Lecture_Flottants, Entier_ES, Flottant_ES, Options;
use Lecture_Entiers, Lecture_Flottants, Entier_ES, Flottant_ES, Options;

with Interfaces.C;
use  Interfaces.C;
with Interfaces.C.Strings;
use  Interfaces.C.Strings;
  
with Ada.Strings.Fixed, ada.Characters.Handling;
with Utf8_Es; use Utf8_Es;  -- pour WUTF8, RUTF8
with Clocks; use Clocks;  -- pour SCLK, CLK

separate(Partie_Op)
procedure exec_inst is

  COD : code_operation;
  OP1, OP2 : operande;
  V1, V2, V3 : valeur;
  T1, T2, T3 : type_val;
  Rm : banalise;
  AP : adresse_mem;
  AM : adresse_mem;

  procedure Float_To_Hex_String(F : C_Float; Dest: Chars_Ptr);
  pragma Import(C, Float_To_Hex_String, "float_to_hex_string");

   function Test_Debord (X : Flot_MA) return Flot_MA is
   begin
      -- return X;
      -- if X in Flottant'Range then
      if X'Valid then
         return X;
      else
         raise Constraint_Error;
      end if;
   end Test_Debord;

   procedure Exec_Scc (Cond : in Boolean) is
   begin
      OP1 := oper1;
      Rm := acces_registre(OP1);
      if Cond then
        Reg(Rm) := (typ_entier, 1);
        Incr (T_Sccv);
      else
        Reg(Rm) := (typ_entier, 0);
        Incr (T_Sccf);
      end if;
   end Exec_Scc;

   procedure Exec_Bcc (Nom : in String; Cond : in Boolean) is
   begin
      OP1 := oper1;
      V1 := calc_val (Op1);
      T1 := V1.T;
      if T1 /= typ_adr_code then
         erreur (Nom & " avec operande : " & mess_typ_val (T1));
      end if;

      if Cond then
        charger_PC (V1.v_adr_code);
        Incr (T_Bccv);
      else
        Incr (T_Bccf);
      end if;
   end Exec_Bcc;

   generic
      Tps_e : in Natural;
      Tps_r : in Natural;
      with function Op (E1, E2 : Entier) return Entier;
      with function Op (E1, E2 : Flot_MA) return Flot_MA;
      Nom : in String;
   procedure Exec_Ar_Bin;

   procedure Exec_Ar_Bin  is
   begin
     OP1 := oper1;
     V1 := calc_val(OP1);
     T1 := V1.T;
     OP2 := oper2;
     Rm := acces_registre(OP2);
     V2 := Reg(Rm);
     T2 := V2.T;
     -- Reset de OV, on le positionnera à true si besoin.
     OV := False;
     if T1 = typ_entier and T2 = typ_entier then
       Incr (Tps_e);
       -- On fait l'opération une première fois avec les exception,
       -- pour positionner OV ...
       begin
          Reg(Rm).v_entier := Op (V2.v_entier, V1.v_entier);
       exception
          when Constraint_Error =>
             Ov := True;
       end;
       -- ... puis on fait la vraie opération (modulo 2^32)
       declare
            pragma Suppress (Overflow_Check);
            pragma Suppress (Range_Check);
       begin
          Reg(Rm).v_entier := Op (V2.v_entier, V1.v_entier);
       end;
     elsif T1 = typ_flottant and T2 = typ_flottant then
       Incr (Tps_r);
       declare
            pragma Suppress (Overflow_Check);
            pragma Suppress (Range_Check);
       begin
          Reg(Rm).v_flottant :=
            Test_Debord (Op (V2.v_flottant, V1.v_flottant));
       exception
          when constraint_error =>
             OV := true;
       end;
     else
       erreur(Nom & " avec op1 : " & mess_typ_val(T1) &
              " et op2 : " & mess_typ_val(T2));
     end if;
     pos_CC(Reg(Rm));
   end Exec_Ar_Bin;

   procedure Exec_Ar_Bin_ADD is
      new Exec_Ar_Bin (T_ADDe, T_ADDr, "+", "+", "ADD");

   procedure Exec_Ar_Bin_SUB is
      new Exec_Ar_Bin (T_SUBe, T_SUBr, "-", "-", "SUB");

   procedure Exec_Ar_Bin_MUL is
      new Exec_Ar_Bin (T_MULe, T_MULr, "*", "*", "MUL");
   
   procedure Exec_SHL is
   begin
      OP1 := Oper1;
      Rm := acces_registre(OP1);
      V1 := Reg(Rm);
      T1 := V1.T;
      
      if T1 /= Typ_Entier then
	 Erreur ("SHL avec operande 1 : " & Mess_Typ_Val (T1));
      end if;
      
      Incr(T_SHL);
      
      begin
       Reg(Rm).V_Entier := V1.V_Entier*2;
      exception
         when constraint_error =>
	    OV := true;
	    Reg(Rm).V_Entier := 0;
      end;
      pos_CC(Reg(Rm));
   end;
   
   procedure Exec_SHR is
   begin
      OP1 := Oper1;
      Rm := acces_registre(OP1);
      V1 := Reg(Rm);
      T1 := V1.T;
      
      if T1 /= Typ_Entier then
	 Erreur ("SHR avec operande 1 : " & Mess_Typ_Val (T1));
      end if;
      
      Incr(T_SHR);      
      
      Reg(Rm).V_Entier := V1.V_Entier/2;
      pos_CC(Reg(Rm));
   end;
   
   procedure Exec_Fma is
      function FMA (X,Y,Z : C_Float) return C_Float ;
      pragma Import(C, FMA, "__fmaf");
   begin
      OP1 := oper1;
      V1 := calc_val(OP1);
      T1 := V1.T;
      OP2 := oper2;
      Rm := acces_registre(OP2);
      V2 := Reg(Rm);
      T2 := V2.T;
      V3 := Reg(R1);
      T3 := V3.T;

      if T1 /= typ_flottant then
         erreur ("FMA avec operande 1 : " & Mess_Typ_Val (T1));
      elsif T2 /= Typ_Flottant then
         erreur ("FMA avec operande 2 : " & Mess_Typ_Val (T2));
      elsif T3 /= Typ_Flottant then
         erreur ("FMA avec operande implicite (R1) : " & Mess_Typ_Val (T3));
      end if ;

      Incr (T_FMA);

      declare
         pragma Suppress (Overflow_Check);
         pragma Suppress (Range_Check);
      begin
         Reg(Rm).v_flottant :=
           Test_Debord (Flot_MA(FMA (C_Float(V1.V_Flottant),
                                     C_Float(V2.V_Flottant),
                                     C_Float(V3.V_Flottant))));
      exception
         when constraint_error =>
             OV := true;
      end;
      pos_CC(Reg(Rm));
   end ;

begin -- exec_inst
  incr_PC;
  ecr_a_finir := false;
  COD := code_op;
  case COD is
    when code_LOAD =>
      OP1 := oper1;
      OP2 := oper2;
      V1 := calc_val(OP1);
      Reg(acces_registre(OP2)) := V1;
      pos_CC(V1);
      -- OV := false;
      Incr (T_LOAD);

    when code_STORE =>
      OP1 := oper1;
      V1 := Reg(acces_registre(OP1));
      OP2 := oper2;
      begin
        AP := calc_adr_mem(OP2);
        Store (AP, V1);
      exception
        when constraint_error =>
          erreur("STORE : Pas une adresse memoire");
      end;
      pos_CC(V1);
      -- OV := false;
      Incr (T_STORE);

    when code_PUSH =>
      begin
        OP1 := oper1;
        V1 := Reg(acces_registre(OP1));
        le_SP.v_adr_mem := le_SP.v_adr_mem + 1;
        Store (le_SP.v_adr_mem, V1);
        pos_CC(V1);
        -- OV := false;
        Incr (T_PUSH);
      exception
        when constraint_error =>
          erreur("PUSH : Debordement de la pile");
      end;

    when code_POP =>
      begin
        OP1 := oper1;
        V1 := Mem (le_SP.v_adr_mem);
        Reg(acces_registre(OP1)) := V1;
        le_SP.v_adr_mem := le_SP.v_adr_mem - 1;
        pos_CC(V1);
        -- OV := false;
        Incr (T_POP);
      exception
        when constraint_error =>
          erreur("POP : SP ne contient pas une adresse de la pile");
      end;

    when code_LEA =>
      OP1 := oper1;
      OP2 := oper2;
      AM := calc_adr_mem(OP1);
      Reg(acces_registre(OP2)) := (typ_adr_mem, AM);
      Incr (T_LEA);

    when code_PEA =>
      begin
        OP1 := oper1;
        AM := calc_adr_mem(OP1);
        le_SP.v_adr_mem := le_SP.v_adr_mem + 1;
        Store (le_SP.v_adr_mem, (typ_adr_mem, AM));
        Incr (T_PEA);
      exception
        when constraint_error =>
          erreur("PEA : Debordement de la pile");
      end;

    when code_NEW =>
      declare
         Nb_A_Allouer : Depl_Mem;
         Bloc : P_Bloc_Mem;
      begin
        OP1 := oper1;
        V1 := calc_val (OP1);
        Incr(T_NEW);
        Nb_A_Allouer := V1.V_Entier;
        if Nb_Alloc + Nb_A_Allouer > Max_Alloc then
           raise Constraint_Error;
        end if;
        OP2 := oper2;
        -- array initializer (... => ...) would allocate a temporary
        -- variable on the stack, which is far more limited than the
        -- heap.
        Bloc := new Bloc_Mem(0 .. Nb_A_Allouer - 1);
        for I in Bloc'Range loop
           Bloc(I) := (T => Typ_Indef);
        end loop;
        Reg (acces_registre(OP2)) := (typ_adr_mem, (Bloc, 0));
        Nb_Alloc := Nb_Alloc + Nb_A_Allouer;
        OV := False;
      exception
        when Constraint_Error | Storage_Error =>
           OV := True;
      end;

    when code_DEL =>
      begin
        OP1 := oper1;
        Incr(T_DEL);
        Nb_Alloc := Nb_Alloc - Reg (acces_registre(OP1)).v_adr_mem.base'Length;
        Liberer_Bloc(Reg (acces_registre(OP1)).v_adr_mem.base);
        Reg (acces_registre(OP1)) := (T => typ_indef);
        OV := False;
      exception
        when Constraint_Error | Storage_Error =>
           OV := True;
      end;

    when code_ADD =>
      Exec_Ar_Bin_ADD;

    when code_SUB =>
      Exec_Ar_Bin_SUB;

    when code_MUL =>
       Exec_Ar_Bin_MUL;

     when Code_FMA =>
        Exec_Fma;
	
     when Code_SHL =>
	Exec_SHL;
	
     when Code_SHR =>
	Exec_SHR;
	
    when code_OPP =>
      OP1 := oper1;
      V1 := calc_val(OP1);
      T1 := V1.T;
      OP2 := oper2;
      Rm := acces_registre(OP2);
      if T1 = typ_entier then
          declare
               pragma Suppress (Overflow_Check);
               pragma Suppress (Range_Check);
          begin
              Reg(Rm) := (typ_entier, - V1.v_entier);
              Incr (T_OPPe);
         end;
      elsif T1 = typ_flottant then
        Reg(Rm) := (typ_flottant, Test_Debord (- V1.v_flottant));
        Incr (T_OPPr);
      else
        erreur("OPP avec operande : " & mess_typ_val(T1));
      end if;
      pos_CC(Reg(Rm));
      -- OV := false;

    when code_CMP =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        V2 := Reg(Rm);
        T2 := V2.T;
        if T1 = typ_entier and T2 = typ_entier then
          N := V2.v_entier < V1.v_entier;
          Z := V2.v_entier = V1.v_entier;
          Incr (T_CMPe);
        elsif T1 = typ_flottant and T2 = typ_flottant then
          N := V2.v_flottant < V1.v_flottant;
          Z := V2.v_flottant = V1.v_flottant;
          Incr (T_CMPr);
        elsif T1 = typ_adr_mem and T2 = typ_adr_mem then
          Z := V2.v_adr_mem = V1.v_adr_mem; -- #null = #null
          N := not Z;  -- pour la coherence
        else
          erreur("CMP avec op1 : " & mess_typ_val(T1) &
                 " et op2 : " & mess_typ_val(T2));
        end if;
      end;

    when code_QUO =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        V2 := Reg(Rm);
        T2 := V2.T;
        if T1 = typ_entier and T2 = typ_entier then
          Incr (T_QUO);
          Reg(Rm).v_entier := V2.v_entier / V1.v_entier;
          pos_CC(Reg(Rm));
        else
          erreur("QUO avec op1 : " & mess_typ_val(T1) &
                 " et op2 : " & mess_typ_val(T2));
        end if;
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;

    when code_REM =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        V2 := Reg(Rm);
        T2 := V2.T;
        if T1 = typ_entier and T2 = typ_entier then
          Incr (T_REM);
          Reg(Rm).v_entier := V2.v_entier rem V1.v_entier;
          pos_CC(Reg(Rm));
        else
          erreur("REM avec op1 : " & mess_typ_val(T1) &
                 " et op2 : " & mess_typ_val(T2));
        end if;
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;

    when code_SEQ =>
      Exec_Scc (EQ);

    when code_SNE =>
      Exec_Scc (NE);

    when code_SGT =>
      Exec_Scc (GT);

    when code_SLT =>
      Exec_Scc (LT);

    when code_SGE =>
      Exec_Scc (GE);

    when code_SLE =>
      Exec_Scc (LE);

    when code_SOV =>
      Exec_Scc (OV);

    when code_DIV =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        V2 := Reg(Rm);
        T2 := V2.T;
        if T1 = typ_flottant and T2 = typ_flottant then
          Incr (T_DIV);
          Reg(Rm).v_flottant := Test_Debord (V2.v_flottant / V1.v_flottant);
          pos_CC(Reg(Rm));
        else
          erreur("DIV avec op1 : " & mess_typ_val(T1) &
                 " et op2 : " & mess_typ_val(T2));
        end if;
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;

    when code_FLOAT =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        if T1 = typ_entier then
          Incr (T_FLOAT);
          Reg(Rm) := (typ_flottant, Test_Debord (Flot_MA(V1.v_entier)));
          -- pos_CC(Reg(Rm));
        else
          erreur("FLOAT avec operande : " & mess_typ_val(T1));
        end if;
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;

    when code_INT =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        T1 := V1.T;
        OP2 := oper2;
        Rm := acces_registre(OP2);
        if T1 = typ_flottant then
          declare
            r : Flot_MA;
            -- Supprime 03/02/99
            -- a : entier;
            b : entier;
          begin
            Incr (T_INT);
            r := V1.v_flottant;
            -- Supprime 03/02/99
            -- a := entier(r);
            -- if r >= 0.0 then
            --    if flottant(a) <= r then
            --       b := a;
            --    else
            --       b := a - 1;
            --    end if;
            -- else -- r < 0.0
           --    if flottant(a) >= r then
            --       b := a;
            --    else
            --       b := a + 1;
            --    end if;
            -- end if;
            b := Entier (Flot_MA'Truncation (r));
            Reg(Rm) := (typ_entier, b);
          end;
        else
          erreur("INT avec operande : " & mess_typ_val(T1));
        end if;
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;

    when code_BRA =>
      OP1 := oper1;
      V1 := calc_val (Op1);
      T1 := V1.T;
      if T1 /= typ_adr_code then
         erreur ("BRA avec operande : " & mess_typ_val (T1));
      end if;
      charger_PC (V1.v_adr_code);
      Incr (T_BRA);

    when code_BEQ =>
      Exec_Bcc ("BEQ", EQ);

    when code_BNE =>
      Exec_Bcc ("BNE", NE);

    when code_BGT =>
      Exec_Bcc ("BGT", GT);

    when code_BLT =>
      Exec_Bcc ("BLT", LT);

    when code_BGE =>
      Exec_Bcc ("BGE", GE);

    when code_BLE =>
      Exec_Bcc ("BLE", LE);

    when code_BOV =>
      Exec_Bcc ("BOV", OV);

    when code_BSR =>
      begin
        OP1 := oper1;
        V1 := calc_val (Op1);
        T1 := V1.T;
        if T1 /= typ_adr_code then
           erreur ("BSR avec operande : " & mess_typ_val (T1));
        end if;
        le_SP.v_adr_mem := le_SP.v_adr_mem + 2;
        Store (le_SP.v_adr_mem - 1, (typ_adr_code, contenu_PC));
        Store (le_SP.v_adr_mem, le_LB);
        le_LB.v_adr_mem := le_SP.v_adr_mem;
        charger_PC (V1.v_adr_code);
        Incr (T_BSR);
      exception
        when constraint_error =>
          erreur("BSR : Debordement de la pile");
      end;

    when code_RTS =>
      declare
        AM : adresse_mem;
        AP : adresse_mem;
      begin
        begin
          AM := le_LB.v_adr_mem;
        exception
          when constraint_error =>
            erreur("RTS : LB ne contient pas une adresse ** IMPOSSIBLE **");
        end;
        begin
          AP := AM - 1;
        exception
          when constraint_error =>
            erreur("RTS : LB ne pointe pas sur la pile");
        end;
        begin
          charger_PC(Mem (AP).v_adr_code);
        exception
          when constraint_error =>
            erreur("RTS : Pas d'adresse de retour");
        end;
        begin
          le_SP.v_adr_mem := AM - 2;
        exception
          when constraint_error =>
             erreur("RTS : Pas une adresse memoire pour SP");
        end;
        begin
          le_LB.v_adr_mem := Mem (AM).v_adr_mem;
          Incr (T_RTS);
        exception
          when constraint_error =>
             erreur("RTS : Pas une adresse memoire pour LB");
        end;
      end;

    when code_RINT =>
      declare
        val_e : entier;
      begin
        Incr (T_RINT);
        lire_entier(val_e);
        pos_CC_ent(val_e);
        Reg(R1) := (typ_entier, val_e);
        OV := false;
        lect_pas_finie := true;
      exception
        when constraint_error =>
          OV := true;
          lect_pas_finie := true;
        when data_error =>
          OV := true;
          lect_pas_finie := true;
          -- lect_pas_finie := false;
          -- skip_line; -- pour manger ce qu'il reste sur la ligne
          -- erreur("RINT : pas un entier");
        when End_Error =>
          OV := True;
      end;
      
    when code_RUTF8 =>
      declare
        val_e : entier;
      begin
        Incr (T_RINT);
        Get_Utf8(val_e);
        pos_CC_ent(val_e);
        Reg(R1) := (typ_entier, val_e);
        -- NE PAS faire "skip_line" sur un enchainement de
        --  Get_Utf8 = Get_Immediate au-dessus
        lect_pas_finie := false;         
      end;

    when code_RFLOAT =>
      declare
        val_r : Flot_MA;
      begin
        Incr (T_RFLOAT);
        lire_flottant(val_r);
        pos_CC_flottant(val_r);
        Reg(R1) := (typ_flottant, val_r);
        OV := false;
        lect_pas_finie := true;
      exception
        when constraint_error =>
          OV := true;
          lect_pas_finie := true;
        when data_error =>
          OV := true;
          lect_pas_finie := true;
          -- lect_pas_finie := false;
          -- skip_line; -- pour manger ce qu'il reste sur la ligne
          -- erreur("RFLOAT : pas un flottant");
        when End_Error =>
          OV := True;
      end;

    when code_WINT =>
      V1 := Reg(R1);
      if V1.T /= typ_entier then
        erreur("WINT avec R1 " & mess_typ_val(V1.T));
      end if;
      put(V1.v_entier, WIDTH => 1);
      Incr (T_WINT);
      if beautify then
        new_line;
      else
        ecr_a_finir := true;
      end if;
      
    when code_WUTF8 =>
      V1 := Reg(R1);
      if V1.T /= typ_entier then
        erreur("WUTF8 avec R1 " & mess_typ_val(V1.T));
      end if;
      Put_Utf8(V1.v_entier);
      Incr (T_WINT);
      if beautify then
        new_line;
      else
        ecr_a_finir := true;
      end if;

    when code_WFLOAT =>
      V1 := Reg(R1);
      if V1.T /= typ_flottant then
        erreur("WFLOAT avec R1 " & mess_typ_val(V1.T));
      end if;
      put(Ada.Characters.Handling.To_Lower
            (Ada.Strings.Fixed.Trim
               (Flot_MA'Image(V1.V_Flottant), Ada.Strings.Left)));
      Incr (T_WFLOAT);
      if beautify then
        new_line;
      else
        ecr_a_finir := true;
      end if;

    when code_WFLOATX =>
      V1 := Reg(R1);
      if V1.T /= typ_flottant then
        erreur("WFLOATX avec R1 " & mess_typ_val(V1.T));
      end if;
      declare
         -- Allocate a buffer large enough to hold any float.
         Buf : Chars_Ptr := New_String("12345678901234567890012345678990");
      begin
         Float_To_Hex_String(C_Float(V1.V_Flottant), Buf);
         Put(Value(Buf));
         Free(Buf);
      end;
      Incr (T_WFLOAT);
      if beautify then
        new_line;
      else
        ecr_a_finir := true;
      end if;

    when code_WSTR =>
      OP1 := oper1;
      put(acces_string(acces_chaine(OP1)));
      Incr (T_WSTR);
      Incr (T_OP_CHAINE * longueur(acces_chaine(OP1)));
      if beautify then
        new_line;
      else
        ecr_a_finir := true;
      end if;
      
     when Code_SCLK =>
        Incr (T_SCLK);
        pos_CC_ent(Start_Clk);
        Reg(R1) := (typ_entier, Start_Clk);
        
     when Code_CLK =>
      declare
        val_r : Flot_MA;
      begin
        Incr (T_CLK);
        Val_R := Clk;
        pos_CC_flottant(Val_R);
        Reg(R1) := (typ_flottant, val_r);
        OV := false;
      exception
        when constraint_error =>
          OV := true;
      end;
      
    when code_WNL =>
      new_line;
      Incr (T_WNL);

    when code_ADDSP =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        le_SP.v_adr_mem := le_SP.v_adr_mem + V1.v_entier;
        Incr (T_ADDSP);
      exception
        when constraint_error =>
          erreur("ADDSP : Pas une adresse");
      end;

    when code_SUBSP =>
      begin
        OP1 := oper1;
        V1 := calc_val(OP1);
        le_SP.v_adr_mem := le_SP.v_adr_mem - V1.v_entier;
        Incr (T_SUBSP);
      exception
        when constraint_error =>
           erreur("SUBSP : Pas une adresse");
      end;

    when code_TSTO =>
      OP1 := oper1;
      V1 := calc_val (OP1);
      Incr (T_TSTO);
      begin
         OV := taille_pile < le_SP.v_adr_mem.depl + V1.v_entier;
      exception
         when Constraint_Error =>
            OV := True;
      end;

    when code_HALT =>
      Incr (T_HALT);
      Etat_Ima := Termine;
      raise fin_du_programme;

    when Code_ERROR =>
      Incr (T_HALT);
      Etat_Ima := Erreur;
      raise Fin_Du_Programme;

    when Code_SETROUND_TONEAREST =>
      Incr (T_SETROUND);
      Positionner_Arrondi_Flottant(FE_TONEAREST);

    when Code_SETROUND_UPWARD =>
      Incr (T_SETROUND);
      Positionner_Arrondi_Flottant(FE_UPWARD);

    when Code_SETROUND_DOWNWARD =>
      Incr (T_SETROUND);
      Positionner_Arrondi_Flottant(FE_DOWNWARD);

    when Code_SETROUND_TOWARDZERO =>
      Incr (T_SETROUND);
      Positionner_Arrondi_Flottant(FE_TOWARDZERO);

  end case;
end;
