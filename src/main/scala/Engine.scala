// Motor de inferencia

import classes._
import scala.util.Try
import scala.collection.mutable

object Engine {
  private var _freshCounter: Long = 0L
  private def freshId(): Long = { _freshCounter += 1; _freshCounter }

  // aplicar sustituciones recursivamente
  def aplicarSustitucion(exp: Expresion, subs: Map[Variable, Expresion]): Expresion = {
    def resolver(e: Expresion): Expresion = e match {
      case v: Variable if subs.contains(v) => resolver(subs(v))
      case Predicado(n, args) => Predicado(n, args.map(resolver))
      case other => other
    }
    resolver(exp)
  }

  // acepta enteros y decimales con punto, ej: 7, 7.0, -3.25
  private def parseNumberAtom(n: String): Option[Double] = {
    val Num = "^-?\\d+(?:\\.\\d+)?$".r
    n match {
      case Num() => Some(n.toDouble)
      case _ => None
    }
  }

  private def getNumber(e: Expresion, s: Map[Variable, Expresion]): Option[Double] =
    aplicarSustitucion(e, s) match {
      case Atomo(n) => parseNumberAtom(n)
      case _ => None
    }

  // para imprimir sin .0 cuando es entero
  private def numToAtom(d: Double): Atomo =
    if (d.isWhole) Atomo(d.toLong.toString) else Atomo(d.toString)
  // extraer variables en orden de aparición
  def extraerVariables(exp: Expresion): List[Variable] = {
    val buf = scala.collection.mutable.ListBuffer.empty[Variable]
    def visit(e: Expresion): Unit = e match {
      case v: Variable => if (!buf.exists(_.nombre == v.nombre)) buf += v
      case Predicado(_, args) => args.foreach(visit)
      case _ => ()
    }
    visit(exp)
    buf.toList
  }

  private def standardizeApart(regla: Regla): Regla = {
    val id = freshId()
    val mapping = scala.collection.mutable.Map.empty[String, Variable]
    def renameVar(v: Variable): Variable = mapping.getOrElseUpdate(v.nombre, Variable(s"${v.nombre}_$id"))
    def renameExp(e: Expresion): Expresion = e match {
      case v: Variable => renameVar(v)
      case Atomo(n) => Atomo(n)
      case Predicado(n, args) => Predicado(n, args.map(renameExp))
    }
    val nuevaCabeza = renameExp(regla.cabeza).asInstanceOf[Predicado]
    val nuevoCuerpo = regla.argumentos.map(p => renameExp(p).asInstanceOf[Predicado])
    Regla(nuevaCabeza, nuevoCuerpo)
  }

  //Built-ins
  private def evalNum(e: Expresion, s: Map[Variable, Expresion]): Option[Long] = aplicarSustitucion(e, s) match {
    case Atomo(n) if n.matches("^-?\\d+$") => Some(n.toLong)
    case Predicado("add", List(a, b)) => for (x <- evalNum(a, s); y <- evalNum(b, s)) yield x + y
    case Predicado("sub", List(a, b)) => for (x <- evalNum(a, s); y <- evalNum(b, s)) yield x - y
    case Predicado("mult", List(a, b)) => for (x <- evalNum(a, s); y <- evalNum(b, s)) yield x * y
    case _ => None
  }

  private[Engine] def evaluarBuiltIn(goal: Predicado, subs: Map[Variable, Expresion]): Option[Map[Variable, Expresion]] = {
    def bindIfVar(target: Expresion, valueAtomo: Atomo, s: Map[Variable, Expresion]): Option[Map[Variable, Expresion]] =
      aplicarSustitucion(target, s) match {
        case v: Variable                 => Some(s.updated(v, valueAtomo))
        case a: Atomo if a == valueAtomo => Some(s)
        case _                           => None
      }

    val name = goal.nombre.toLowerCase
    val args = goal.argumentos
    // Evaluar expresiones aritméticas complejas
    def evalNum(e: Expresion, s: Map[Variable, Expresion]): Option[Double] = aplicarSustitucion(e, s) match {
          case Atomo(n) => parseNumberAtom(n)
          case Predicado(op, List(a,b)) if Set("suma","resta","multiplicacion","add","sub","mult").contains(op.toLowerCase) =>
            for { x <- evalNum(a,s); y <- evalNum(b,s) } yield op.toLowerCase match {
              case "suma" | "add"            => x + y
              case "resta" | "sub"           => x - y
              case "multiplicacion" | "mult" => x * y
            }
          case _ => None
        }

    name match {
      case "=" if args.length == 2 =>
        scala.util.Try(unificar(args(0), args(1), subs)).toOption

      case "is" if args.length == 2 =>
        evalNum(args(1), subs).flatMap(n => scala.util.Try(unificar(args(0), numToAtom(n), subs)).toOption)

      case "suma" | "add" if args.length == 3 =>
        for {
          a <- evalNum(args(0), subs)
          b <- evalNum(args(1), subs)
          s2 <- bindIfVar(args(2), numToAtom(a + b), subs)
        } yield s2

      case "resta" | "sub" if args.length == 3 =>
        for {
          a <- evalNum(args(0), subs)
          b <- evalNum(args(1), subs)
          s2 <- bindIfVar(args(2), numToAtom(a - b), subs)
        } yield s2

      case "multiplicacion" | "mult" if args.length == 3 =>
        for {
          a <- evalNum(args(0), subs)
          b <- evalNum(args(1), subs)
          s2 <- bindIfVar(args(2), numToAtom(a * b), subs)
        } yield s2

      case "eq"  if args.length == 2 =>
        (evalNum(args(0), subs), evalNum(args(1), subs)) match {
          case (Some(a), Some(b)) if a == b => Some(subs)
          case _ => None
        }
      case "neq" if args.length == 2 =>
        (evalNum(args(0), subs), evalNum(args(1), subs)) match {
          case (Some(a), Some(b)) if a != b => Some(subs)
          case _ => None
        }
      case "gt"  if args.length == 2 =>
        (evalNum(args(0), subs), evalNum(args(1), subs)) match {
          case (Some(a), Some(b)) if a > b  => Some(subs)
          case _ => None
        }

      case _ => None
    }
  }


  // resolución / backtracking
  def resolverCuerpo(metas: List[Predicado], kb: List[Conocimiento], subs: Map[Variable, Expresion]): List[Map[Variable, Expresion]] = metas match {
    case Nil => List(subs)
    case head :: tail =>
      val headA = aplicarSustitucion(head, subs).asInstanceOf[Predicado]
      resolverMeta(headA, kb, subs).flatMap { s2 =>
        val tailA = tail.map(t => aplicarSustitucion(t, s2).asInstanceOf[Predicado])
        resolverCuerpo(tailA, kb, s2)
      }
  }

  private def resolverMeta(meta: Predicado, kb: List[Conocimiento], subs: Map[Variable, Expresion]): List[Map[Variable, Expresion]] = {
    evaluarBuiltIn(meta, subs) match {
      case Some(s) => List(s)
      case None =>
        kb.to(LazyList).flatMap {
          case Hecho(p) => Try(unificar(meta, p, subs)).toOption.toList
          case Regla(cabeza, cuerpo) =>
            val reglaFresh = standardizeApart(Regla(cabeza, cuerpo))
            Try(unificar(meta, reglaFresh.cabeza, subs)).toOption.toList.flatMap { subs2 =>
              resolverCuerpo(reglaFresh.argumentos.map(a => aplicarSustitucion(a, subs2).asInstanceOf[Predicado]), kb, subs2)
            }
          case _ => Nil
        }.toList
    }
  }


  def consultarPredicado(consultaPred: Predicado, kb: List[Conocimiento]): List[Map[Variable, Expresion]] = resolverCuerpo(List(consultaPred), kb, Map.empty)

  def consultar(conocimientoConsulta: Conocimiento, kb: List[Conocimiento]): List[Map[Variable, Expresion]] = conocimientoConsulta match {
    case Hecho(pred) => consultarPredicado(pred, kb)
    case _ => Nil
  }

  def formatResultado(consulta: List[Predicado], soluciones: List[Map[Variable, Expresion]]): String = {
    if (consulta.isEmpty) return "false"

    val varsOrdenadas: List[Variable] =
      consulta.flatMap(extraerVariables)
        .foldLeft(List.empty[Variable]) { (acc, v) =>
          if (acc.exists(_.nombre == v.nombre)) acc else acc :+ v
        }

    if (varsOrdenadas.isEmpty) {
      if (soluciones.nonEmpty) "true" else "false"
    } else {
      soluciones.headOption match {
        case None => "false"
        case Some(sol) =>
          val ligaduras = varsOrdenadas.map { v =>
            aplicarSustitucion(v, sol) match {
              case Atomo(n) => s"${v.nombre} = $n"
              case Variable(n) => s"${v.nombre} = $n"
              case p: Predicado => s"${v.nombre} = ${predicadoToString(p)}"
            }
          }
          s"true (${ligaduras.mkString("; ")})"
      }
    }
  }
  private def predicadoToString(p: Predicado): String = {
    val args = p.argumentos.map {
      case Atomo(n) => n
      case Variable(n) => n
      case Predicado(n2, a2) => predicadoToString(Predicado(n2, a2))
      case other => other.toString
    }.mkString(", ")
    s"${p.nombre}(${args})"
  }
}