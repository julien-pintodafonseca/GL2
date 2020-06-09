-- This implementation is not based on Ada.Wide_Text_Io nor Ada.Wide_Wide_Text_Io 
-- because they seem quite buggy on my GNAT version.

with Interfaces; use Interfaces;
with Ada.Text_Io; use Ada.Text_Io;
with Types_Base; use Types_Base;

with Ada.Unchecked_Conversion;
package body Utf8_Es is
   
   function Entier_From_Unsigned is new Ada.Unchecked_Conversion(Unsigned_32, Entier);   
   --  function Entier_From_Unsigned(N: Unsigned_32) return Entier is
   --  begin
   --     if Shift_Right(N, 31)=0 then
   --        return Entier(N);
   --     end if;
   --     return Entier'First+Entier(N xor Shift_Left(1, 31));
   --  end;    
   
   
   function Unsigned_From_Entier is new Ada.Unchecked_Conversion(Entier, Unsigned_32);   
   --  function Unsigned_From_Entier(N: Entier) return Unsigned_32 is
   --  begin
   --     if N>=0 then
   --        return Unsigned_32(N);
   --     end if;
   --     return Unsigned_32(N - Entier'First) xor Shift_Left(1, 31);
   --  end;    
     
   procedure Get_Utf8(R: out Entier) is
      -- see https://en.wikipedia.org/wiki/UTF-8#Description
      C: Character;
      Index: Unsigned_32;
      W: Unsigned_32;
   begin
      Get_Immediate(C);
      W := Character'Pos(C);
      Index := Shift_Right(W,4);
      if Index < 2#1100# then
         -- only one byte
         R:=Entier(W);
         return;
      end if;
      Get_Immediate(C);
      W := Shift_Left(W,8) xor Character'Pos(C);
      if Index < 2#1110# then
         -- only two bytes
         R := Entier(W);
         return;
      end if;
      Get_Immediate(C);
      W := Shift_Left(W,8) xor Character'Pos(C);
      if Index < 2#1111# then
         -- only three bytes
         R := Entier(W);
         return;
      end if;
      Get_Immediate(C);
      R := Entier_From_Unsigned(Shift_Left(W,8) xor Character'Pos(C));
   exception
      when End_Error =>
         R := 0;
         return;
   end;
   
   
   procedure Put_Utf8(N: Entier) is   
      procedure Decode(W: Unsigned_32) is
      begin
         if W = 0 then
            -- we skip EOF character.
            return;
         end if;
         Decode(Shift_Right(W,8));
         Put(Character'Val(W mod 256));
      end;
   begin
      Decode(Unsigned_From_Entier(N));
   end;
   
end;
