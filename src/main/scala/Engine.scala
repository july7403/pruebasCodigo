// Motor de inferencia - Versión funcional pura

import classes._

object Engine {
  // Contexto para generar identificadores únicos de variables
  case class ContextoFresco(contador: Long = 0L) {
    def siguiente: (Long, ContextoFresco) = (contador, ContextoFresco(contador + 1))
  }

  // Aplicar sustituciones de forma recursiva
  def aplicarSustitucion(expresion: Expresion, sustituciones: Map[Variable, Expresion]): Expresion = {
    def resolverExpresion(exp: Expresion): Expresion = exp match {
      case variable: Variable => 
        sustituciones.get(variable).map(resolverExpresion).getOrElse(variable)
      case Predicado(nombre, argumentos) => 
        Predicado(nombre, argumentos.map(resolverExpresion))
      case otro => otro
    }
    resolverExpresion(expresion)
  }

  // Parsear números (enteros y decimales)
  private def parsearNumero(texto: String): Option[Double] = {
    val NumeroRegex = "^-?\\d+(?:\\.\\d+)?$".r
    texto match {
      case NumeroRegex() => scala.util.Try(texto.toDouble).toOption
      case _ => None
    }
  }

  private def obtenerNumero(expresion: Expresion, subs: Map[Variable, Expresion]): Option[Double] =
    aplicarSustitucion(expresion, subs) match {
      case Atomo(valor) => parsearNumero(valor)
      case _ => None
    }

  // Convertir número a átomo 
  private def numeroAAtomo(numero: Double): Atomo = numero match {
    case n if n.isWhole => Atomo(n.toLong.toString)
    case n => Atomo(n.toString)
  }

  // Extraer variables en orden de aparición
  def extraerVariables(expresion: Expresion): List[Variable] = {
    def visitar(exp: Expresion, yaVistas: Set[String]): (List[Variable], Set[String]) = exp match {
      case variable: Variable => 
        (yaVistas.contains(variable.nombre), variable) match {
          case (true, _) => (Nil, yaVistas)
          case (false, v) => (List(v), yaVistas + v.nombre)
        }
      case Predicado(_, argumentos) =>
        val (variablesAlReves, vistasFinales) = argumentos.foldLeft((List.empty[Variable], yaVistas)) { 
          case ((acumuladorReverso, vistas), argumento) =>
            val (nuevasVars, nuevasVistas) = visitar(argumento, vistas)
            (nuevasVars.reverse ::: acumuladorReverso, nuevasVistas)
        }
        (variablesAlReves.reverse, vistasFinales)
      case _ => (Nil, yaVistas)
    }
    visitar(expresion, Set.empty)._1
  }
  private def renombrarVariables(regla: Regla, contexto: ContextoFresco): (Regla, ContextoFresco) = {
    val (identificador, nuevoContexto) = contexto.siguiente
    
    def renombrar(exp: Expresion, mapeoNombres: Map[String, Variable]): (Expresion, Map[String, Variable]) = exp match {
      case variable: Variable =>
        mapeoNombres.get(variable.nombre) match {
          case Some(variableRenombrada) => (variableRenombrada, mapeoNombres)
          case None =>
            val variableNueva = Variable(s"${variable.nombre}_$identificador")
            (variableNueva, mapeoNombres + (variable.nombre -> variableNueva))
        }
      case Atomo(nombre) => (Atomo(nombre), mapeoNombres)
      case Predicado(nombre, argumentos) =>
        val (argumentosReversos, mapeoFinal) = argumentos.foldLeft((List.empty[Expresion], mapeoNombres)) {
          case ((acumuladorReverso, mapeo), argumento) =>
            val (renombrado, nuevoMapeo) = renombrar(argumento, mapeo)
            (renombrado :: acumuladorReverso, nuevoMapeo)
        }
        (Predicado(nombre, argumentosReversos.reverse), mapeoFinal)
    }

    val (cabezaNueva, mapeoTrasRenombrarCabeza) = renombrar(regla.cabeza, Map.empty)
    val (cuerpoReverso, _) = regla.argumentos.foldLeft((List.empty[Expresion], mapeoTrasRenombrarCabeza)) {
      case ((acumuladorReverso, mapeo), predicado) =>
        val (renombrado, nuevoMapeo) = renombrar(predicado, mapeo)
        (renombrado :: acumuladorReverso, nuevoMapeo)
    }

    (Regla(cabezaNueva.asInstanceOf[Predicado], cuerpoReverso.reverse.map(_.asInstanceOf[Predicado])), nuevoContexto)
  }

  // Evaluar expresiones numéricas
  private def evaluarNumero(expresion: Expresion, subs: Map[Variable, Expresion]): Option[Double] = 
    aplicarSustitucion(expresion, subs) match {
      case Atomo(valor) => parsearNumero(valor)
      case Predicado(operador, List(izquierda, derecha)) if Set("suma", "resta", "multiplicacion", "add", "sub", "mult").contains(operador.toLowerCase) =>
        (evaluarNumero(izquierda, subs), evaluarNumero(derecha, subs)) match {
          case (Some(valorIzq), Some(valorDer)) => operador.toLowerCase match {
            case "suma" | "add" => Some(valorIzq + valorDer)
            case "resta" | "sub" => Some(valorIzq - valorDer)
            case "multiplicacion" | "mult" => Some(valorIzq * valorDer)
            case _ => None
          }
          case _ => None
        }
      case _ => None
    }

  private def vincularSiEsVariable(objetivo: Expresion, valorAtomo: Atomo, subs: Map[Variable, Expresion]): Option[Map[Variable, Expresion]] =
    aplicarSustitucion(objetivo, subs) match {
      case variable: Variable => Some(subs.updated(variable, valorAtomo))
      case atomo: Atomo if atomo == valorAtomo => Some(subs)
      case _ => None
    }

  // Evaluar predicados built-in
  private[Engine] def evaluarBuiltIn(meta: Predicado, sustituciones: Map[Variable, Expresion]): Option[Map[Variable, Expresion]] = {
    val nombrePredicado = meta.nombre.toLowerCase
    val argumentos = meta.argumentos

    nombrePredicado match {
      case "=" if argumentos.length == 2 =>
        unificar(argumentos(0), argumentos(1), sustituciones)

      case "is" if argumentos.length == 2 =>
        evaluarNumero(argumentos(1), sustituciones).flatMap(resultado => 
          unificar(argumentos(0), numeroAAtomo(resultado), sustituciones)
        )

      case "suma" | "add" if argumentos.length == 3 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) => vincularSiEsVariable(argumentos(2), numeroAAtomo(primerValor + segundoValor), sustituciones)
          case _ => None
        }

      case "resta" | "sub" if argumentos.length == 3 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) => vincularSiEsVariable(argumentos(2), numeroAAtomo(primerValor - segundoValor), sustituciones)
          case _ => None
        }

      case "multiplicacion" | "mult" if argumentos.length == 3 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) => vincularSiEsVariable(argumentos(2), numeroAAtomo(primerValor * segundoValor), sustituciones)
          case _ => None
        }

      case "eq" if argumentos.length == 2 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) if primerValor == segundoValor => Some(sustituciones)
          case _ => None
        }

      case "neq" if argumentos.length == 2 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) if primerValor != segundoValor => Some(sustituciones)
          case _ => None
        }

      case "gt" if argumentos.length == 2 =>
        (evaluarNumero(argumentos(0), sustituciones), evaluarNumero(argumentos(1), sustituciones)) match {
          case (Some(primerValor), Some(segundoValor)) if primerValor > segundoValor => Some(sustituciones)
          case _ => None
        }

      case _ => None
    }
  }

  // Resolver una meta individual
  private def resolverMeta(meta: Predicado, baseConocimiento: List[Conocimiento], sustituciones: Map[Variable, Expresion], contexto: ContextoFresco): (List[Map[Variable, Expresion]], ContextoFresco) = {
    evaluarBuiltIn(meta, sustituciones) match {
      case Some(sustitucionesNuevas) => (List(sustitucionesNuevas), contexto)
      case None =>
        baseConocimiento.foldRight((List.empty[Map[Variable, Expresion]], contexto)) { case (conocimiento, (solucionesAcumuladas, contextoActual)) =>
          conocimiento match {
            case Hecho(predicado) =>
              unificar(meta, predicado, sustituciones) match {
                case Some(sustitucionesNuevas) => (sustitucionesNuevas :: solucionesAcumuladas, contextoActual)
                case None => (solucionesAcumuladas, contextoActual)
              }
            
            case Regla(cabeza, cuerpo) =>
              val (reglaRenombrada, contextoSiguiente) = renombrarVariables(Regla(cabeza, cuerpo), contextoActual)
              unificar(meta, reglaRenombrada.cabeza, sustituciones) match {
                case Some(sustitucionesTrasUnificar) =>
                  val metasActualizadas = reglaRenombrada.argumentos.map(argumento => 
                    aplicarSustitucion(argumento, sustitucionesTrasUnificar).asInstanceOf[Predicado]
                  )
                  val (solucionesCuerpo, contextoFinal) = resolverCuerpo(metasActualizadas, baseConocimiento, sustitucionesTrasUnificar, contextoSiguiente)
                  (solucionesCuerpo ::: solucionesAcumuladas, contextoFinal)
                case None => (solucionesAcumuladas, contextoSiguiente)
              }
            
            case _ => (solucionesAcumuladas, contextoActual)
          }
        }
    }
  }

  def resolverCuerpo(metas: List[Predicado], baseConocimiento: List[Conocimiento], sustituciones: Map[Variable, Expresion], contexto: ContextoFresco = ContextoFresco()): (List[Map[Variable, Expresion]], ContextoFresco) = {
    metas match {
      case Nil => (List(sustituciones), contexto)
      case primeraMeta :: restoMetas =>
        val metaActualizada = aplicarSustitucion(primeraMeta, sustituciones).asInstanceOf[Predicado]
        val (solucionesPrimerasMeta, contextoTrasResolverPrimera) = resolverMeta(metaActualizada, baseConocimiento, sustituciones, contexto)
        
        solucionesPrimerasMeta.foldRight((List.empty[Map[Variable, Expresion]], contextoTrasResolverPrimera)) { case (sustitucionActual, (solucionesAcumuladas, contextoActual)) =>
          val metasRestantesActualizadas = restoMetas.map(meta => aplicarSustitucion(meta, sustitucionActual).asInstanceOf[Predicado])
          val (solucionesResto, contextoSiguiente) = resolverCuerpo(metasRestantesActualizadas, baseConocimiento, sustitucionActual, contextoActual)
          (solucionesResto ::: solucionesAcumuladas, contextoSiguiente)
        }
    }
  }

  def resolverCuerpo(metas: List[Predicado], baseConocimiento: List[Conocimiento], sustituciones: Map[Variable, Expresion]): List[Map[Variable, Expresion]] = {
    resolverCuerpo(metas, baseConocimiento, sustituciones, ContextoFresco())._1
  }

  def consultarPredicado(predicadoConsulta: Predicado, baseConocimiento: List[Conocimiento]): List[Map[Variable, Expresion]] = 
    resolverCuerpo(List(predicadoConsulta), baseConocimiento, Map.empty)

  def consultar(conocimientoConsulta: Conocimiento, baseConocimiento: List[Conocimiento]): List[Map[Variable, Expresion]] = 
    conocimientoConsulta match {
      case Hecho(predicado) => consultarPredicado(predicado, baseConocimiento)
      case _ => Nil
    }

  def formatResultado(consulta: List[Predicado], soluciones: List[Map[Variable, Expresion]]): String = {
    consulta match {
      case Nil => "false"
      case _ =>
        val (variablesReverso, _) = consulta.flatMap(extraerVariables)
          .foldLeft((List.empty[Variable], Set.empty[String])) { 
            case ((listaAcumulada, nombresVistos), variable) if !nombresVistos.contains(variable.nombre) =>
              (variable :: listaAcumulada, nombresVistos + variable.nombre)
            case (acumulador, _) => 
              acumulador
          }
        val variablesOrdenadas = variablesReverso.reverse

        variablesOrdenadas match {
          case Nil =>
            soluciones.headOption match {
              case Some(_) => "true"
              case None => "false"
            }
          case variables =>
            soluciones.headOption match {
              case None => "false"
              case Some(solucion) =>
                val ligaduras = variables.map { variable =>
                  aplicarSustitucion(variable, solucion) match {
                    case Atomo(valor) => s"${variable.nombre} = $valor"
                    case Variable(nombre) => s"${variable.nombre} = $nombre"
                    case predicado: Predicado => s"${variable.nombre} = ${predicadoATexto(predicado)}"
                  }
                }
                s"true (${ligaduras.mkString("; ")})"
            }
        }
    }
  }

  private def predicadoATexto(predicado: Predicado): String = {
    val argumentosTexto = predicado.argumentos.map {
      case Atomo(valor) => valor
      case Variable(nombre) => nombre
      case Predicado(nombre, args) => predicadoATexto(Predicado(nombre, args))
      case otro => otro.toString
    }.mkString(", ")
    s"${predicado.nombre}(${argumentosTexto})"
  }
}