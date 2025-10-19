import scala.io.Source
import scala.util.Try
import classes._
import parse.Parser._   // usamos parseLines y parsePredicado

object Main {

  private def stripDot(s: String): String =
    s match
      case s if s.nonEmpty && s.last == '.' => s.dropRight(1)
      case _ => s


  private def splitTopLevelCommas(s: String): List[String] = {
    @annotation.tailrec
    def loop(chars: List[Char], depth: Int, current: String, acc: List[String]): List[String] = {
      chars match{
        case Nil =>
          val part = current.trim
          if (part.nonEmpty) (part :: acc).reverse else acc.reverse
        case '(' :: rest =>
          loop(rest, depth + 1, current + '(', acc)
        case ')' :: rest =>
          loop(rest, (depth - 1).max(0), current + ')', acc)
        case ',' :: rest if depth == 0 =>
          loop(rest, depth, "", acc :+ current.trim)
        case ch :: rest =>
          loop(rest, depth, current + ch, acc)
      }
    }
    loop(s.toList, 0, "", List.empty)
  }

  private def parseQuery(line: String): List[Predicado] = {
    val raw = line.trim
    raw match
      case "" => Nil
      case s if s.startsWith("%") => Nil
      case s if s.startsWith("?-") => parseContenido(s.drop(2).trim)
      case s => parseContenido(s)
  } 

  private def parseContenido(raw: String):List[Predicado] = {
      val sinPunto = stripDot(raw)
      sinPunto match
        case "" => Nil
        case s => splitTopLevelCommas(s).map(parsePredicado)
  }    
  
  private def validarEntrada(args: Array[String]):Option[(String, String)] = {
    args.length match {
      case n if n < 2 =>
        System.err.println("Uso: sbt \"run path/de/entrada.pl path/de/input\"")
        System.err.println("      (usa '-' como segundo argumento para leer consultas por STDIN)")
        None
      case n if n > 2 =>
        System.err.println("Uso: sbt \"run path/de/entrada.pl path/de/input\"")
        System.err.println("      (usa '-' como segundo argumento para leer consultas por STDIN)")
        None
      case _ => Some((args(0), args(1)))
    }
  }

  private def obtenerEntrada(path: String): Iterator[String] = {
    path match
      case "-" => 
        println("Leyendo consultas desde STDIN (salir con EXIT")
        io.Source.stdin.getLines().takeWhile(line => line.trim.toUpperCase != "EXIT")
      case path =>
        val entrada = new java.io.File(path)
        if (!entrada.exists()) { System.err.println(s"No se encontro $path"); System.exit(1) }
        Source.fromFile(entrada).getLines()
  }
    

  def main(args: Array[String]): Unit = {
    validarEntrada(args) match
      case None => System.exit(1)
      case Some((pathKB, pathConsult)) =>
        val fkb = new java.io.File(pathKB)
        if (!fkb.exists()) { System.err.println(s"No se encontro $pathKB"); System.exit(1) }
        val kbLines = Source.fromFile(fkb).getLines().toList
        val kb: List[Conocimiento] = parseLines(kbLines)

        val consultasIter= obtenerEntrada(pathConsult)

        consultasIter
          .map(_.trim)
          .foreach { raw =>
            val metas = parseQuery(raw)
            metas match
              case Nil if metas.isEmpty => () // linea vacia o comentario, lo ignoramos
              case _ => 
                val soluciones = Try(Engine.resolverCuerpo(metas, kb, Map.empty[Variable, Expresion])).getOrElse(Nil)
                val out        = Engine.formatResultado(metas, soluciones)
                println(out)
          
          }
  }
}