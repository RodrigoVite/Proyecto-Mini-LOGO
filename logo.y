/** Proyecto: Mini-LOGO
**  Integrantes:
	-Estrada Gonzalez David
  -Molina Santiago Isaac
	-Solís Ávila Angélica
	-Vite Cruz Rodrigo
**  Grupo: 3CM5
**  Materia: Compiladores
**  Fecha: Enero de 2021
**/
%{
	import java.lang.Math;
	import java.io.*;
	import java.util.StringTokenizer;
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
%}

/** Simbolos gramaticales generales y para LOGO **/
%token  NUMBER PRINT VAR BLTIN INDEF WHILE IF ELSE FOR
%token  FUNCTION PROCEDURE RETURN FUNC PROC FORWARD UP DOWN GD GI COLOR
%token  ARG

/** Jerarquía de operadores **/
%right '='
%left OR
%left AND
%left GT GE LT LE EQ NE
%left '+' '-'
%left '*' '/'
%left UNARYMINUS NOT
%right '^'

/** Gramática - Reglas **/
%%
list:   
    | list '\n'  
    | list oper '\n'
    ;

oper: defn ';' 		            { maq.code("STOP");return 1 ;}
    | stmt  		              { maq.code("STOP"); return 1 ;}
    | expr ';' 		            { maq.code("print"); maq.code("STOP"); return 1 ;} 
  	;

asgn: VAR '=' expr 	          { $$=$3; 
					                      maq.code("varpush"); 
                                maq.code(((Algo)$1.obj).simb); 
		  			                    maq.code("assign");}
    | ARG '=' expr 	          { defnonly("$");     
        			                  //code2(argassign,(Inst)$1); 
        			                  maq.code("argassign"); 
        			                  maq.code($1.ival+"");
        			                  $$=$3;}
    ;

expr:     NUMBER  				    { ((Algo)$$.obj).inst=maq.code("constpush");
                						    maq.code(((Algo)$1.obj).simb); }
    | VAR 		    						{ ((Algo)$$.obj).inst=maq.code("varpush");
                						    maq.code(((Algo)$1.obj).simb); maq.code("eval");}
    | ARG       							{ defnonly("$"); 
            								    //$$ = code2(arg, (Inst)$1);      
    								            //erroneo ((Algo)$$.obj).inst= maq.code("arg"); 
    								            $$ = new ParserVal(new Algo(maq.code("arg"))); 
    								            maq.code($1.ival+"");}
    | asgn

    /*** COMANDOS BÁSICOS LOGO ***/
    | FORWARD expr 						{ $$=$2; maq.code("forward");}
    | UP expr 							  { $$=$2; maq.code("upPencil");}
    | DOWN expr 						  { $$=$2; maq.code("downPencil");} 
    | GD expr 							  { $$=$2; maq.code("girarDerecha");}
    | GI expr 			          { $$=$2; maq.code("girarIzquierda");}
    | COLOR '[' expr ',' expr ',' expr ']' 	{ $$=$2; maq.code("color");}
    /************************/

    | FUNCTION begin '(' arglist ')'	{ $$ = $2; 
            							              maq.code("call"); 
            							              maq.code(((Algo)$1.obj).simb); 
            							              maq.code($4.ival+"");
            							        	  } 
    | BLTIN '(' expr ')'			{ $$=$3; 
            									  maq.code("bltin"); 
            									  maq.code(((Algo)$1.obj).simb); 
            									}
    | expr '+' expr 					{ maq.code("add"); }
    | expr '-' expr 					{ maq.code("sub"); }
    | expr '*' expr 					{ maq.code("mul"); }
    | expr '/' expr 					{ maq.code("div"); }
    | expr '^' expr 					{ maq.code("power"); }	
    | '(' expr ')'  					{ $$= $2;}
    | '-' expr %prec UNARYMINUS 		{ $$=$2; maq.code("negate");}
    | expr EQ expr  					{ maq.code("eq"); }
    | expr NE expr  					{ maq.code("ne"); }
    | expr GT expr  					{ maq.code("gt"); }
    | expr GE expr  					{ maq.code("ge"); }
	  | expr LT expr 					  { maq.code("lt"); }
    | expr LE expr  					{ maq.code("le"); }
	  | expr AND expr 					{ maq.code("and"); }
	  | expr OR expr  					{ maq.code("or"); }
	  | NOT expr      					{ $$ = $2; maq.code("not"); }
	  ;

stmt: expr ';'  						  { maq.code("pop");}
   	| RETURN ';' 						  { defnonly("return"); 
   						  				        maq.code("procret");} 
   	| RETURN expr ';'					{ defnonly( "return" ); 
				   						          $$ = $2; 
				   						          maq.code("funcret");} 
   	| PROCEDURE begin '(' arglist ')'	{ $$ = $2; 
            							              maq.code("call"); 
            							              maq.code(((Algo)$1.obj).simb); 
            							              maq.code($4.ival+"");
            							            }  ';'
   	| PRINT prlist ';' 				{ $$ = $2; }
   	| for '(' forexp ';' condFor ';' forexp ')' stmt end 	{ maq.getProg().setElementAt(
													                                     new Integer(((Algo)$5.obj).inst),
													                                     ((Algo)$1.obj).inst+1); 
                        													      	  maq.getProg().setElementAt(
                    													                 new Integer(((Algo)$7.obj).inst),
                    													                 ((Algo)$1.obj).inst+2);
                        													    	    maq.getProg().setElementAt(
                    													                 new Integer(((Algo)$9.obj).inst),
                    													                 ((Algo)$1.obj).inst+3); 
                        													    	    maq.getProg().setElementAt(
                    													                 new Integer(((Algo)$10.obj).inst),
                    													                 ((Algo)$1.obj).inst+4); 
                        													   		  }
    | while cond stmt end 		{ maq.getProg().setElementAt(
    							                 new Integer(((Algo)$3.obj).inst),
    							                 ((Algo)$1.obj).inst+1); 
						             	      maq.getProg().setElementAt(
    							                 new Integer(((Algo)$4.obj).inst),
    							                 ((Algo)$1.obj).inst+2); 
					 					          }    
    | if cond stmt end 				{ maq.getProg().setElementAt(
        							             new Integer(((Algo)$3.obj).inst),
        							             ((Algo)$1.obj).inst+1); 
							                  maq.getProg().setElementAt(
      								             new Integer(((Algo)$4.obj).inst),
      								             ((Algo)$1.obj).inst+3); 
							                }  
	  | if cond stmt end ELSE stmt end 	{ maq.getProg().setElementAt(
          							                 new Integer(((Algo)$3.obj).inst),
          							                 ((Algo)$1.obj).inst+1); 
          						                  maq.getProg().setElementAt(
          							                 new Integer(((Algo)$6.obj).inst),
          							                 ((Algo)$1.obj).inst+2);             
          						                  maq.getProg().setElementAt(
          							                 new Integer(((Algo)$7.obj).inst),
          							                 ((Algo)$1.obj).inst+3);  
          	  									      } 
	  |   '{'  stmtlist  '}' 		{ $$  =  $2;}
    ;

cond: '(' expr ')'  					{ maq.code("STOP");  
              								  ((Algo)$$.obj).inst = ((Algo)$2.obj).inst;//checar
          									  }
	  ;

while:	WHILE   						  { $$ = new ParserVal(new Algo(maq.code("whilecode"))); 
              								  maq.code("STOP");maq.code("STOP");
              								}
	   ;

for:  FOR  								    { $$ = new ParserVal(new Algo(maq.code("forcode"))); 
      								          maq.code("STOP");maq.code("STOP");maq.code("STOP");maq.code("STOP");
      								        }
   ;

if:		IF   							      { //$$ = code(ifcode); code3(STOP,STOP,STOP);
        										    $$ = new ParserVal(new Algo(maq.code("ifcode"))); 
              								  maq.code("STOP");maq.code("STOP");maq.code("STOP");
              								}
  ;

begin:  /* nada */          	{ $$=new ParserVal(new Algo(maq.getProgP()));
        								        //$$ = new ParserVal(maq.getProgP()); 
        								      }
   	 ;

condFor:  iniFor expr   			{ maq.code("STOP");  
        								        ((Algo)$$.obj).inst = ((Algo)$1.obj).inst; //checar
    									        }
       ;

iniFor:	/* nada */						{ $$=new ParserVal(new Algo(maq.getProgP()));
        								        //$$ = new ParserVal(maq.getProgP());
        								      }
	    ;

forexp: /* nada */						{ maq.code("STOP");  $$ = new ParserVal(0); }
      | iniFor expr 					{ maq.code("STOP");  
        								        ((Algo)$$.obj).inst = ((Algo)$1.obj).inst; //checar
    									        }
	    ;

end:    /* nada */						{ maq.code("STOP"); 
           										  $$=new ParserVal(new Algo(maq.getProgP()));
           										  //$$=new ParserVal(maq.getProgP());        
              								}
   ;

stmtlist: 	/* nada */				{ $$=new ParserVal(new Algo(maq.getProgP()));          
										            //$$=new ParserVal(maq.getProgP());  
        								      }
    		| stmtlist '\n'
    		| stmtlist stmt
    		;

prlist: expr               		{ maq.code("prexpr"); }
   	  | prlist ',' expr    		{ maq.code("prexpr"); }
   	  ;

defn:   FUNC procname 				{ ((Algo)$2.obj).simb.tipo=FUNCTION; indef=true; }
		    '(' ')' stmt 					{ maq.code("procret"); maq.define(((Algo)$2.obj).simb); indef=false; } 
    |   PROC procname 				{ ((Algo)$2.obj).simb.tipo=PROCEDURE; indef=true; }
		    '(' ')' stmt 					{ maq.code("procret"); maq.define(((Algo)$2.obj).simb); indef=false; }
    ;

procname: VAR
     		| FUNCTION 
     		| PROCEDURE
     		;

arglist:  	/* nada */   			{ $$ = new ParserVal(0); }
   	   | 	expr                { $$ = new ParserVal(1); }
   	   | 	arglist ',' expr    { $$ =  new ParserVal($1.ival + 1); }
   	   ;

%%

/** Código de soporte en Java **/
class Algo {
	Simbolo simb;
	int inst;

	public Algo(int i){
		inst=i;
	}
	public Algo(Simbolo s, int i){
		simb=s;
                inst=i;
	}
}

public void setTokenizer(StringTokenizer st){
  	this.st= st;
}

public void setNewline(boolean newline){
  	this.newline= newline;
}

boolean indef;
static Tabla tabla;
static Maquina maq;

static String ins;
static StringTokenizer st;

void yyerror(String s){
 	System.out.println("parser:"+s);
}

void defnonly(String s){
	if(!indef)
		System.out.println(s+" usado fuera de la definicion");
}

static boolean newline;
int yylex(){
    String s;
    int tok = 0;
    Double d;
    Simbolo simbo;
    if (!st.hasMoreTokens())                                          //Revisa por nueva linea
      	if (!newline){
        	newline=true;
        	return '\n'; 
      	}
    else
        return 0;	
    s = st.nextToken();
    //System.out.println("tok act: ("+s+" )");
    try{                                                              //Es un digito
      	d = Double.valueOf(s);
      	yylval = new ParserVal( new Algo(tabla.install("", NUMBER, d.doubleValue()), 0) );
      	tok = NUMBER; 
   	} catch (Exception e){                                            //Todo menos digitos
      	if(Character.isJavaLetter(s.charAt(0))&& s.charAt(0)!='$'){   //Es una letra diferente de $
	        if((simbo=tabla.lookup(s))== null)                          //Se busca en la tabla de simbolos
  			    simbo=tabla.install(s, INDEF, 0.0);                       //Si no se encuentra se instala en la tabla
  			    yylval = new ParserVal(new Algo(simbo, 0));
			    //System.out.println("tok act tipo: ("+simbo.tipo+" )");
		 	    tok= (simbo.tipo == INDEF ? VAR : simbo.tipo);	
      	}else if(s.charAt(0) == '$'){                                 //Si es $ es un argumento
      		 	//System.out.println("tok act tipo: ARG < "+s.substring(1)+" >");
      			yylval = new ParserVal((int)Integer.parseInt(s.substring(1)));
            tok= ARG;
      	}else if(s.equals("==")){
            tok= EQ;
      	}else if(s.equals("!=")){
            tok= NE;
      	}else if(s.equals("<")){
            tok= LT;
      	}else if(s.equals("<=")){
            tok= LE;
      	}else if(s.equals(">")){
            tok= GT;
      	}else if(s.equals(">=")){
            tok= GE;
      	}else if(s.charAt(0) == '!'){
            tok= NOT;
      	}else if(s.equals("||")){
            tok= OR;
      	}else if(s.equals("&&")){
            tok= AND;
      	}else{
         	tok = s.charAt(0);
      	}
   	}
   	//System.out.println("Token: " + tok);
   	return tok;
}

static Parser par = new Parser(0);
static JFrame jf;
static JLabel lmuestra=new JLabel("                                 ");
static Canvas canv;
static Graphics g;
static PanelEjecuta panelEjecuta;
Parser(int foo){
 	maq=new Maquina();
   	tabla=new Tabla();                              //Se crea la tabla de simbolos y se instalan las funciones
   	tabla.install("sin",BLTIN, 0.0);
   	tabla.install("cos",BLTIN, 0.0);
   	tabla.install("tan",BLTIN, 0.0);
   	tabla.install("asin",BLTIN, 0.0);
   	tabla.install("acos",BLTIN, 0.0);
   	tabla.install("atan",BLTIN, 0.0);
   	tabla.install("exp",BLTIN, 0.0);
   	tabla.install("log",BLTIN, 0.0);
   	tabla.install("sqrt",BLTIN, 0.0);
   	tabla.install("if", IF, 0.0);
   	tabla.install("else", ELSE, 0.0);
   	tabla.install("while", WHILE, 0.0);
   	tabla.install("print", PRINT, 0.0);
   	tabla.install("proc", PROC, 0.0);
   	tabla.install("func", FUNC, 0.0);
   	tabla.install("return", RETURN, 0.0);
   	tabla.install("for", FOR, 0.0);
   	tabla.install("avanzar", FORWARD, 0.0);
   	tabla.install("subir", UP, 0.0);
   	tabla.install("bajar", DOWN, 0.0);
   	tabla.install("girarDerecha", GD, 0.0);
   	tabla.install("girarIzquierda", GI, 0.0);
   	tabla.install("color", COLOR, 0.0);

   	maq.setTabla(tabla);
   	jf=new JFrame("Proyecto Mini-LOGOS");           //Se crea la interfaz para visualizar los Logos
   	canv=new Canvas();
   	//canv.setSize(800,800);
   	panelEjecuta = new PanelEjecuta(maq, this);
   	jf.setLayout(new GridLayout (1,2));
   	jf.add(panelEjecuta);
   	jf.add(canv);
   	jf.setSize( 1800, 900);
   	jf.setVisible(true);
   	g=canv.getGraphics();
   	g.setColor(new Color(121,222,228));
   	g.fillRect(0, 0, 800, 800);
   	g.setColor(new Color(0,0,0));
   	maq.setGraphics(g);
   	panelEjecuta.setCanvas(g);

   	jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
}

public static void main(String s[]){
	  //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    new Parser();
}