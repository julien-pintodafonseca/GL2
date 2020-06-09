------------------------------------------------------------------------
-- mes_tables.adb : corps du paquetage MES_TABLES                     --
--                                                                    --
-- On implante par un arbre binaire de recherche equilibre, et non    --
-- par une table de hachage.                                          --
-- Auteur : X. Nicollin,                                              --
--          pompe dans Utilitaires/Prog/pseudo_code.table.adb !       --
--                                                                    --
-- Date de creation : 05/01/95                                        --
-- Date de derniere modification :                                    --
------------------------------------------------------------------------

with Ada.TexT_IO ;
use  Ada.TexT_IO ;

package body Mes_Tables is

   -- plus trace a une valeur importante, plus il y a d'affichage
   trace : constant natural := 0 ;

   -- indique si on doit faire les tests defensifs
   defensif : constant boolean := True ;

   type Desequilibre is range -2 .. 2 ;

   type structure_element_table is
      record
         C : chaine ;              -- la chaine associee a l'entree
         I : info ;                -- l'info associee
         deseq : Desequilibre  ;   -- le desequilibre de l'arbre
         gauche : element_table ;  -- le sous-arbre gauche
         droit : element_table ;   -- le sous-arbre droit ;
      end record ;

   type structure_table is
      record
         racine : element_table := null ;
      end record ;

   procedure mettre_au_point(niveau_trace : positive ; S : string) is
   begin
      if niveau_trace <= trace then
         put_line(S) ;
      end if ;
   end mettre_au_point ;

   procedure tester(B : boolean) is
   begin
      if defensif and then B then
         raise erreur_table ;
      end if ;
   end tester ;

   -- Conversion en minuscules
   procedure normaliser(S : in out string) is
      diff : constant := character'pos('A')-character'pos('a');
      car : character;
   begin
      for i in S'range loop
         car := S(i);
         if car in 'A'..'Z' then 
            car := character'val(character'pos(car)-diff); 
         end if;
         S(i) := car;
      end loop;
   end normaliser;


   procedure RD(A : in out element_table) is
      aux : element_table ;
   begin
      aux := A.gauche ;
      A.gauche := aux.droit ;
      aux.droit := A ;
      A := aux ;
   end RD ;

   procedure RG(A : in out element_table) is
      aux : element_table ;
   begin
      aux := A.droit ;
      A.droit := aux.gauche ;
      aux.gauche := A ;
      A := aux ;
   end RG ;

   procedure RGD(A : in out element_table) is
   begin
      RG(A.gauche) ;
      RD(A) ;
   end RGD ;

   procedure RDG(A : in out element_table) is
   begin
      RD(A.droit) ;
      RG(A) ;
   end RDG ;

   function creation_table return table is
   begin
      return new structure_table ;
   end creation_table ;
     
   procedure chercher_string(T : in table ;
                             S : in string ;
                             a_creer : in boolean ;
                             present : out boolean ;
                             E : out element_table) is
      pere, cour, nouveau : element_table ;
      A, AA : element_table ;
      existe : boolean ;
      M : string(S'range) := S ;

   begin
      tester((S'length = 0) or T = table_vide) ;
      mettre_au_point(1, "table : recherche de la string " & S) ;
      normaliser(M) ;
      mettre_au_point(1, "debut du parcours de l'arbre") ;
      pere := null ;
      cour := T.racine ;
      A := cour ;
      AA := pere ;
      while (cour /= null) and then (M /= acces_string(cour.C)) loop
         if cour.deseq /= 0 then
            -- tester((cour.deseq = 2) or (cour.deseq = -2)) ;
            A := cour ;
            AA := pere ;
         end if ;
         pere := cour ;
         if M < acces_string(cour.C) then
            cour := cour.gauche ;
         else
            cour := cour.droit ;
         end if ;
      end loop ;
      mettre_au_point(1, "fin du parcours de l'arbre") ;
      if cour = null then
         existe := false ;
         if a_creer then
            nouveau := new structure_element_table'(creation(M),
                                                    info_vide, 0, 
                                                    null, null) ;
            if pere = null then
               T.racine := nouveau ;
            else 
               -- on insere
               mettre_au_point(1, "insertion") ;
               if M < acces_string(pere.C) then
                  pere.gauche := nouveau ;
               else
                  pere.droit := nouveau ;
               end if ;
               -- on modifie les desequilibres
               mettre_au_point(1, "modif deseq") ;
               cour := A ;
               while cour /= nouveau loop
                  if M < acces_string(cour.C) then
                     mettre_au_point(1, "a gauche");
                     -- tester(cour.deseq >= 2) ;
                     cour.deseq := cour.deseq + 1 ;
                     cour := cour.gauche ;
                     mettre_au_point(1, "fin a gauche");
                  else
                     mettre_au_point(1, "a droite");
                     -- tester(cour.deseq <= -2) ;
                     cour.deseq := cour.deseq - 1 ;
                     cour := cour.droit ;
                     mettre_au_point(1, "fin a droite");
                  end if ;
               end loop ;
               -- on reequilibre
               mettre_au_point(1, "reequilibrage");
               case A.deseq is
                  when -1 | 0 | 1 => null ;
                  when 2 =>
                     case A.gauche.deseq is
                        when 1 =>
                           RD(A) ;
                           A.deseq := 0 ;
                           A.droit.deseq := 0 ;
                        when -1 =>
                           RGD(A) ;
                           case A.deseq is
                              when 1 =>
                                 A.gauche.deseq := 0 ;
                                 A.droit.deseq := -1 ;
                              when -1 =>
                                 A.gauche.deseq := 1 ;
                                 A.droit.deseq := 0 ;
                              when 0 =>
                                 A.gauche.deseq := 0 ;
                                 A.droit.deseq := 0 ;
                              when others =>
                                 mettre_au_point(2, "deseq impossible !") ;
                           end case ;
                           A.deseq := 0 ;
                        when others =>
                           mettre_au_point(2, "deseq impossible !") ;
                     end case ;
                  when -2 =>
                     case A.droit.deseq is
                        when -1 =>
                           RG(A) ;
                           A.deseq := 0 ;
                           A.gauche.deseq := 0 ;
                        when 1 =>
                           RDG(A) ;
                           case A.deseq is
                              when -1 =>
                                 A.droit.deseq := 0 ;
                                 A.gauche.deseq := 1 ;
                              when 1 =>
                                 A.droit.deseq := -1 ;
                                 A.gauche.deseq := 0 ;
                              when 0 =>
                                 A.droit.deseq := 0 ;
                                 A.gauche.deseq := 0 ;
                              when others =>
                                 mettre_au_point(2, "deseq impossible !") ;
                           end case ;
                           A.deseq := 0 ;
                        when others =>
                           mettre_au_point(2, "deseq impossible !") ;
                     end case ;
               end case ;
               mettre_au_point(1, "fin du reequilibrage") ;
               if AA = null then
                  T.racine := A ;
               elsif acces_string(A.C) < acces_string(AA.C) then
                  AA.gauche := A ;
               else
                  AA.droit := A ;
               end if ;
            end if ;
            E := nouveau ;
         else
            E := null ;
         end if ;
      else
         existe := true ;
         E := cour ;
      end if ;
         present := existe ;
         if existe then
            mettre_au_point(1, "        ... string trouvee") ;
         else
            mettre_au_point(1, "        ... string non trouvee") ;
         end if ;
   end chercher_string ;

   procedure chercher_chaine(T : in table ;
                             C : in chaine ;
                             a_creer : in boolean ;
                             present : out boolean ;
                             E : out element_table) is
   begin
      chercher_string(T, acces_string(C), a_creer, present, E) ;
   end chercher_chaine ;

   function acces_chaine(E : element_table) return chaine is
   begin
      return E.C ;
   end acces_chaine ;

   function acces_info(E : element_table) return info is
   begin
      return E.I ;
   end acces_info ;

   procedure changer_info(E : in element_table ; I : in info) is
   begin
      E.I := I ;
   end changer_info  ;


   procedure parcourir_table(T : in table) is
      -- E, Eprec, Fictif : element_table ;
      procedure parcours (E : in element_table) is
      begin
         -- tant qu'a faire, on parcours dans l'ordre alphanumerique
         if E /= null then
            parcours(E.gauche) ;
            traiter(E) ;
            mettre_au_point(1, "desequilibre " & Desequilibre'image(E.deseq)) ;
            parcours(E.droit) ;
         end if ;
      end parcours ;

   begin -- parcourir_table 
      mettre_au_point(1, "debut parcourir_table") ;
      tester(T = table_vide) ;
--      E := T.racine ;
--      if E = null then 
--         return ;
--      end if ;
--      Fictif := new structure_element_table ;
--      Fictif.gauche := E ; -- ou bien on accroche a droite, c'est pareil
--      E.pere := Fictif ;
--      Eprec := Fictif ;
--      loop
--         if Eprec = E.droit then -- il faut remonter
--            mettre_au_point(2, "remontee");
--            Eprec := E ;
--            E := E.pere ;
--         elsif Eprec = E.gauche or  -- on vient de remonter de la gauche ou
--               E.gauche = null then -- il n'y a rien a gauche
--            traiter(E) ;
--            Eprec := E ;
--            if E.droit = null then -- il faut remonter
--               mettre_au_point(2, "remontee") ;
--               E := E.pere ;
--            else -- il faut descendre dans le droit
--               mettre_au_point(2, "descente a droite") ;
--               E := E.droit ;
--            end if ;
--         else -- dernier cas : on vient de descendre, et il y a qq'un a gauche
--            mettre_au_point(2, "descente a gauche") ;
--            Eprec := E ;
--            E := E.gauche ;
--         end if ;
--      exit when E = Fictif ;
--      end loop ;
--      Eprec.pere := null ; -- C'est plus propre (Eprec = T.racine)
      parcours(T.racine) ;
      
      mettre_au_point(1, "fin de parcourir_table") ;
   end parcourir_table ;
end Mes_Tables ;

