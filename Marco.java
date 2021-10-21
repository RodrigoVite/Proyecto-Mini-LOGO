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
class Marco {
	Simbolo s;
	int retpc;	//Dirección de retorno
	int argn;	//n-esimo elemento pila
	int nargs;	//Cantidad de argumentos
	Marco(Simbolo s, int retpc, int argn, int nargs){
		this.s=s;
		this.retpc=retpc;
		this.argn=argn;
		this.nargs=nargs;
	}
}