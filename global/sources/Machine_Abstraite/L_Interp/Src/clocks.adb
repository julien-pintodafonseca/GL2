with Ada.Calendar; use Ada.Calendar;

package body Clocks is
   
   Year_2001: constant Time := Time_Of(2001, 1, 1, 0.0);
   
   Start: constant Time := Clock;
   Start_Clock: constant Entier := Entier(Start - Year_2001);
   
   function Start_Clk return Entier is
   begin
      return Start_Clock;
   end;
   
   function Clk return Flot_Ma is
   begin
      return Flot_Ma(Clock - Start);
   end;
   
end Clocks;
