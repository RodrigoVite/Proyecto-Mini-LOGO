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
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.StringTokenizer;
import javax.swing.text.*;

public class PanelEjecuta extends JPanel {
  private Maquina maq;
  private Parser par;
  private boolean newline;
  private String etiqs[]={"Ejecutar", "Limpiar"};
  private Paleta paleta;;
  static JTextArea txtInstrucciones;
  Graphics g;
  public PanelEjecuta(Maquina maq, Parser par){
    /** Se desarrolla la interfaz **/
    this.maq=maq; this.par=par;
    GridBagLayout gridBag = new GridBagLayout ();
    GridBagConstraints restricciones = new GridBagConstraints ();
    setLayout(gridBag);
    restricciones.fill = GridBagConstraints.BOTH;

    /** Se añade el área de texto para recibir el código **/
    txtInstrucciones = new JTextArea(8, 40);
    txtInstrucciones.setFont(txtInstrucciones.getFont().deriveFont(20f));
    JScrollPane qScroller = new JScrollPane(txtInstrucciones);
    restricciones.gridwidth = GridBagConstraints.REMAINDER;
    restricciones.weighty = 1.5;
    restricciones.weightx = 0.5;
    restricciones.gridheight = 10;
    gridBag.setConstraints (qScroller, restricciones);   
    add(qScroller);

    /** Se añaden los botones **/
    paleta=new Paleta(etiqs,new GridLayout(1, etiqs.length), new ManejaAccionInt()); 
    restricciones.weightx = 0.0;
    restricciones.gridwidth = 2;
    restricciones.gridheight = 2;
    restricciones.weighty = 0;
    gridBag.setConstraints (paleta, restricciones);  
    add(paleta);
  }
  
  public void setCanvas(Graphics g){ 
    this.g=g;
    setPanel();
    
  }

  public void setPanel(){
    g.setColor(new Color(121,222,228));
    g.fillRect(0, 0, 800, 800);
    g.setColor(new Color(0,0,0));
  }
  class ManejaAccionInt implements AccionInt {
    public void accion(int n, ActionEvent e){ 
      if(n==0){ 
        /** Se establece la entrada con la cadena que se analizará **/
        par.setTokenizer(new StringTokenizer(txtInstrucciones.getText()));
        par.setNewline(false);

        /** Se limpia el área de dibujo **/
        g.setColor(Color.WHITE);
        g.setColor(new Color(121,222,228));
        g.fillRect(0, 0, 900, 900);
        g.setColor(new Color(0,0,0));
        
        /** Se establece el cursor del panel de dibujo en el centro con un ángulo de 0 **/
        maq.setX(360);
        maq.setY(360);
        maq.setAngulo(0);

        /** Se analiza todo el texto de entrada **/
        for(maq.initcode(); par.yyparse ()!=0; maq.initcode())
          maq.execute(maq.progbase);

        /** Se dibuja un triangulo indicando la posición final del cursor de Logo **/
        Polygon poligono = null;
        int xs[] = new int[3];
        int ys[] = new int[3];
        double x = maq.getX();
        double y = maq.getY();;
        double angulo = maq.getAngulo();
        xs[0] = (int) x;
        ys[0] = (int) y;
        xs[1] = (int) (x - 10*Math.cos(Math.toRadians(angulo+20)));
        ys[1] = (int) (y - + 10*Math.sin(Math.toRadians(angulo+20)));
        xs[2] = (int) (x - 10*Math.cos(Math.toRadians(angulo-20)));
        ys[2] = (int) (y - + 10*Math.sin(Math.toRadians(angulo-20)));
        poligono = new Polygon(xs,ys,3);
        g.setColor(Color.BLACK);
        g.drawPolygon(poligono);
        g.fillPolygon(poligono);
      }
      if(n==1){
        /** Se limpia el cuadro de texto **/
        txtInstrucciones.setText("");

        /** Se limpia el panel de dibujo **/
        g.setColor(Color.WHITE);
        g.setColor(new Color(121,222,228));
        g.fillRect(0, 0, 900, 900);
        g.setColor(new Color(0,0,0));

        /** Se establece el cursor del panel de dibujo en el centro **/
        maq.setX(360);
        maq.setY(360);
        maq.setAngulo(0);

        /** Se dibuja un triangulo con la posición del cursor **/
        Polygon poligono = null;
        int xs[] = new int[3];
        int ys[] = new int[3];
        double x = maq.getX();
        double y = maq.getY();;
        double angulo = maq.getAngulo();
        xs[0] = (int) x;
        ys[0] = (int) y;
        xs[1] = (int) (x - 10*Math.cos(Math.toRadians(angulo+20)));
        ys[1] = (int) (y - + 10*Math.sin(Math.toRadians(angulo+20)));
        xs[2] = (int) (x - 10*Math.cos(Math.toRadians(angulo-20)));
        ys[2] = (int) (y - + 10*Math.sin(Math.toRadians(angulo-20)));
        poligono = new Polygon(xs,ys,3);
        g.setColor(Color.BLACK);
        g.drawPolygon(poligono);
        g.fillPolygon(poligono);
      }
    }
  }
}