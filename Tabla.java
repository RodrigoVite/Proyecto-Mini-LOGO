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
public class Tabla {
	Simbolo listaSimbolo;

	Tabla(){
		listaSimbolo=null;
	}

	/** Se añade un elemento a la tabla de simbolos **/
	Simbolo install(String s, short t, double d){
		Simbolo simb=new Simbolo(s,t,d);
		simb.ponSig(listaSimbolo);
		listaSimbolo=simb;
		return simb;
	}
	
	/** Se busca por un elemento en la tabla de simbolos **/
	Simbolo lookup(String s){
		for(Simbolo sp=listaSimbolo; sp!=null; sp=sp.obtenSig())
			if((sp.obtenNombre()).equals(s))
				return sp;
		return null;
	}
}