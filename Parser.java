//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 12 "logo.y"
	import java.lang.Math;
	import java.io.*;
	import java.util.StringTokenizer;
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;
//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short NUMBER=257;
public final static short PRINT=258;
public final static short VAR=259;
public final static short BLTIN=260;
public final static short INDEF=261;
public final static short WHILE=262;
public final static short IF=263;
public final static short ELSE=264;
public final static short FOR=265;
public final static short FUNCTION=266;
public final static short PROCEDURE=267;
public final static short RETURN=268;
public final static short FUNC=269;
public final static short PROC=270;
public final static short FORWARD=271;
public final static short UP=272;
public final static short DOWN=273;
public final static short GD=274;
public final static short GI=275;
public final static short COLOR=276;
public final static short ARG=277;
public final static short OR=278;
public final static short AND=279;
public final static short GT=280;
public final static short GE=281;
public final static short LT=282;
public final static short LE=283;
public final static short EQ=284;
public final static short NE=285;
public final static short UNARYMINUS=286;
public final static short NOT=287;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    1,    5,    5,    4,    4,
    4,    4,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,    4,    3,    3,    3,    8,
    3,    3,    3,    3,    3,    3,    3,   15,   14,   10,
   16,    6,   12,   18,   11,   11,   13,   17,   17,   17,
    9,    9,   20,    2,   21,    2,   19,   19,   19,    7,
    7,    7,
};
final static short yylen[] = {                            2,
    0,    2,    3,    2,    1,    2,    3,    3,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    8,    5,    4,
    3,    3,    3,    3,    3,    3,    2,    3,    3,    3,
    3,    3,    3,    3,    3,    2,    2,    2,    3,    0,
    7,    3,   10,    4,    4,    7,    3,    3,    1,    1,
    1,    0,    2,    0,    0,    2,    0,    0,    2,    2,
    1,    3,    0,    6,    0,    6,    1,    1,    1,    0,
    1,    3,
};
final static short yydefred[] = {                         1,
    0,    9,    0,    0,    0,   49,   51,   50,   52,   52,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    2,    0,   58,    0,    0,    5,    0,   12,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   38,
    0,   67,   68,   69,   63,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    3,    4,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    6,    0,    0,    0,    0,   42,    0,    0,
    0,    0,    0,   39,    0,    0,    0,    0,   26,   59,
   47,   60,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   57,
   57,    0,   20,    0,    0,    0,    0,    0,    0,   37,
   54,    0,   48,   44,    0,    0,   19,   40,    0,    0,
    0,    0,    0,    0,    0,    0,   64,   66,    0,    0,
    0,   57,   41,    0,    0,   46,   18,    0,   57,   43,
};
final static short yydgoto[] = {                          1,
   26,   27,   28,   93,   30,   38,  115,  136,   35,   31,
  107,  132,  124,   32,   76,   33,   57,  108,   45,   85,
   86,
};
final static short yysindex[] = {                         0,
  -10,    0,  124,  -58,  -32,    0,    0,    0,    0,    0,
   58, -182, -182,  124,  124,  124,  124,  124,  -78,  -30,
  124,  124,    0,  124,    0,   17,  -27,    0,  530,    0,
   -6,   12,   12,  614,  -16,  124,  124,   30,   35,    0,
  539,    0,    0,    0,    0,    0,  614,  614,  614,  614,
  614,  124,  124,  -33,  -33,  125,   36,    0,    0,  124,
  124,  124,  124,  124,  124,  124,  124,  124,  124,  124,
  124,  124,    0,    0,  124,  -36,  -36,    0,  124,  614,
  441,  124,  124,    0,   51,   53,  552,  614,    0,    0,
    0,    0,  561,  816,  822,  -31,  -31,  -31,  -31,  -31,
  -31,  -25,  -25,  -33,  -33,  -33,   40,  124,  451,    0,
    0,  614,    0,  614,  -39,  -15,   54,   61,  124,    0,
    0,  614,    0,    0, -159,  124,    0,    0,  -36,  -36,
  572,   47,  124,  -36,  614,   48,    0,    0,  124,    0,
  614,    0,    0,  595,   67,    0,    0,  -36,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  140,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  149,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -26,   -5,   -3,    1,
    3,    0,    0,  394,  402,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   80,    0,    0,    0,    0,    0,    7,
    0,   27,   27,    0,    0,    0,    0,   38,    0,    0,
    0,    0,    0,   -4,  -34,  475,  483,  492,  500,  512,
  520,  459,  467,  413,  421,  432,    0,    0,    0,    0,
    0,   21,    0,   42,    0,    0,    0,    0,    0,    0,
    0,  -35,    0,    0,   13,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,    0,    0,    0,    0,  102,
   50,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  -56,  903,    0,  100,   28,    0,    0,    0,
  -28,    0,  -92,    0,   81,    0,    0,   -2,  103,    0,
    0,
};
final static int YYTABLESIZE=1107;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         23,
   92,  127,   36,   24,  126,   56,   34,   37,   21,   34,
   70,   68,   52,   69,   13,   71,   70,   13,  125,  110,
  111,   71,   45,   56,   34,  128,   58,   79,  126,   24,
   53,   59,   13,   74,   21,   14,   35,   15,   14,   35,
   15,   16,   78,   17,   16,   90,   17,    7,   61,  146,
    7,   75,   45,   14,   35,   15,  150,   45,   34,   16,
   72,   17,   72,   61,   62,    7,   13,   70,   72,   82,
   70,   45,  137,  138,   83,   24,   42,  142,    8,   62,
   21,    8,   71,   43,   44,   71,   25,   14,   35,   15,
  117,  149,  118,   16,  129,   17,    8,   24,  121,    7,
   72,  130,   21,   72,  134,  140,  143,  148,   53,   39,
  116,  145,   25,   77,    0,   46,   40,    0,  133,   54,
    0,    0,    0,    0,   54,    0,    0,    0,    0,    0,
    8,    0,    0,    0,    0,   45,    0,   45,   55,    0,
    0,   54,   55,    0,    0,    0,   54,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   25,    0,
   91,    0,    0,   24,    0,   89,   70,   68,   21,   69,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
   10,   10,   10,   10,   10,    0,   10,    0,    0,   11,
   11,   11,   11,   11,    0,   11,    0,    0,   10,    0,
    0,    0,    0,    0,    0,    0,    0,   11,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   72,    0,
    2,    3,    4,    5,    0,    6,    7,    0,    8,    9,
   10,   11,   10,   10,   14,   15,   16,   17,   18,   19,
   20,   11,   11,   34,   34,    0,    2,    3,    4,    5,
   22,    6,    7,    0,    8,    9,   10,   11,   12,   13,
   14,   15,   16,   17,   18,   19,   20,    0,    0,   45,
   45,   45,   45,   35,   45,   45,   22,   45,   45,   45,
   45,    0,    0,   45,   45,   45,   45,   45,   45,   45,
    0,    0,    2,    3,    4,    5,    0,    6,    7,   45,
    8,    9,   10,   11,    0,    0,   14,   15,   16,   17,
   18,   19,   20,    0,    2,    0,    4,    5,    0,    0,
    0,    0,   22,    9,    0,    0,    0,    0,   14,   15,
   16,   17,   18,   19,   20,    0,   54,    0,   54,   54,
    0,    0,    0,    0,   22,   54,    0,    0,    0,    0,
   54,   54,   54,   54,   54,   54,   54,    0,   54,    0,
   54,   54,    0,    0,    0,    0,   54,   54,    0,    0,
    0,    0,   54,   54,   54,   54,   54,   54,   54,    0,
    2,    0,    4,    5,    0,    0,    0,    0,   54,    9,
    0,    0,    0,    0,   14,   15,   16,   17,   18,   19,
   20,    0,   60,   61,   62,   63,   64,   65,   66,   67,
   22,    0,    0,    0,    0,    0,    0,   10,   10,   10,
   10,   10,   10,   10,   10,    0,   11,   11,   11,   11,
   11,   11,   11,   11,   27,   27,   27,   27,   27,    0,
   27,    0,   36,   36,   36,   36,   36,    0,   36,    0,
    0,    0,   27,   23,   23,   23,   23,   23,    0,   23,
   36,   24,   24,   24,   24,   24,    0,   24,    0,    0,
    0,   23,   25,   25,   25,   25,   25,    0,   25,   24,
    0,  113,   70,   68,    0,   69,   27,   71,    0,    0,
   25,  123,   70,   68,   36,   69,    0,   71,    0,   21,
    0,   21,   21,   21,    0,   23,    0,   22,    0,   22,
   22,   22,    0,   24,    0,   30,    0,   21,   30,    0,
    0,    0,    0,   31,   25,   22,   31,    0,    0,    0,
    0,    0,   32,   30,   72,   32,    0,    0,    0,    0,
   33,   31,    0,   33,   72,    0,    0,    0,    0,    0,
   32,   21,   28,    0,    0,   28,    0,    0,   33,   22,
   29,    0,    0,   29,    0,    0,    0,   30,    0,    0,
   28,   70,   68,    0,   69,   31,   71,    0,   29,    0,
   70,   68,    0,   69,   32,   71,    0,    0,   73,    0,
    0,    0,   33,   70,   68,  119,   69,   84,   71,    0,
    0,    0,   70,   68,   28,   69,    0,   71,    0,    0,
    0,    0,   29,   70,   68,  139,   69,    0,   71,  120,
    0,    0,    0,   72,    0,    0,    0,    0,    0,    0,
    0,    0,   72,    0,    0,    0,   70,   68,    0,   69,
    0,   71,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,    0,   72,   70,   68,    0,   69,    0,
   71,    0,    0,    0,    0,   72,    0,    0,    0,    0,
    0,   27,   27,   27,   27,   27,   27,   27,   27,   36,
   36,   36,   36,   36,   36,   36,   36,  147,   72,    0,
   23,   23,   23,   23,   23,   23,   23,   23,   24,   24,
   24,   24,   24,   24,   24,   24,    0,   72,    0,   25,
   25,   25,   25,   25,   25,   25,   25,    0,   60,   61,
   62,   63,   64,   65,   66,   67,    0,    0,   60,   61,
   62,   63,   64,   65,   66,   67,   21,   21,   21,   21,
   21,   21,   21,   21,   22,   22,   22,   22,   22,   22,
   22,   22,   30,   30,   30,   30,   30,   30,   30,   30,
   31,   31,   31,   31,   31,   31,   31,   31,    0,   32,
   32,   32,   32,   32,   32,   32,   32,   33,   33,   33,
   33,   33,   33,   33,   33,    0,    0,    0,    0,   28,
   28,   28,   28,   28,   28,   28,   28,   29,   29,   29,
   29,   29,   29,   29,   29,    0,    0,   60,   61,   62,
   63,   64,   65,   66,   67,    0,   60,   61,   62,   63,
   64,   65,   66,   67,    0,    0,    0,    0,    0,   60,
   61,   62,   63,   64,   65,   66,   67,    0,   60,   61,
   62,   63,   64,   65,   66,   67,    0,    0,    0,   60,
   61,   62,   63,   64,   65,   66,   67,   70,   68,    0,
   69,    0,   71,   70,   68,    0,   69,    0,   71,    0,
    0,    0,   60,   61,   62,   63,   64,   65,   66,   67,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   60,   61,   62,   63,   64,   65,   66,   67,    0,
    0,    0,    0,   29,    0,   34,    0,    0,    0,   72,
    0,    0,    0,   41,    0,   72,   47,   48,   49,   50,
   51,    0,    0,   54,   55,    0,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   80,   81,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   87,   88,    0,    0,    0,    0,
    0,    0,   94,   95,   96,   97,   98,   99,  100,  101,
  102,  103,  104,  105,  106,    0,    0,  109,    0,    0,
    0,  112,    0,    0,  114,  114,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  122,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,    0,    0,    0,    0,    0,    0,  135,    0,
    0,    0,    0,    0,    0,  141,    0,    0,    0,    0,
    0,  144,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,   62,   63,   64,   65,   66,
   67,   62,   63,   64,   65,   66,   67,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   57,   41,   61,   40,   44,   41,   41,   40,   45,   44,
   42,   43,   91,   45,   41,   47,   42,   44,  111,   76,
   77,   47,   10,   59,   59,   41,   10,   44,   44,   40,
   61,   59,   59,   40,   45,   41,   41,   41,   44,   44,
   44,   41,   59,   41,   44,   10,   44,   41,   44,  142,
   44,   40,   40,   59,   59,   59,  149,   45,   93,   59,
   94,   59,   94,   59,   44,   59,   93,   41,   94,   40,
   44,   59,  129,  130,   40,   40,  259,  134,   41,   59,
   45,   44,   41,  266,  267,   44,  123,   93,   93,   93,
   40,  148,   40,   93,   41,   93,   59,   40,   59,   93,
   41,   41,   45,   44,  264,   59,   59,   41,   59,   10,
   83,  140,  123,   33,   -1,   13,   59,   -1,  121,   40,
   -1,   -1,   -1,   -1,   45,   -1,   -1,   -1,   -1,   -1,
   93,   -1,   -1,   -1,   -1,  123,   -1,  125,   59,   -1,
   -1,   40,   41,   -1,   -1,   -1,   45,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,   -1,
  125,   -1,   -1,   40,   -1,   41,   42,   43,   45,   45,
   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   41,
   42,   43,   44,   45,   -1,   47,   -1,   -1,   59,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   94,   -1,
  257,  258,  259,  260,   -1,  262,  263,   -1,  265,  266,
  267,  268,   93,   94,  271,  272,  273,  274,  275,  276,
  277,   93,   94,  278,  279,   -1,  257,  258,  259,  260,
  287,  262,  263,   -1,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  275,  276,  277,   -1,   -1,  257,
  258,  259,  260,  278,  262,  263,  287,  265,  266,  267,
  268,   -1,   -1,  271,  272,  273,  274,  275,  276,  277,
   -1,   -1,  257,  258,  259,  260,   -1,  262,  263,  287,
  265,  266,  267,  268,   -1,   -1,  271,  272,  273,  274,
  275,  276,  277,   -1,  257,   -1,  259,  260,   -1,   -1,
   -1,   -1,  287,  266,   -1,   -1,   -1,   -1,  271,  272,
  273,  274,  275,  276,  277,   -1,  257,   -1,  259,  260,
   -1,   -1,   -1,   -1,  287,  266,   -1,   -1,   -1,   -1,
  271,  272,  273,  274,  275,  276,  277,   -1,  257,   -1,
  259,  260,   -1,   -1,   -1,   -1,  287,  266,   -1,   -1,
   -1,   -1,  271,  272,  273,  274,  275,  276,  277,   -1,
  257,   -1,  259,  260,   -1,   -1,   -1,   -1,  287,  266,
   -1,   -1,   -1,   -1,  271,  272,  273,  274,  275,  276,
  277,   -1,  278,  279,  280,  281,  282,  283,  284,  285,
  287,   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,  280,
  281,  282,  283,  284,  285,   -1,  278,  279,  280,  281,
  282,  283,  284,  285,   41,   42,   43,   44,   45,   -1,
   47,   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   59,   41,   42,   43,   44,   45,   -1,   47,
   59,   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,
   -1,   59,   41,   42,   43,   44,   45,   -1,   47,   59,
   -1,   41,   42,   43,   -1,   45,   93,   47,   -1,   -1,
   59,   41,   42,   43,   93,   45,   -1,   47,   -1,   41,
   -1,   43,   44,   45,   -1,   93,   -1,   41,   -1,   43,
   44,   45,   -1,   93,   -1,   41,   -1,   59,   44,   -1,
   -1,   -1,   -1,   41,   93,   59,   44,   -1,   -1,   -1,
   -1,   -1,   41,   59,   94,   44,   -1,   -1,   -1,   -1,
   41,   59,   -1,   44,   94,   -1,   -1,   -1,   -1,   -1,
   59,   93,   41,   -1,   -1,   44,   -1,   -1,   59,   93,
   41,   -1,   -1,   44,   -1,   -1,   -1,   93,   -1,   -1,
   59,   42,   43,   -1,   45,   93,   47,   -1,   59,   -1,
   42,   43,   -1,   45,   93,   47,   -1,   -1,   59,   -1,
   -1,   -1,   93,   42,   43,   44,   45,   59,   47,   -1,
   -1,   -1,   42,   43,   93,   45,   -1,   47,   -1,   -1,
   -1,   -1,   93,   42,   43,   44,   45,   -1,   47,   59,
   -1,   -1,   -1,   94,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   94,   -1,   -1,   -1,   42,   43,   -1,   45,
   -1,   47,   -1,   -1,   -1,   94,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   94,   42,   43,   -1,   45,   -1,
   47,   -1,   -1,   -1,   -1,   94,   -1,   -1,   -1,   -1,
   -1,  278,  279,  280,  281,  282,  283,  284,  285,  278,
  279,  280,  281,  282,  283,  284,  285,   93,   94,   -1,
  278,  279,  280,  281,  282,  283,  284,  285,  278,  279,
  280,  281,  282,  283,  284,  285,   -1,   94,   -1,  278,
  279,  280,  281,  282,  283,  284,  285,   -1,  278,  279,
  280,  281,  282,  283,  284,  285,   -1,   -1,  278,  279,
  280,  281,  282,  283,  284,  285,  278,  279,  280,  281,
  282,  283,  284,  285,  278,  279,  280,  281,  282,  283,
  284,  285,  278,  279,  280,  281,  282,  283,  284,  285,
  278,  279,  280,  281,  282,  283,  284,  285,   -1,  278,
  279,  280,  281,  282,  283,  284,  285,  278,  279,  280,
  281,  282,  283,  284,  285,   -1,   -1,   -1,   -1,  278,
  279,  280,  281,  282,  283,  284,  285,  278,  279,  280,
  281,  282,  283,  284,  285,   -1,   -1,  278,  279,  280,
  281,  282,  283,  284,  285,   -1,  278,  279,  280,  281,
  282,  283,  284,  285,   -1,   -1,   -1,   -1,   -1,  278,
  279,  280,  281,  282,  283,  284,  285,   -1,  278,  279,
  280,  281,  282,  283,  284,  285,   -1,   -1,   -1,  278,
  279,  280,  281,  282,  283,  284,  285,   42,   43,   -1,
   45,   -1,   47,   42,   43,   -1,   45,   -1,   47,   -1,
   -1,   -1,  278,  279,  280,  281,  282,  283,  284,  285,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  278,  279,  280,  281,  282,  283,  284,  285,   -1,
   -1,   -1,   -1,    1,   -1,    3,   -1,   -1,   -1,   94,
   -1,   -1,   -1,   11,   -1,   94,   14,   15,   16,   17,
   18,   -1,   -1,   21,   22,   -1,   24,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   36,   37,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   52,   53,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   61,   62,   63,   64,   65,   66,   67,
   68,   69,   70,   71,   72,   -1,   -1,   75,   -1,   -1,
   -1,   79,   -1,   -1,   82,   83,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  108,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  119,   -1,   -1,   -1,   -1,   -1,   -1,  126,   -1,
   -1,   -1,   -1,   -1,   -1,  133,   -1,   -1,   -1,   -1,
   -1,  139,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  279,  280,  281,  282,  283,  284,
  285,  280,  281,  282,  283,  284,  285,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=287;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'",null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'","'^'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"NUMBER","PRINT","VAR","BLTIN",
"INDEF","WHILE","IF","ELSE","FOR","FUNCTION","PROCEDURE","RETURN","FUNC","PROC",
"FORWARD","UP","DOWN","GD","GI","COLOR","ARG","OR","AND","GT","GE","LT","LE",
"EQ","NE","UNARYMINUS","NOT",
};
final static String yyrule[] = {
"$accept : list",
"list :",
"list : list '\\n'",
"list : list oper '\\n'",
"oper : defn ';'",
"oper : stmt",
"oper : expr ';'",
"asgn : VAR '=' expr",
"asgn : ARG '=' expr",
"expr : NUMBER",
"expr : VAR",
"expr : ARG",
"expr : asgn",
"expr : FORWARD expr",
"expr : UP expr",
"expr : DOWN expr",
"expr : GD expr",
"expr : GI expr",
"expr : COLOR '[' expr ',' expr ',' expr ']'",
"expr : FUNCTION begin '(' arglist ')'",
"expr : BLTIN '(' expr ')'",
"expr : expr '+' expr",
"expr : expr '-' expr",
"expr : expr '*' expr",
"expr : expr '/' expr",
"expr : expr '^' expr",
"expr : '(' expr ')'",
"expr : '-' expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr GT expr",
"expr : expr GE expr",
"expr : expr LT expr",
"expr : expr LE expr",
"expr : expr AND expr",
"expr : expr OR expr",
"expr : NOT expr",
"stmt : expr ';'",
"stmt : RETURN ';'",
"stmt : RETURN expr ';'",
"$$1 :",
"stmt : PROCEDURE begin '(' arglist ')' $$1 ';'",
"stmt : PRINT prlist ';'",
"stmt : for '(' forexp ';' condFor ';' forexp ')' stmt end",
"stmt : while cond stmt end",
"stmt : if cond stmt end",
"stmt : if cond stmt end ELSE stmt end",
"stmt : '{' stmtlist '}'",
"cond : '(' expr ')'",
"while : WHILE",
"for : FOR",
"if : IF",
"begin :",
"condFor : iniFor expr",
"iniFor :",
"forexp :",
"forexp : iniFor expr",
"end :",
"stmtlist :",
"stmtlist : stmtlist '\\n'",
"stmtlist : stmtlist stmt",
"prlist : expr",
"prlist : prlist ',' expr",
"$$2 :",
"defn : FUNC procname $$2 '(' ')' stmt",
"$$3 :",
"defn : PROC procname $$3 '(' ')' stmt",
"procname : VAR",
"procname : FUNCTION",
"procname : PROCEDURE",
"arglist :",
"arglist : expr",
"arglist : arglist ',' expr",
};

//#line 233 "logo.y"

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
//#line 694 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 4:
//#line 42 "logo.y"
{ maq.code("STOP");return 1 ;}
//break;
case 5:
//#line 43 "logo.y"
{ maq.code("STOP"); return 1 ;}
//break;
case 6:
//#line 44 "logo.y"
{ maq.code("print"); maq.code("STOP"); return 1 ;}
//break;
case 7:
//#line 47 "logo.y"
{ yyval=val_peek(0); 
					                      maq.code("varpush"); 
                                maq.code(((Algo)val_peek(2).obj).simb); 
		  			                    maq.code("assign");}
break;
case 8:
//#line 51 "logo.y"
{ defnonly("$");     
        			                  /*code2(argassign,(Inst)$1); */
        			                  maq.code("argassign"); 
        			                  maq.code(val_peek(2).ival+"");
        			                  yyval=val_peek(0);}
break;
case 9:
//#line 58 "logo.y"
{ ((Algo)yyval.obj).inst=maq.code("constpush");
                						    maq.code(((Algo)val_peek(0).obj).simb); }
break;
case 10:
//#line 60 "logo.y"
{ ((Algo)yyval.obj).inst=maq.code("varpush");
                						    maq.code(((Algo)val_peek(0).obj).simb); maq.code("eval");}
break;
case 11:
//#line 62 "logo.y"
{ defnonly("$"); 
            								    /*$$ = code2(arg, (Inst)$1);      */
    								            /*erroneo ((Algo)$$.obj).inst= maq.code("arg"); */
    								            yyval = new ParserVal(new Algo(maq.code("arg"))); 
    								            maq.code(val_peek(0).ival+"");}
break;
case 13:
//#line 70 "logo.y"
{ yyval=val_peek(0); maq.code("forward");}
break;
case 14:
//#line 71 "logo.y"
{ yyval=val_peek(0); maq.code("upPencil");}
break;
case 15:
//#line 72 "logo.y"
{ yyval=val_peek(0); maq.code("downPencil");}
break;
case 16:
//#line 73 "logo.y"
{ yyval=val_peek(0); maq.code("girarDerecha");}
break;
case 17:
//#line 74 "logo.y"
{ yyval=val_peek(0); maq.code("girarIzquierda");}
break;
case 18:
//#line 75 "logo.y"
{ yyval=val_peek(6); maq.code("color");}
break;
case 19:
//#line 78 "logo.y"
{ yyval = val_peek(3); 
            							              maq.code("call"); 
            							              maq.code(((Algo)val_peek(4).obj).simb); 
            							              maq.code(val_peek(1).ival+"");
            							        	  }
break;
case 20:
//#line 83 "logo.y"
{ yyval=val_peek(1); 
            									  maq.code("bltin"); 
            									  maq.code(((Algo)val_peek(3).obj).simb); 
            									}
break;
case 21:
//#line 87 "logo.y"
{ maq.code("add"); }
break;
case 22:
//#line 88 "logo.y"
{ maq.code("sub"); }
break;
case 23:
//#line 89 "logo.y"
{ maq.code("mul"); }
break;
case 24:
//#line 90 "logo.y"
{ maq.code("div"); }
break;
case 25:
//#line 91 "logo.y"
{ maq.code("power"); }
break;
case 26:
//#line 92 "logo.y"
{ yyval= val_peek(1);}
break;
case 27:
//#line 93 "logo.y"
{ yyval=val_peek(0); maq.code("negate");}
break;
case 28:
//#line 94 "logo.y"
{ maq.code("eq"); }
break;
case 29:
//#line 95 "logo.y"
{ maq.code("ne"); }
break;
case 30:
//#line 96 "logo.y"
{ maq.code("gt"); }
break;
case 31:
//#line 97 "logo.y"
{ maq.code("ge"); }
break;
case 32:
//#line 98 "logo.y"
{ maq.code("lt"); }
break;
case 33:
//#line 99 "logo.y"
{ maq.code("le"); }
break;
case 34:
//#line 100 "logo.y"
{ maq.code("and"); }
break;
case 35:
//#line 101 "logo.y"
{ maq.code("or"); }
break;
case 36:
//#line 102 "logo.y"
{ yyval = val_peek(0); maq.code("not"); }
break;
case 37:
//#line 105 "logo.y"
{ maq.code("pop");}
break;
case 38:
//#line 106 "logo.y"
{ defnonly("return"); 
   						  				        maq.code("procret");}
break;
case 39:
//#line 108 "logo.y"
{ defnonly( "return" ); 
				   						          yyval = val_peek(1); 
				   						          maq.code("funcret");}
break;
case 40:
//#line 111 "logo.y"
{ yyval = val_peek(3); 
            							              maq.code("call"); 
            							              maq.code(((Algo)val_peek(4).obj).simb); 
            							              maq.code(val_peek(1).ival+"");
            							            }
break;
case 42:
//#line 116 "logo.y"
{ yyval = val_peek(1); }
break;
case 43:
//#line 117 "logo.y"
{ maq.getProg().setElementAt(
													                                     new Integer(((Algo)val_peek(5).obj).inst),
													                                     ((Algo)val_peek(9).obj).inst+1); 
                        													      	  maq.getProg().setElementAt(
                    													                 new Integer(((Algo)val_peek(3).obj).inst),
                    													                 ((Algo)val_peek(9).obj).inst+2);
                        													    	    maq.getProg().setElementAt(
                    													                 new Integer(((Algo)val_peek(1).obj).inst),
                    													                 ((Algo)val_peek(9).obj).inst+3); 
                        													    	    maq.getProg().setElementAt(
                    													                 new Integer(((Algo)val_peek(0).obj).inst),
                    													                 ((Algo)val_peek(9).obj).inst+4); 
                        													   		  }
break;
case 44:
//#line 130 "logo.y"
{ maq.getProg().setElementAt(
    							                 new Integer(((Algo)val_peek(1).obj).inst),
    							                 ((Algo)val_peek(3).obj).inst+1); 
						             	      maq.getProg().setElementAt(
    							                 new Integer(((Algo)val_peek(0).obj).inst),
    							                 ((Algo)val_peek(3).obj).inst+2); 
					 					          }
break;
case 45:
//#line 137 "logo.y"
{ maq.getProg().setElementAt(
        							             new Integer(((Algo)val_peek(1).obj).inst),
        							             ((Algo)val_peek(3).obj).inst+1); 
							                  maq.getProg().setElementAt(
      								             new Integer(((Algo)val_peek(0).obj).inst),
      								             ((Algo)val_peek(3).obj).inst+3); 
							                }
break;
case 46:
//#line 144 "logo.y"
{ maq.getProg().setElementAt(
          							                 new Integer(((Algo)val_peek(4).obj).inst),
          							                 ((Algo)val_peek(6).obj).inst+1); 
          						                  maq.getProg().setElementAt(
          							                 new Integer(((Algo)val_peek(1).obj).inst),
          							                 ((Algo)val_peek(6).obj).inst+2);             
          						                  maq.getProg().setElementAt(
          							                 new Integer(((Algo)val_peek(0).obj).inst),
          							                 ((Algo)val_peek(6).obj).inst+3);  
          	  									      }
break;
case 47:
//#line 154 "logo.y"
{ yyval  =  val_peek(1);}
break;
case 48:
//#line 157 "logo.y"
{ maq.code("STOP");  
              								  ((Algo)yyval.obj).inst = ((Algo)val_peek(1).obj).inst;/*checar*/
          									  }
break;
case 49:
//#line 162 "logo.y"
{ yyval = new ParserVal(new Algo(maq.code("whilecode"))); 
              								  maq.code("STOP");maq.code("STOP");
              								}
break;
case 50:
//#line 167 "logo.y"
{ yyval = new ParserVal(new Algo(maq.code("forcode"))); 
      								          maq.code("STOP");maq.code("STOP");maq.code("STOP");maq.code("STOP");
      								        }
break;
case 51:
//#line 172 "logo.y"
{ /*$$ = code(ifcode); code3(STOP,STOP,STOP);*/
        										    yyval = new ParserVal(new Algo(maq.code("ifcode"))); 
              								  maq.code("STOP");maq.code("STOP");maq.code("STOP");
              								}
break;
case 52:
//#line 178 "logo.y"
{ yyval=new ParserVal(new Algo(maq.getProgP()));
        								        /*$$ = new ParserVal(maq.getProgP()); */
        								      }
break;
case 53:
//#line 183 "logo.y"
{ maq.code("STOP");  
        								        ((Algo)yyval.obj).inst = ((Algo)val_peek(1).obj).inst; /*checar*/
    									        }
break;
case 54:
//#line 188 "logo.y"
{ yyval=new ParserVal(new Algo(maq.getProgP()));
        								        /*$$ = new ParserVal(maq.getProgP());*/
        								      }
break;
case 55:
//#line 193 "logo.y"
{ maq.code("STOP");  yyval = new ParserVal(0); }
break;
case 56:
//#line 194 "logo.y"
{ maq.code("STOP");  
        								        ((Algo)yyval.obj).inst = ((Algo)val_peek(1).obj).inst; /*checar*/
    									        }
break;
case 57:
//#line 199 "logo.y"
{ maq.code("STOP"); 
           										  yyval=new ParserVal(new Algo(maq.getProgP()));
           										  /*$$=new ParserVal(maq.getProgP());        */
              								}
break;
case 58:
//#line 205 "logo.y"
{ yyval=new ParserVal(new Algo(maq.getProgP()));          
										            /*$$=new ParserVal(maq.getProgP());  */
        								      }
break;
case 61:
//#line 212 "logo.y"
{ maq.code("prexpr"); }
break;
case 62:
//#line 213 "logo.y"
{ maq.code("prexpr"); }
break;
case 63:
//#line 216 "logo.y"
{ ((Algo)val_peek(0).obj).simb.tipo=FUNCTION; indef=true; }
break;
case 64:
//#line 217 "logo.y"
{ maq.code("procret"); maq.define(((Algo)val_peek(4).obj).simb); indef=false; }
break;
case 65:
//#line 218 "logo.y"
{ ((Algo)val_peek(0).obj).simb.tipo=PROCEDURE; indef=true; }
break;
case 66:
//#line 219 "logo.y"
{ maq.code("procret"); maq.define(((Algo)val_peek(4).obj).simb); indef=false; }
break;
case 70:
//#line 227 "logo.y"
{ yyval = new ParserVal(0); }
break;
case 71:
//#line 228 "logo.y"
{ yyval = new ParserVal(1); }
break;
case 72:
//#line 229 "logo.y"
{ yyval =  new ParserVal(val_peek(2).ival + 1); }
break;
//#line 1173 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
