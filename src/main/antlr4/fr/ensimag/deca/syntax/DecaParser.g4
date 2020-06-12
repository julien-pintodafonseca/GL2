parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.tools.SymbolTable;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
            assert($dv1.tree != null);
            $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            assert($dv2.tree != null);
            $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
            AbstractInitialization init;
        }
    : i=ident {
            init = new NoInitialization();
        }
      (eq=EQUALS e=expr {
            assert($e.tree!=null);
            init = new Initialization($e.tree);
            setLocation(init, $eq);
        }
      )? {
            assert($t != null);
            assert($i.tree !=null);
            assert(init != null);
            $tree = new DeclVar($t, $i.tree, init);
            setLocation($tree, $i.start);
            setLocation($i.tree, $i.start);
        }
    ;

list_inst returns[ListInst tree]
@init   {
            $tree = new ListInst();
        }
    : (inst {
            assert($inst.tree != null);
            $tree.add($inst.tree);
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
        }
    | SEMI {
            $tree = new NoOperation();
        }
    | token=PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $token);
        }
    | token=PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false, $list_expr.tree);
            setLocation($tree, $token);
        }
    | token=PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $token);
        }
    | token=PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $token);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
            setLocation($tree, $if_then_else.start);
        }
    | token=WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
            setLocation($tree, $token);
        }
    | token=RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $token);
        }
    ;

if_then_else returns[IfThenElse tree]
@init   {
            ListInst elseInst = new ListInst();
            ListInst elsifInst = new ListInst();
            IfThenElse ifElse;
        }
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
            $tree = new IfThenElse($condition.tree, $li_if.tree, elseInst);
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
            assert($elsif_cond.tree != null);
            assert($elsif_li.tree != null);
            ifElse = new IfThenElse($elsif_cond.tree, $elsif_li.tree, elsifInst);
            elseInst.add(ifElse);
            setLocation(ifElse, $elsif);
            elseInst = elsifInst;
            elsifInst = new ListInst();
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
            for (AbstractInst tt : $li_else.tree.getList()) {
                assert(tt != null);
                elseInst.add(tt);
            }
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {
            $tree = new ListExpr();
        }
    : (e1=expr {
            assert($e1.tree != null);
            $tree.add($e1.tree);
        }
       (COMMA e2=expr {
            assert($e2.tree != null);
            $tree.add($e2.tree);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree;
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        token=EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue)$e.tree, $e2.tree);
            setLocation($tree, $token);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=or_expr token=OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $token);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    |  e1=and_expr token=AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=eq_neq_expr token=EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=eq_neq_expr token=NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=inequality_expr token=LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=inequality_expr token=GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=inequality_expr token=GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=inequality_expr token=LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            $tree = new InstanceOf($e1.tree, $type.tree);
            setLocation($tree, $INSTANCEOF);
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=sum_expr token=PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=sum_expr token=MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=mult_expr token=TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=mult_expr token=SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    | e1=mult_expr token=PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $token);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $op);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $op);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
            setLocation($tree, $e1.start);
        }
        | /* epsilon */ {
            $tree = new Selection($e1.tree, $i.tree);
            setLocation($tree, $DOT);
            // we matched "e.i"
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            $tree = new MethodCall(new This(true), $m.tree, $args.tree);
            setLocation($tree, $m.start);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
        }
    | token=READINT OPARENT CPARENT {
            $tree = new ReadInt();
            setLocation($tree, $token);
        }
    | token=READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
            setLocation($tree, $token);
        }
    | token=NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $token);
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $cast);
        }
    | literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
            setLocation($tree, $literal.start);
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    ;

literal returns[AbstractExpr tree]
    : INT {
            $tree = new IntLiteral(Integer.parseInt($INT.text));
        }
    | fd=FLOAT {
            $tree = new FloatLiteral(Float.parseFloat($fd.text));
        }
    | STRING {
            $tree = new StringLiteral($STRING.text.substring(1, $STRING.text.length()-1));
        }
    | TRUE {
    		$tree = new BooleanLiteral(true);
        }
    | FALSE {
    		$tree = new BooleanLiteral(false);
        }
    | THIS {
            $tree = new This(false);
        }
    | NULL {
            $tree = new NullLiteral();
        }
    ;

ident returns[AbstractIdentifier tree]
    : token=IDENT {
            $tree = new Identifier(
                getDecacCompiler().createSymbol($token.text)
            );
            setLocation($tree, $token);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
@init   {
            $tree = new ListDeclClass();
        }
    :
      (c1=class_decl {
            assert($c1.tree != null);
            $tree.add($c1.tree);
        }
      )*
    ;

class_decl returns[AbstractDeclClass tree]
    : token=CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
            assert($name.tree != null);
            assert($superclass.tree != null);
            assert($class_body.fieldtree != null);
            assert($class_body.methodtree != null);
            $tree = new DeclClass($name.tree, $superclass.tree, $class_body.fieldtree, $class_body.methodtree);
            setLocation($tree, $token);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | /* epsilon */ {
            $tree = new Identifier(getDecacCompiler().createSymbol("Object"));
        }
    ;

class_body returns[ListDeclField fieldtree, ListDeclMethod methodtree]
@init {
        $methodtree = new ListDeclMethod();
        $fieldtree = new ListDeclField();
}
    : (m=decl_method {
            assert($decl_method.tree != null);
            $methodtree.add($decl_method.tree);
        }
      | decl_field_set[$fieldtree] {
            assert($decl_field_set.tree != null);
            $fieldtree = $decl_field_set.tree;
      }
      )*
    ;

decl_field_set[ListDeclField fieldtree] returns[ListDeclField tree]
    : v=visibility t=type list_decl_field[$v.tree, $t.tree, $fieldtree]
      SEMI {
            assert($list_decl_field.tree != null);
            $tree = $list_decl_field.tree;
      }
    ;

visibility returns[Visibility tree]
    : /* epsilon */ {
            $tree = Visibility.PUBLIC;
        }
    | PROTECTED {
            $tree = Visibility.PROTECTED;
        }
    ;

list_decl_field[Visibility v, AbstractIdentifier t, ListDeclField fieldtree] returns[ListDeclField tree]
@init {
        assert($fieldtree != null);
}
    : dv1=decl_field[$v, $t] {
        assert($dv1.tree != null);
        $fieldtree.add($dv1.tree);
    }
        (COMMA dv2=decl_field[$v, $t] {
            assert($dv2.tree != null);
            $fieldtree.add($dv2.tree);
        }
      )*
      {
            $tree = $fieldtree;
      }
    ;

decl_field[Visibility v, AbstractIdentifier t] returns[AbstractDeclField tree]
@init {
        AbstractInitialization init;
}
    : i=ident {
            init = new NoInitialization();
        }
      (EQUALS e=expr {
            assert($e.tree != null);
            init = new Initialization($e.tree);
        }
      )? {
            assert($v != null);
            assert($t != null);
            $tree = new DeclField($v, $t, $i.tree, init);
            setLocation($tree, $i.start);
        }
    ;

decl_method returns[DeclMethod tree]
@init {
        AbstractMethodBody methodBody;
}
    : type ident OPARENT params=list_params CPARENT (block {
            assert($type.tree != null);
            assert($ident.tree != null);
            assert($params.tree != null);
            assert($block.decls != null);
            assert($block.insts != null);
            methodBody = new MethodBody($block.decls, $block.insts);
            setLocation(methodBody, $block.start);

            // à mettre à la fin lorsque MethodBodyASM sera créée
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, methodBody);
            setLocation($tree, $type.start);
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
        }
      ) {

        }
    ;

list_params returns[ListDeclParam tree]
@init {
    $tree = new ListDeclParam();
}
    : (p1=param {
           assert($p1.tree != null);
           $tree.add($p1.tree);
        } (COMMA p2=param {
            assert($p2.tree != null);
            $tree.add($p2.tree);
            }
        )*
      )?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[AbstractDeclParam tree]
    : type ident {
            assert($type.tree != null);
            assert($ident.tree != null);
            $tree = new DeclParam($type.tree, $ident.tree);
            setLocation($tree, $type.start);
        }
    ;
