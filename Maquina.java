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
import java.awt.*;
import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.text.*;

class  Maquina {

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
   public final static short ARG=272;
   public final static short OR=273;
   public final static short AND=274;
   public final static short GT=275;
   public final static short GE=276;
   public final static short LT=277;
   public final static short LE=278;
   public final static short EQ=279;
   public final static short NE=280;
   public final static short UNARYMINUS=281;
   public final static short NOT=282;
   public final static short YYERRCODE=256;

   /** Atributos **/
   Stack pila;
   Stack marcos;
   Vector prog=new Vector(); 
   Tabla tabla;
   static int pc=0;
   int progp;
   int progbase=0;
   boolean returning=false;

   int numArchi=0;
   Method metodo;
   Method metodos[];
   Class c;
   Graphics g;
   double angulo;
   double x=0, y=0;
   Class parames[];

   Maquina(){}
   /** Funciónes para colocar valores en la máquina **/
   public void setTabla(Tabla t){ tabla = t; }

   public void setGraphics(Graphics g){ this.g=g; }

   Maquina(Graphics g){ this.g=g; }

   public Vector getProg(){
      return prog;
   }

   public int getProgP(){
      return progp;
   }

    public int getProgBase(){
      return progbase;
   }

   public int getProgSize(){
      return prog.size();
   }

   /** Función para iniciar el código **/
   void initcode(){
      progp = progbase;
      pila=new Stack();
      marcos=new Stack();
      returning = false;
      int index;
      for (index=prog.size()-1;index>=progbase;index--)
         prog.removeElementAt(index);
   }

   /**Función para sacar un elemento de la pila de datos **/
   Object pop(){
      return pila.pop();
   }

   /** Función para meter una instrucción a la RAM **/
   int code(Object f){
      prog.addElement(f);
      progp = prog.size();
      return prog.size()-1;
   }
   
   /** Función para ejecutar el código de la RAM **/
   void execute(int p){
      String inst;
      for(pc = p; !(inst=(String)prog.elementAt(pc)).equals("STOP") && !returning && pc<prog.size(); ){
         try {
            inst=(String)prog.elementAt(pc);
            pc=pc+1;
            c=this.getClass();
            metodo=c.getDeclaredMethod(inst, null);
            metodo.invoke(this, null);
         } catch(NoSuchMethodException e){
            System.out.println("No metodo "+e);
         } catch(InvocationTargetException e){
            System.out.println(" inst = "+inst+"  "+e+"this "+this+" pc = [ "+pc+" ]");
         } catch(IllegalAccessException e){
            System.out.println(e);
         }
      }
   }

   /** Función para meter una constante a la pila de datos **/
   void constpush(){
      Simbolo s;
      Double d;
      s=(Simbolo)prog.elementAt(pc);
      pc=pc+1;
      pila.push(new Double(s.val));
   }

   /** Función para meter una variable a la pila de datos **/
   void varpush(){
      Simbolo s;
      double d;
      s=(Simbolo)prog.elementAt(pc);
      pc=pc+1;
      pila.push(s);
   }

   /** Función para ejecutar ciclo while **/
   void whilecode(){
      boolean d;
      int savepc = pc;
      execute(savepc+2);	/* condition */
      d=((Boolean)pila.pop()).booleanValue();
      while (d) {
         execute(((Integer)prog.elementAt(savepc)).intValue());
         if (returning) break;
         execute(savepc+2);
         d=((Boolean)pila.pop()).booleanValue();	
      }
      if(!returning)
         pc=((Integer)prog.elementAt(savepc+1)).intValue();
   }

   /** Función para ejecutar ciclo for **/
   void forcode(){
      boolean d;
      int savepc = pc;
      if(!prog.elementAt(savepc+4).toString().equals("STOP")){
         execute(savepc+4);                                       /* Expresión de iniciación */  
         pila.pop();
      }
      execute(((Integer)prog.elementAt(savepc)).intValue());      /* Condición */
      d=((Boolean)pila.pop()).booleanValue();
      while (d) {
         execute(((Integer)prog.elementAt(savepc+2)).intValue()); /* Cuerpo del for*/
         if(returning) break;
         if(!prog.elementAt(savepc+1).toString().equals("STOP")){
            execute(
            ((Integer)prog.elementAt(savepc+1)).intValue());      /* Tercer argumento del for*/
         }
         execute(((Integer)prog.elementAt(savepc)).intValue());   /* Condicion*/
         d=((Boolean)pila.pop()).booleanValue();  
      }
      if(!returning)
         pc=((Integer)prog.elementAt(savepc+1)).intValue();
   }

   /** Función para ejecutar decisiones if **/
   void ifcode(){
      boolean d;
      int savepc=pc;
      execute(savepc+3);                                          /*Condition*/
      d=((Boolean)pila.pop()).booleanValue(); 
      if(d){
         execute(((Integer)prog.elementAt(savepc)).intValue());   /*Cuerpo del if*/
      }
      else if(!prog.elementAt(savepc+1).toString().equals("STOP"))
         execute(((Integer)prog.elementAt(savepc+1)).intValue()); /*Parte else*/
      if (!returning)
         pc=((Integer)prog.elementAt(savepc+2)).intValue();
   }

   /** Función para definir funciones y procedimientos **/
   void define(Simbolo s){
      s.defn=progbase;
      progbase=prog.size();
   }

   /** Función para llamar a funciones y procedimientos **/
   void call(){
      Simbolo s;
      Marco m;       
      s=(Simbolo)prog.elementAt(pc);
      m=new Marco(s, pc+2, pila.size()-1, Integer.parseInt((String)prog.elementAt(pc+1)));
      marcos.push(m);
      execute(s.defn);
      returning = false;
   }

   /** Función para return **/ 
   void ret(){
      Marco m=(Marco)marcos.peek();
      for(int i=0 ; i< m.nargs; i++)
         pila.pop();
      pc=m.retpc;     
      marcos.pop();
      returning = true;
   }	

   /** Función para return de una función **/ 
   void funcret(){
      Object o;
      Marco m=(Marco)marcos.peek();
      if(m.s.tipo==PROCEDURE)
         System.out.println(m.s.nombre+" (proc) regresa valor");
      double d;
      d=((Double)pila.pop()).doubleValue();
      ret();
      pila.push(new Double(d));
   }

   /** Función para return de un procedimiento **/
   void procret(){
      Marco m=(Marco)marcos.peek();
      if(m.s.tipo == FUNCTION)
         System.out.println(m.s.nombre+" (func) regresa valor");
      ret();
   }

   /** Funciones para obtener argumentos **/
   int getarg(){
      Marco m=(Marco)marcos.peek();
      int nargs =Integer.parseInt((String)prog.elementAt(pc));
      pc=pc+1;
      if(nargs > m.nargs)
         System.out.println(m.s.nombre+" argumentos insuficientes");
      return m.argn+nargs-m.nargs;
   }

   void arg(){
      Object o;
      double d;
      d=((Double)pila.elementAt(getarg())).doubleValue();
      pila.push(new Double(d));
   }

   /** Función para el comando avanzar de logo **/
   void forward(){
      double d1;
      d1=((Double)pila.pop()).doubleValue();
      if(g!=null){
         (new Linea((int)x,(int)y,(int)(x+d1*Math.cos(Math.toRadians(angulo))), 
            (int)(y+d1*Math.sin(Math.toRadians(angulo)))) ).dibuja(g);
      }
      x=x+d1*Math.cos(Math.toRadians(angulo));
      y=y+d1*Math.sin(Math.toRadians(angulo));
      pila.push(new Double(d1));
   }

   /** Función para el comando subir pincel de logo **/
   void upPencil(){
      double d1;
      d1=((Double)pila.pop()).doubleValue();
      //y = y - (int)d1 ;
      x=x+d1*Math.cos(Math.toRadians(angulo));
      y=y+d1*Math.sin(Math.toRadians(angulo));
      pila.push(new Double(d1));
   }

   /** Función para el comando bajar pincel de logo **/
   void downPencil(){
      double d1;
      d1=((Double)pila.pop()).doubleValue();
      //y = y + (int)d1 ;
      x=x-d1*Math.cos(Math.toRadians(angulo));
      y=y-d1*Math.sin(Math.toRadians(angulo));
      pila.push(new Double(d1));
   }

   /** Función para el comando girarIzquierda de logo **/
   void girarIzquierda(){
      double d1;
      d1=((Double)pila.pop()).doubleValue();
      angulo = angulo - d1 ;
      angulo = angulo%360;
      pila.push(new Double(d1));
   }

   /** Función para el comando girarDerecha de logo **/
   void girarDerecha(){
      double d1;
      d1=((Double)pila.pop()).doubleValue();
      angulo = angulo + d1 ;
      angulo = angulo%360;
      pila.push(new Double(d1));
   }

   /** Función para el comando cambiar color del pincel de logo **/
   void color(){
      double d1, d2, d3;
      d1=((Double)pila.pop()).doubleValue();
      d2=((Double)pila.pop()).doubleValue();
      d3=((Double)pila.pop()).doubleValue();
      g.setColor(new Color((int)d3%256,(int)d2%256,(int)d1%256));
      pila.push(new Double(d1));
   }

   void setX(double x){
      this.x=x;
   }

   void setY(double y){
      this.y=y;
   }

   void setAngulo(double ang){
      this.angulo=ang;
   }


   double getX(){
      return this.x;
   }

   double getY(){
      return this.y;
   }

   double getAngulo(){
      return this.angulo;
   }

   /** Función para asignar un valor a un argumento dentro de función o procedimiento **/
   void argassign()  {
      Double doble;
      double d;
      doble = (Double)pila.pop();
      d=doble.doubleValue();   
      pila.push(doble); /* dejar valor en la pila */
      pila.setElementAt( doble, getarg());
   }

   /** Funciones para operar double **/
   void add(){
      double d1, d2;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      d1+=d2;
      pila.push(new Double(d1));
   }

   void sub(){
      double d1, d2;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      d1-=d2;
      pila.push(new Double(d1));
   }

   void mul(){
      double d1, d2;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      d1*=d2;
      pila.push(new Double(d1));
   }

   void div(){
      double d1, d2;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      d1/=d2;
      pila.push(new Double(d1));
   }

   void power(){
      double d1, d2;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      d1= Math.pow(d1, d2);
      pila.push(new Double(d1));
   }

   void negate(){
      double d;
      d=((Double)pila.pop()).doubleValue();
      d=-d;
      pila.push(new Double(d));
   }

   void eq(){
      double d1, d2;
      boolean res;	        
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 == d2);
      pila.push(new Boolean(res));
   }

   void ne(){
      double d1, d2;
      boolean res;	        
         
      d2=((Double)pila.pop()).doubleValue();     
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 != d2);
      pila.push(new Boolean(res));
   }

   void lt(){
      double d1, d2;
      boolean res;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 < d2);
      pila.push(new Boolean(res));
   }

   void le(){
      double d1, d2;
      boolean res;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 <= d2);
      pila.push(new Boolean(res));
   }

   void gt(){
      double d1, d2;
      boolean res;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 > d2);
      pila.push(new Boolean(res));
   }

   void ge(){
      double d1, d2;
      boolean res;
      d2=((Double)pila.pop()).doubleValue();
      d1=((Double)pila.pop()).doubleValue();
      res= (d1 >= d2);
      pila.push(new Boolean(res));
   }

   void or(){
      boolean d1, d2;
      d2=((Boolean)pila.pop()).booleanValue();
      d1=((Boolean)pila.pop()).booleanValue();
      d1=(d1 || d2);
      pila.push(new Boolean(d1));
   }

   void and(){
      boolean d1, d2;
      d2=((Boolean)pila.pop()).booleanValue();
      d1=((Boolean)pila.pop()).booleanValue();
      d1=(d1 && d2);
      pila.push(new Boolean(d1));
   }

   void not(){
      boolean d;
      d=((Boolean)pila.pop()).booleanValue();
      d=!d;
      pila.push(new Boolean(d));
   }

   /** Función para evaluar y obtener el valor de una variable **/
   void eval(){
      Simbolo s;
      s=(Simbolo)pila.pop();
      if(s.tipo == INDEF)
         System.out.println("variable no definida "+ s.nombre);
      pila.push(new Double(s.val));
   }

   /** Función para asignar el valor a una variable **/
   void assign(){
      Simbolo s;
      Double d2;
      s=(Simbolo)pila.pop();
      d2=(Double)pila.pop();
      if(s.tipo != VAR && s.tipo != INDEF)
         System.out.println("asignacion  a una no variable "+s.nombre);
      s.val = d2.doubleValue();
      s.tipo= VAR;
      pila.push(new Double(s.val));
   }

   /** Función para imprimir doubles **/
   void print(){
      Double d;
      d=(Double)pila.pop();
      System.out.println(" "+d.doubleValue());
   }

   /** Función para imprimir expresiones **/
   void prexpr(){
      Double d;
      d=(Double)pila.pop();
      System.out.print("["+d.doubleValue()+"]");
   }

   /** Función para bltins dentro de la calculadora **/
   void bltin(){
      Double d;
      String inst;
      Class c;
      Double ret;

      Class paramC[]={java.lang.Double.TYPE};
      Object param[]=new Object[1];

      d=(Double)pila.pop();
      param[0]=new Double (d.doubleValue());
      inst=((Simbolo)prog.elementAt(pc)).nombre;
      c=java.lang.Math.class;
      try {
         metodo=c.getDeclaredMethod(inst, paramC);
         ret=(Double)metodo.invoke(this, param);
         pila.push(ret);
      } catch(NoSuchMethodException e){
         System.out.println("No metodo "+e+ "instru "+inst);
      } catch(InvocationTargetException e){
         System.out.println(e);
      } catch(IllegalAccessException e){
         System.out.println(e);
      }
      pc=pc+1;
   }
}