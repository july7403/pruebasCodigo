import classes.*

object UnificacionError extends Exception

type Sustituciones = Map[Variable, Expresion]

def unificar(expr1: Expresion, expr2: Expresion, sustitucion: Sustituciones): Sustituciones = {

  def resolver(e: Expresion): Expresion = e match {
    case v: Variable if sustitucion.contains(v) => resolver(sustitucion(v))
    case Predicado(nombre, args) => Predicado(nombre, args.map(resolver))
    case otro => otro
  }

  val e1 = resolver(expr1)
  val e2 = resolver(expr2)

  (e1, e2) match {
    case _ if e1 == e2 => sustitucion
    case (v: Variable, otraExpr) => sustitucion.updated(v, otraExpr)
    case (otraExpr, v: Variable) => sustitucion.updated(v, otraExpr)
    case (Predicado(n1, args1), Predicado(n2, args2))
        if n1 == n2 && args1.length == args2.length =>
            unificarListas(args1, args2, sustitucion)
    case _ => throw UnificacionError
  }
}

private def unificarListas(l1: List[Expresion], l2: List[Expresion], sustitucionInicial: Sustituciones): Sustituciones = {
  (l1, l2) match {
    case (Nil, Nil) => sustitucionInicial
    case (x :: xs, y :: ys) =>
      val nuevasSustituciones = unificar(x, y, sustitucionInicial)
      unificarListas(xs, ys, nuevasSustituciones)
    case _ => throw UnificacionError
  }
}