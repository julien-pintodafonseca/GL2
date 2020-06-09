-------------------------------------------------------------------------------
--  Pseudo_Code : spécification du paquetage
--
--  Auteur : un enseignant du projet GL
--  Affiliation : Grenoble INP (Ensimag)
--
--  Historique :
--     01/01/2020
--        - version initiale
-------------------------------------------------------------------------------

--|  Primitives pour produire du code machine abstraite
--|  (types abstraits Etiq, Operande, Inst, Ligne).

--|  Ce module est réutilisé par l'Interprète de la Machine Abstraite,
--|  c'est pourquoi il definit plus de primitives que celles
--|  qui vous seront nécessaires.

--|  Une Etiq est une étiquette de ligne, caracterisée par une chaîne
--|  de caractères.  Une étiquette est dite "définie" quand elle est
--|  associée à une ligne. Une étiquette ne peut pas être simultanément
--|  associée à plusieurs lignes.

--|  Un Operande est un opérande d'instruction. Ce peut être :
--|     un registre banalisé
--|     un mot désigné par adressage indirect + déplacement
--|     un mot désigné par adressage indirect indexé + déplacement
--|     une valeur immédiate (Entier, Flottant, Chaine)
--|     une étiquette.

--|  Une Inst est une instruction de la machine abstraite.

--|  Une Ligne est une ligne de programme en langage d'assemblage, constituée de
--|     une définition d'étiquette
--|     une instruction
--|     un commentaire
--|     un accès à la ligne suivante
--|     un numéro de ligne (inutile pour vous).
--|  Chacune de ces informations peut être vide.

--|  A chacun de ces types est associée une procédure d'impression
--|  sur le flot standard de sortie.
--|  Il suffit donc de rediriger ce dernier et d'utiliser "Afficher_Programme"
--|  pour obtenir un fichier en langage d'assemblage de la machine abstraite.

with Types_Base;
use  Types_Base;

package Pseudo_Code is

   -- Un déplacement est un entier utilisé dans les adressages indirects.
   subtype Deplacement is Entier;

   -- Les registres dénotables.
   type Registre is (GB, LB, SP, R0,  R1,  R2,  R3,  R4,  R5,  R6,  R7,
                                 R8,  R9,  R10, R11, R12, R13, R14, R15);

   -- Seuls les registres banalisés sont des opérandes d'instructions
   subtype Banalise is Registre range R0 .. R15;

   -- ***** TYPE Etiq *****

   type St_Etiq (<>) is limited private;
   type Etiq is access all St_Etiq;

   -- ***** TYPE Operande *****

   type Nature_Operande is
      (Op_Direct, Op_Indirect, Op_Indexe,
       Op_Entier, Op_Flottant, Op_Null, Op_Chaine, Op_Etiq);

   type St_Operande (<>) is limited private;
   type Operande is access all St_Operande;

   -- ***** TYPE Inst *****

   type Code_Operation is
      (-- 0 opérande
       Code_RTS, Code_RINT, Code_RFLOAT, Code_WINT,
       Code_WFLOAT, Code_WFLOATX, Code_WNL,
       Code_RUTF8, Code_WUTF8, Code_SCLK, Code_CLK,
       Code_SETROUND_TONEAREST,
       Code_SETROUND_UPWARD,
       Code_SETROUND_DOWNWARD,
       Code_SETROUND_TOWARDZERO,
       Code_ERROR, Code_HALT,
       -- 1 opérande de type Etiq
       Code_BSR, Code_BRA, Code_BEQ, Code_BNE, Code_BGT, Code_BLT,
       Code_BGE, Code_BLE, Code_BOV,
       -- 1 opérande
       Code_SEQ, Code_SNE, Code_SGT, Code_SLT, Code_SGE, Code_SLE, Code_SOV,
       Code_ADDSP, Code_SUBSP, Code_PEA, Code_PUSH, Code_POP,
       Code_TSTO, Code_DEL, Code_SHL, Code_SHR, Code_WSTR,
       -- 2 opérandes
       Code_NEW,
       Code_LOAD, Code_STORE, Code_LEA,
       Code_ADD, Code_SUB, Code_MUL, Code_OPP, Code_CMP, Code_DIV, Code_FMA, Code_INT,
       Code_QUO, Code_REM, Code_FLOAT);

   type St_Inst (<>) is limited private;
   type Inst is access all St_Inst;

   -- TYPE Ligne

   type St_Ligne (<>) is limited private;
   type Ligne is access all St_Ligne;

   -- ***** OPERATIONS SUR LE TYPE Etiq *****

   function L_Etiq (S : String) return Etiq;
   -- Retourne l'étiquette de string S
   -- (en la créant si cela n'est pas déjà fait).
   -- Deux appels avec la même string retournent la même étiquette.

   function L_Etiq_Num (S : String; N : Natural) return Etiq;
   -- Retourne l'étiquette dont la string est la concaténation de S, de '.' et
   -- des caractères de la représentation décimale de N.
   -- Deux appels avec les mêmes S et N retournent la même étiquette.

   function Acces_String (E : not null access St_Etiq) return String;

   function Acces_Ligne (E : not null access St_Etiq) return Ligne;
   -- retourne la ligne associée à l'étiquette E, si E est définie,
   -- retourne null si E n'est pas définie.

   procedure Afficher (E : not null access St_Etiq);

   -- ***** OPERATIONS SUR LE TYPE Operande *****
   -- Les sélecteurs et mutateurs ont des préconditions évidentes
   -- sur la nature de l'opérande

   -- Chaque opérande de nature registre direct existe en un seul exemplaire,
   -- accessibles par :
   function Le_Registre (R : Banalise) return Operande;

   function Creation_Op_Indirect (D : Deplacement;
                                  Base : Registre) return Operande;

   function Creation_Op_Indexe (D : Deplacement;
                                Base : Registre;
                                Index : Banalise) return Operande;

   function Creation_Op_Entier (V : Entier) return Operande;

   function L_Op_Null return Operande;

   function Creation_Op_Flottant (V : Flottant) return Operande;

   -- La deuxième forme de Creation_Op_Flottant sert uniquement
   -- dans la machine abstraite
   function Creation_Op_Flottant (V : Flottant; F : Flot_MA) return Operande;

   function Creation_Op_Chaine (V : not null access St_Chaine) return Operande;

   function Creation_Op_Etiq (E : not null access St_Etiq) return Operande;

   function Acces_Nature (Op : not null access St_Operande) return Nature_Operande;

   function Acces_Registre (Op : not null access St_Operande) return Banalise;
   -- Pour les opérandes de nature registre direct

   function Acces_Deplacement (Op : not null access St_Operande) return Deplacement;

   function Acces_Base (Op : not null access St_Operande) return Registre;
   -- Pour les opérandes de nature indirect ou indexe

   function Acces_Index (Op : not null access St_Operande) return Banalise;

   function Acces_Entier (Op : not null access St_Operande) return Entier;

   function Acces_Flottant (Op : not null access St_Operande) return Flottant;

   function Acces_Flot_MA (Op : not null access St_Operande) return Flot_MA;

   function Acces_Chaine (Op : not null access St_Operande) return Chaine;

   function Acces_Etiq (Op : not null access St_Operande) return Etiq;

   procedure Changer_Deplacement (Op : not null access St_Operande;
                                  Nouveau_Dep : in Deplacement);

   procedure Changer_Base (Op : not null access St_Operande;
                           Nouvelle_Base : in Registre);

   procedure Changer_Index (Op : not null access St_Operande;
                            Nouvel_Index : in Banalise);

   procedure Changer_Entier (Op : not null access St_Operande;
                             Nouvel_Entier : in Entier);

   procedure Changer_Flottant (Op : not null access St_Operande;
                               Nouveau_Flottant : in Flottant);

   procedure Changer_Chaine (Op : not null access St_Operande;
                             Nouvelle_Chaine : not null access St_Chaine);

   procedure Changer_Etiq (Op : not null access St_Operande;
                           Nouvelle_Etiq : not null access St_Etiq);

   procedure Afficher (Op : not null access St_Operande);


   -- ***** OPERATIONS SUR LE TYPE Inst *****
   -- Préconditions évidentes pour les sélecteurs et mutateurs

   function Creation_Inst0 (Code : Code_Operation) return Inst;

   function Creation_Inst1 (Code : Code_Operation;
                            Op1 : not null access St_Operande) return Inst;

   function Creation_Inst2 (Code : Code_Operation;
                            Op1, Op2 : not null access St_Operande) return Inst;

   function Acces_Code_Operation (I : not null access St_Inst) return Code_Operation;

   function Acces_Op1 (I : not null access St_Inst) return Operande;

   function Acces_Op2 (I : not null access St_Inst) return Operande;

   procedure Changer_Op1 (I : not null access St_Inst;
                          Nouvel_Op : not null access St_Operande);

   procedure Changer_Op2 (I : not null access St_Inst;
                          Nouvel_Op : not null access St_Operande);

   procedure Afficher (I : not null access St_Inst);


   -- ***** OPERATIONS SUR LE TYPE Ligne *****

   function Creation (E : Etiq := null;
                      I : Inst := null;
                      Com : String := "";
                      Suiv : Ligne := null;
                      Num_Ligne : positive := 1) return Ligne;
   -- lève l'exception Erreur_Double_Def si E (si présente) est déjà définie.

   function Acces_Etiq (L : not null access St_Ligne) return Etiq;

   function Acces_Inst (L : not null access St_Ligne) return Inst;

   function Acces_Comment (L : not null access St_Ligne) return String;

   function Acces_Suivant (L : not null access St_Ligne) return Ligne;

   function Acces_Num_Ligne (L : not null access St_Ligne) return Positive;

   procedure Changer_Etiq (L : not null access St_Ligne;
                           Nouvelle_Etiq : in Etiq);

   procedure Changer_Inst (L : not null access St_Ligne;
                           Nouvelle_Inst : in Inst);

   procedure Changer_Comment (L : not null access St_Ligne;
                              Nouveau_Com : in String);

   procedure Changer_Suivant (L : not null access St_Ligne;
                              Nouveau_Suiv : in Ligne);

   procedure Changer_Num_Ligne (L : not null access St_Ligne;
                                Nouveau_Num : in Positive);

   procedure Afficher (L : not null access St_Ligne);
   -- affiche la ligne L (sans afficher les lignes suivantes)


   -- ***** AFFICHAGE D'UN Programme *****

   procedure Afficher_Programme (L : not null access St_Ligne);
   -- affiche la ligne L ainsi que les lignes suivantes


   -- ***** EXCEPTIONS *****

   -- exceptions levées en cas d'erreur d'utilisation des types abstraits
   Erreur_Etiq, Erreur_Operande, Erreur_Inst, Erreur_Ligne : exception;

   -- exception levée si deux lignes définissent la même étiquette
   Erreur_Double_Def : exception;

   -- exception levée si l'on tente de créer une étiquette correspondant
   -- à un nom réservé de la machine abstraite
   Erreur_Etiq_Illegale : exception;

private

   type St_Etiq is record
      Reserve : Boolean;
      Nom : Chaine;
      Def : Ligne;
   end record;

   type St_Operande (Nature : Nature_Operande) is record
      case Nature is
         when Op_Direct =>
            Reg : Banalise;
         when Op_Indirect | Op_Indexe =>
            Dep : Deplacement;
            Base : Registre;
            case Nature is
               when Op_Indexe =>
                  Index : Banalise;
               when Op_Indirect =>
                  null;
               when others =>  --  Impossible mais obligatoire
                  null;
            end case;
         when Op_Entier =>
            Val_Ent : Entier;
         when Op_Flottant =>
            Val_Flottant : Flottant;
            Val_Flot_MA : Flot_MA;
         when Op_Null =>
            null;
         when Op_Chaine =>
            Val_Chaine : Chaine;
         when Op_Etiq =>
            Val_Etiq : Etiq;
      end case;
   end record;

   type St_Inst (Codop : Code_Operation) is record
      case Codop is
         when Code_RTS .. Code_HALT =>  --  0 opérande
            null;
         when Code_BSR .. Code_FLOAT =>  --  au moins un opérande
            Op1 : Operande;
            case Codop is
               when Code_BSR .. Code_WSTR =>  --  un opérande
                  null;
               when Code_NEW .. Code_FLOAT =>  --  deux opérandes
                  Op2 : Operande;
               when others =>  --  impossible, mais oligatoire
                  null;
            end case;
      end case;
   end record;

   type Ptr_Comment is access String;

   type St_Ligne is record
      Def_Etiq : Etiq := null;
      Val_Inst : Inst := null;
      Comment : Ptr_Comment := null;
      Suiv : Ligne := null;
      Num_Ligne : Positive := 1;
   end record;

end Pseudo_Code;
