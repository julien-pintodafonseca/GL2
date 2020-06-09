with Ada.Text_IO, Ayacc_File_Names;
use  Ada.Text_IO, Ayacc_File_Names;
package body Shift_Reduce_File is

  SCCS_ID : constant String := "@(#) shift_reduce_file.ada, Version 1.2";



  Rcs_ID : constant String := "$Header$";
    
    The_File : File_Type; 

    procedure Open_Write is
    begin
        Create(The_File, Out_File, Get_Shift_Reduce_File_Name); 
        --> Ajout XN 18/11/2007
        Write_Line("with " & Tokens_Unit_Name & ';');
        Write_Line("with " & Goto_Tables_Unit_Name & ';');
        --> Fin ajout XN 18/11/2007
        Write_Line("package " & Shift_Reduce_Tables_Unit_Name & " is"); 
        Write_Line("");
        Write_Line("    type Small_Integer is range -32_000 .. 32_000;");
        Write_Line("");
        Write_Line("    type Shift_Reduce_Entry is record");
        Write_Line("        T   : Small_Integer;");
        Write_Line("        Act : Small_Integer;");
        Write_Line("    end record;");
        Write_Line("    pragma Pack(Shift_Reduce_Entry);");
        Write_Line("");
        Write_Line("    subtype Row is Integer range -1 .. Integer'Last;"); 
        Write_Line("");
        Write_Line("  --pragma suppress(index_check);");
        Write_Line("");
        Write_Line("    type Shift_Reduce_Array is array " &
                   "(Row  range <>) of Shift_Reduce_Entry;");
        Write_Line("");
        Write_Line("    Shift_Reduce_Matrix : constant Shift_Reduce_Array :=");
        Write_Line("        ( (-1,-1) -- Dummy Entry");
        New_Line(The_File);
        exception
             when Name_Error | Use_Error =>
                  Put_Line("Ayacc: Error Opening """ & Get_Shift_Reduce_File_Name & """.");
                  raise;
    end Open_Write;

    procedure Close_Read is
    begin
       Delete(The_File);
    end Close_Read;

    procedure Close_Write is
    begin
        --> XN 18/11/2007 to suppress the warning (originally in
        -->    parse_template_file.adb to be included in yyparse)
     Write_Line ("");
     Write_Line ("   package yy is");
     Write_Line ("");
     Write_Line ("       -- the size of the value and state stacks");
     Write_Line ("       stack_size : constant Natural := 300;");
     Write_Line ("");
     Write_Line ("       -- subtype rule         is natural;");
     Write_Line ("       subtype parse_state  is natural;");
     Write_Line ("       -- subtype nonterminal  is integer;");
     Write_Line ("");
     Write_Line ("       -- encryption constants");
     Write_Line ("       default           : constant := -1;");
     Write_Line ("       first_shift_entry : constant :=  0;");
     Write_Line ("       accept_code       : constant := -3001;");
     Write_Line ("       error_code        : constant := -3000;");
     Write_Line ("");
     Write_Line ("       -- stack data used by the parser");
     Write_Line ("       tos                : natural := 0;");
     Write_Line ("    package yy_tokens              renames");
     Write_Line ("      " & Tokens_Unit_Name & ';');

     Write_Line ("       value_stack        : array(0..stack_size) of yy_tokens.yystype;");
     Write_Line ("       state_stack        : array(0..stack_size) of parse_state;");
     Write_Line ("");
     Write_Line ("       -- current input symbol and action the parser is on");
     Write_Line ("       action             : integer;");
     Write_Line ("       rule_id            : " &
                 Goto_Tables_Unit_Name & ".rule;");
     Write_Line ("       input_symbol       : yy_tokens.token;");
     Write_Line ("");
     Write_Line ("");
     Write_Line ("       -- error recovery flag");
     Write_Line ("       error_flag : natural := 0;");
     Write_Line ("          -- indicates  3 - (number of valid shifts after an error occurs)");
     Write_Line ("");
     Write_Line ("       look_ahead : boolean := true;");
     Write_Line ("       index      : integer;");
     Write_Line ("");
     Write_Line ("    end yy;");
     Write_Line ("");
      --> End XN 17/11/2007

        Write_Line("end " & Shift_Reduce_Tables_Unit_Name & ";"); 
        Close(The_File);
    end Close_Write;

    procedure Write(S: in String) is 
    begin 
        Put(The_File, S) ;
    end;

    procedure Write_Line(S: in String) is
    begin
        Put_Line(The_File, S); 
    end Write_Line;

    procedure Write(C: in Character) is
    begin
        Put(The_File,C);   
    end;

end Shift_Reduce_File;
