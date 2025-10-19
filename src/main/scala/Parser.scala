package parse
import classes._
import scala.compiletime.ops.string

object Parser {

  def parseExpresion(token: String): Expresion = {
    val t = token.trim
    t match
      case "" => Atomo("")
      case t if t.head.isUpper || t.head == '_' => Variable(t)
      case t if t.contains("(") => parsePredicado(t)
      case _ => Atomo(t)
  }

  @scala.annotation.tailrec
  private def splitComasRec(
      s:List[Char],
      actual:String,
      profundidad:Int,
      partes:List[String]): List[String] = {
        s match
          case Nil => 
            val ultima = actual.trim
             ultima match { 
              case "" => partes
              case _ => partes :+ ultima
             }
          case '(' :: tail =>
            splitComasRec(tail, actual + '(', profundidad + 1, partes)
          case ')' :: tail =>
            splitComasRec(tail, actual + ')', (profundidad - 1).max(0), partes)
          case ',' :: tail if profundidad == 0 =>
            splitComasRec(tail, "",  0 , partes :+ actual.trim)
          case c :: tail =>
            splitComasRec(tail, actual + c, profundidad, partes)
      }
  private def splitComas(s: String): List[String] = {
    splitComasRec(s.toList, "" , 0 , List.empty)
  }
    

  def parsePredicado(input: String): Predicado = {
    val s = input.trim
    s.indexOf('(') match {
      case -1 => Predicado(s, List.empty)
      case idx =>
        val nombre = s.take(idx).trim
        val argsStr = s.substring(idx + 1, s.length - 1).trim
        val argumentos = argsStr match {
          case "" => Nil
          case str => splitComas(str).map(parseExpresion)
        }
        Predicado(nombre, argumentos)
    }
  }

  def parseHecho(input: String): Hecho = Hecho(parsePredicado(input))

  def parseRule(input: String): Regla = {
    val parts = input.split(":-", 2).map(_.trim).toList

    parts match {
      case cabezaStr :: cuerpoStr :: Nil =>
        val cabeza = parsePredicado(cabezaStr)
        val normalizar = (s:String) => s
            .replace(" is ", "_is_")
            .replace(" = ", "_equal_")
        val metasRaw = splitComas(normalizar(cuerpoStr))
                          .map(_.trim).filter(_.nonEmpty)
        val cuerpo = metasRaw.map { meta =>
          meta match {
            case meta if meta.contains("_is_") =>
              val Array(left, right) = meta.split("_is_", 2).map(_.trim)
              Predicado("is", List(parseExpresion(left), parseExpresion(right)))
            case meta if meta.contains("_equal_") =>
              val Array(left, right) = meta.split("_equal_", 2).map(_.trim)
              Predicado("=", List(parseExpresion(left), parseExpresion(right)))
            case meta =>
              parsePredicado(meta)
          }
        }
          Regla(cabeza, cuerpo)
      case _ =>
        throw new IllegalArgumentException(s"Regla inválida: $input")
          }
    }
  
  def parseConocimiento(lineNoDot: String): Conocimiento = {
    val s = lineNoDot.trim
    s match
      case s if s.contains(":-") => parseRule(s)
      case _ => parseHecho(s)
  }

  def parseLine(line: String): Option[Conocimiento] = {
    val l = line.trim
    l match
      case "" => None
      case s if s.startsWith("%") => None
      case s if s.endsWith(".") =>
        val noDot = s.dropRight(1).trim
        Some(parseConocimiento(noDot))
      case _ =>
        Some(parseConocimiento(l)) 
  }

  def parseLines(lines: List[String]): List[Conocimiento] = lines.flatMap(parseLine)
}