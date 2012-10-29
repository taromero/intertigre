package intertigre.services
import intertigre.domain.Categoria
import intertigre.domain.Club
import intertigre.domain.Equipo
import intertigre.domain.Fecha
import intertigre.domain.Fixture
import intertigre.domain.Grupo

import org.joda.time.DateTime
import org.joda.time.LocalDate;



class FixtureService {

	final def MAX_EQUIPOS_POR_GRUPO = 7
	final LocalDate INICIO = new DateTime(2012, 6, 30, 0,0,0).toLocalDate()
	final LocalDate FIN = new DateTime(2012, 9, 30, 0,0,0).toLocalDate()
	final def HORARIOS = [10, 12, 14, 16, 18, 20, 22]

	Random random = new Random()

	def Fixture generarFixture(Categoria categoria, maxEquiposPorGrupo = MAX_EQUIPOS_POR_GRUPO, LocalDate inicio = INICIO, LocalDate fin = FIN, List<BigDecimal> horarios = HORARIOS){
		def equipos = Equipo.findAllByCategoria(categoria)
		Fixture fixture =  this.generarFixture(categoria, equipos, maxEquiposPorGrupo, inicio, fin, horarios)
		fixture.grupos.each{ it.save() }

		return fixture
	}

	def Fixture generarFixture(Categoria categoria, List<Equipo> equipos, maxEquiposPorGrupo = MAX_EQUIPOS_POR_GRUPO, LocalDate inicio = INICIO, LocalDate fin = FIN,
								 List<BigDecimal> horarios = HORARIOS){
		def cantidadGrupos = this.calcularCantidadDeGrupos(equipos, maxEquiposPorGrupo)
		def cantidadEquiposPorGrupoMap = calcularCantidadEquiposPorGrupo(equipos.size(), cantidadGrupos)
		def cantidadEquiposPorGrupo = cantidadEquiposPorGrupoMap.cantidad
		def cantidadGruposConMayorNumeroEquipos = cantidadEquiposPorGrupoMap.cantidadGruposConMayor
		def grupos = this.generateGrupos(equipos, cantidadGrupos, cantidadEquiposPorGrupo, cantidadGruposConMayorNumeroEquipos)
		def diasJugables = getDiasJugables(inicio, fin, horarios)
		def cantGruposTratados = 0
		List<Fecha> fechas = new ArrayList<Fecha>()
		for(grupo in grupos){
			if(cantGruposTratados==cantidadGruposConMayorNumeroEquipos){
				cantidadEquiposPorGrupo = cantidadEquiposPorGrupo - 1
			}
			def maximaCantLocalias = (cantidadEquiposPorGrupo % 2) == 0 ? cantidadEquiposPorGrupo/2 : Math.floor(cantidadEquiposPorGrupo/2)
			def equiposGrupo = grupo.equipos
			for(local in equiposGrupo){
				def cantidadLocalias = 0
				def equiposRivalesOrdenados = darlePrioridadALosEquiposQueNoVisito(local, equiposGrupo)
				for(visitante in equiposRivalesOrdenados){
					if(!hayFechaEntreEquipos(local, visitante)){
						def fecha = new Fecha(categoria: categoria, grupo: grupo,equipoLocal: local, equipoVisitante: visitante,
									fechaDeJuego: getDiaHorarioDisponibleParaAmbos(local, visitante, diasJugables).toDate())
						agregoFechaAListaYaEquiposYaGrupo(fechas, fecha, local, visitante, grupo)
						cantidadLocalias++
					}
					if(cantidadLocalias == maximaCantLocalias){
						break
					}
				}
			}
			cantGruposTratados++
		}
		return new Fixture(fechas: fechas, grupos: grupos)
	}

	private List<Equipo> darlePrioridadALosEquiposQueNoVisito(Equipo local, equiposGrupo){
		return equiposGrupo.sort(false, { local.getEquiposVisitantes().contains(it) }).findAll { it != local }  //Si lo contiene, le da menos prioridad
	}

	private List<Integer> getIndicesEquipo(List<Equipo> equipos){
		List<Integer> indicesEquiposElegibles = []
		for(i in 0..equipos.size()-1){
			indicesEquiposElegibles.add(i)
		} 
		return indicesEquiposElegibles
	}

	private void agregoFechaAListaYaEquiposYaGrupo(List<Fecha> fechas, Fecha fecha, Equipo local, Equipo visitante, Grupo grupo){
		fechas.add(fecha)
		local.fechasLocal.add(fecha)
		visitante.fechasVisitante.add(fecha)
		grupo.addToFechas(fecha)
	}

	def List<Grupo> generateGrupos(List<Equipo> equiposParameter, cantidadGrupos, equiposPorGrupo, cantidadGruposConMayorNumeroEquipos){
		def grupos = new ArrayList<Grupo>()
		def letra = 'A'
		def maxEquiposPorGrupo = equiposPorGrupo
		def equipos = equiposParameter.clone()
		Collections.shuffle(equipos)
		equipos = priorizarEquiposDeClubesConMayorCantidadDeEquipos(equipos)
		for(i in 1..cantidadGrupos){
			if(cantidadGruposConMayorNumeroEquipos > 0 && (i-1) == cantidadGruposConMayorNumeroEquipos){
				maxEquiposPorGrupo = equiposPorGrupo - 1
			}
			List<Equipo> equiposGrupo = new ArrayList<Equipo>();
			
			def equiposOrdenados = priorizarEquiposCuyosClubesEstanEnMenorCantidadDeGrupos(equipos, grupos)
			for(j in 1..maxEquiposPorGrupo){
				equiposOrdenados = priorizarLosEquiposDeClubesQueNoEstanEnElGrupo(equiposGrupo, equiposOrdenados)
				for(equipo in equiposOrdenados){
					if(!equiposGrupo.contains(equipo)){
						equiposGrupo.add(equipo)
						break
					}
				}
			}
			def grupo = new Grupo(nombre: letra, equipos: equiposGrupo)
			grupos.add(grupo)
			equiposGrupo.each { equipos.remove(it); it.grupo = grupo }
			letra++
		}
		return grupos
	}

	private List<Equipo> priorizarEquiposDeClubesConMayorCantidadDeEquipos(List<Equipo> equipos){
		return equipos.sort(false) { equipo -> equipos*.club.count { it != equipo.club } }
	}

	private List<Equipo> priorizarLosEquiposDeClubesQueNoEstanEnElGrupo(List<Equipo> equiposGrupo, List<Equipo> equipos){
		return equipos.sort(false) { equipo -> equiposGrupo*.club.contains(equipo.club) }
	}

	private List<Equipo> priorizarEquiposCuyosClubesEstanEnMenorCantidadDeGrupos(List<Equipo> equipos, List<Grupo> grupos){
		return equipos.sort(false) { equipo -> grupos.count { g -> g.equipos*.club.contains(equipo.club) } }
	}
	
	def Integer calcularCantidadDeGrupos(List<Equipo> equipos, Integer maxEquiposPorGrupo = MAX_EQUIPOS_POR_GRUPO){
		if((equipos.size % maxEquiposPorGrupo) != 0){
			return (Math.floor(equipos.size/maxEquiposPorGrupo) + 1).toInteger() 
		}else{
			return (equipos.size/maxEquiposPorGrupo).toInteger()
		}
	}

	def Map calcularCantidadEquiposPorGrupo(Integer cantidadEquipos, Integer cantidadGrupos){
		if((cantidadEquipos % cantidadGrupos) == 0){
			return [cantidad: (cantidadEquipos/cantidadGrupos).toInteger(), cantidadGruposConMayor: -1]
		}else{
			def calculado = false
			Integer cantidadMayor = (cantidadEquipos/cantidadGrupos).toInteger() + 1
			Integer cantidadGruposConMayor = 0
			while(!calculado && cantidadMayor > 0){
				for(int cantidadMayorAux=cantidadMayor; cantidadMayorAux<cantidadEquipos; cantidadMayorAux=cantidadMayorAux+cantidadMayor){
					cantidadGruposConMayor++
					if((cantidadEquipos-cantidadMayorAux) % (cantidadMayor-1) == 0){
						calculado = true
						break
					}
				}
				if(!calculado){
					cantidadMayor--
				}
			}
			return [cantidad: cantidadMayor, cantidadGruposConMayor: cantidadGruposConMayor]
		}
	}

	def List<DateTime> getDiasJugables(LocalDate inicio = INICIO, LocalDate fin = FIN, List<BigDecimal> horarios = HORARIOS){
		List<DateTime> diasJugables = new ArrayList<DateTime>()
		LocalDate f = inicio
		while(f.isBefore(fin) || f == fin){
			for(horario in horarios){
				diasJugables.add(new DateTime(f.year, f.monthOfYear, f.dayOfMonth, horario,0,0))
			}
			f = f.plusWeeks(1)
		}
		return diasJugables
	}
	
	def DateTime getDiaHorarioDisponibleParaAmbos(Equipo local, Equipo visitante, List<DateTime> diasDisponibles){
		def horariosPreferidosLocal = local.club.horariosPreferidosParaLocal
		def diasHorariosPriorizados = diasDisponibles.grep { horariosPreferidosLocal.contains(it.getHourOfDay()) }
													  .sort { horariosPreferidosLocal.indexOf(it.getHourOfDay()) }
		def diasHorarios = diasHorariosPriorizados.isEmpty() ? diasDisponibles : diasHorariosPriorizados
		for(diaHora in diasHorarios){
			if(!juegaAlgunoEseDia(local, visitante, diaHora)){
				if(hayCanchasDisponiblesEnClubLocalEnEseDiaHora(local.club, diaHora)){
					return diaHora
				}
			}
		}
		throw new RuntimeException('No hay dia/horario disponible para que puedan jugar estos equipos entre si, en el intervalo deseado')
	}
	
	def Boolean hayCanchasDisponiblesEnClubLocalEnEseDiaHora(Club clubLocal, DateTime diaHora){
		clubLocal.equipos*.fechas.flatten().findAll{ it.equipoLocal.club == clubLocal }*.fechaDeJuego.count { new DateTime(it) == diaHora } < clubLocal.triosDeCanchasDisponibles
	}

	def Boolean juegaAlgunoEseDia(Equipo local, Equipo visitante, DateTime fecha){
		def dia = fecha.toLocalDate() //Para comparar solo por yy/mm/dd, sin horas, minutos ni segundos
		if((local.fechas.find { new LocalDate(it.fechaDeJuego) == dia} != null) || (visitante.fechas.find { new LocalDate(it.fechaDeJuego) == dia } != null)){
			return true
		}else{
			return false
		}
	}

	def Boolean hayFechaEntreEquipos(Equipo local, Equipo visitante){
		return (local.fechas.find { it.equipoVisitante == visitante || it.equipoLocal == visitante} != null)
	}
	
}
