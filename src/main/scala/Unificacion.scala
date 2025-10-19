import classes.*

type Sustituciones = Map[Variable, Expresion]

// Unificar dos expresiones 
def unificar(expresion1: Expresion, expresion2: Expresion, sustitucion: Sustituciones): Option[Sustituciones] = {

  def resolverExpresion(exp: Expresion): Expresion = exp match {
    case variable: Variable => sustitucion.get(variable).map(resolverExpresion).getOrElse(variable)
    case Predicado(nombre, argumentos) => Predicado(nombre, argumentos.map(resolverExpresion))
    case otro => otro
  }

  val expr1Resuelta = resolverExpresion(expresion1)
  val expr2Resuelta = resolverExpresion(expresion2)

  (expr1Resuelta, expr2Resuelta) match {
    case _ if expr1Resuelta == expr2Resuelta => Some(sustitucion)
    case (variable: Variable, otraExpresion) => Some(sustitucion.updated(variable, otraExpresion))
    case (otraExpresion, variable: Variable) => Some(sustitucion.updated(variable, otraExpresion))
    case (Predicado(nombre1, args1), Predicado(nombre2, args2)) if nombre1 == nombre2 && args1.length == args2.length =>
      unificarListas(args1, args2, sustitucion)
    case _ => None
  }
}

private def unificarListas(lista1: List[Expresion], lista2: List[Expresion], sustitucionInicial: Sustituciones): Option[Sustituciones] = {
  (lista1, lista2) match {
    case (Nil, Nil) => Some(sustitucionInicial)
    case (cabeza1 :: cola1, cabeza2 :: cola2) =>
      unificar(cabeza1, cabeza2, sustitucionInicial).flatMap { sustitucionesActualizadas =>
        unificarListas(cola1, cola2, sustitucionesActualizadas)
      }
    case _ => None
  }
}