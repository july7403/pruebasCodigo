//Clases para nuestro mini-prolog
package classes:
    //Para las expresiones
    sealed trait Expresion
    case class Atomo(nombre: String) extends Expresion //maria,auto,santa rosa
    case class Variable(nombre: String) extends Expresion //X,Y,Z
    case class Predicado(nombre:String, argumentos:List[Expresion]) extends Expresion //padre(maria,julian)

    //Para la base de conocimiento
    sealed trait Conocimiento
    case class Hecho(predicado:Predicado) extends Conocimiento
    case class Regla(cabeza:Predicado,argumentos:List[Predicado]) extends Conocimiento