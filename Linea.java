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

public class Linea implements Dibujable {
	private int x1=0;
	private int y1=0;
	private int x2=0;
	private int y2=0;

	public Linea(int x1, int y1, int x2, int y2){
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}

	public void dibuja(Graphics g){
		g.drawLine(x1,y1,x2,y2);
	}
}