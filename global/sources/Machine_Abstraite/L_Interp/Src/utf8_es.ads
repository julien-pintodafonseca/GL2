--
-- E/S from utf8 characters into 32-bits signed integer
--
-- The bytes of the integer encoding are "shift on the right" as much
-- as possible, and correspond to those of the utf8 encoding in the
-- same order (the last byte is the least significant byte).
--
-- For example, a single byte character (e.g. an ASCII character)
-- corresponds to an integer between 0 and 127. And, an utf8
-- character corresponds to a negative integer iff its utf8 encoding
-- has 4 bytes.
--
-- see https://en.wikipedia.org/wiki/UTF-8
-- 
-- Author: Ensimag teachers
--
-- Creation: July 2016 


with Types_Base; use Types_Base;

package Utf8_Es is
     
   procedure Get_Utf8(R: out Entier);
   -- NB: similar to a "Get_Immediate"
   -- R=0 if EOF is reached
   
   procedure Put_Utf8(N: Entier);
   
end;
