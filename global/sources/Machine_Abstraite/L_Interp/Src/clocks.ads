-- Clocks from the machine (in seconds)
--
-- In order to have "accurate" times with 32 bits integer and floats,
-- we split the current clock in two functions: 
--    one "integer" clock representing the time when ima "starts".
--    one "float" clock representing the time since ima is run ! 
--
--
-- Author: Ensimag teachers
--
-- Creation: July 2016 

with Types_Base; use Types_Base;
  
package Clocks is
   
   pragma Elaborate_body(Clocks);
   
   function Start_Clk return Entier;
   -- returns the rounded number of seconds 
   -- ellapsed between the launching time of "ima"
   -- and 'Mon, 01 Jan 2001 00:00:00'
   
   function Clk return Flot_MA;
   -- returns the number of seconds ellapsed since Start_Clk
   -- NB: this number can not be negative ! 
   
end Clocks;
