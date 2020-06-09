------------------------------------------------------------------------
-- ma_token_io.ada : paquetage de lecture et d'ecriture de tokens     --
--                                                                    --
-- Auteur : X. Nicollin                                               --
--                                                                    --
-- Date de creation : 01/94                                        --
-- Date de derniere modification :                                    --
------------------------------------------------------------------------

with Ada.Text_IO, MA_Syntax_Tokens;
use  Ada.Text_IO, MA_Syntax_Tokens;

package MA_Token_IO is new Enumeration_IO(token);

