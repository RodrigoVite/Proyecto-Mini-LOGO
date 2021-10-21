# Proyecto-Mini-LOGO

Los archivos ya tienen todo lo necesario para la ejecución mediante la línea:
	java Parser


Anexo van:
	- Los archivos del programa contiene el código fuente y todo lo necesario para ejecutar el código.
	- El código en demo.txt cuyo contenido puede ser ejecutado en el proyecto.


Si se realizan cambios, la compilación se hace primero desde la gramática:
	byaccj -J logo.y

Compilar el Parser generado con Java:
javac Parser.java

Y ejecutarlo:
java Parser
