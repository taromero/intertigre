package intertigre.domain

import intertigre.services.FixtureService
import intertigre.util.DomainFactoryService

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime
import org.joda.time.LocalDate

import spock.lang.Ignore
import spock.lang.IgnoreRest;

class FixtureServiceSpec extends BaseIntegrationSpec{

	FixtureService fixtureService

	def 'generateFixture test'(){
		given: '16 equipos de una categoria, de 7 clubes distintos'
			def categoria = Categoria.build(nombre: 'libre', edadLimiteInferior: 5, edadLimiteSuperior: 1000, sexo: 'M')
			List<Equipo> equipos = domainFactoryService.crearXCantidadEquiposDeCategoriaDeXClubesDistintos(16, categoria, 7)
			List<Club> clubes = Club.findAll()
		and: 'un inicio, fin y horarios del torneo'
			LocalDate inicio = new DateTime(2012, 6, 2, 0,0,0).toLocalDate()
			LocalDate fin = new DateTime(2012, 7, 30, 0,0,0).toLocalDate()
			List<BigDecimal> horarios = [10, 12, 14, 16]
		and: 'un maximo de equipos por grupo'
			def cantMaximaEquiposPorGrupo = 6
		when: 'genero el fixture'
			def fixture = fixtureService.generarFixture(categoria, equipos, cantMaximaEquiposPorGrupo, inicio, fin, horarios)
		then: 'Deberian haber 3 grupos'
			fixture.grupos.size() == 3
		and: 'el primer grupo deberia tener 6 equipos, el segundo y el tercero 5'
			fixture.grupos.get(0).equipos.size() == 6
			fixture.grupos.get(1).equipos.size() == 5
			fixture.grupos.get(2).equipos.size() == 5
		and: 'el primer grupo deberia tener 15 fechas, el segundo y el tercero 10'
			fixture.grupos.get(0).fechas.size() == 15
			fixture.grupos.get(1).fechas.size() == 10
			fixture.grupos.get(2).fechas.size() == 10
		and: 'en el primer grupo deberian haber 3 equipos con 3 visitas y 3 con 2 visitas'
			fixture.grupos.get(0).equipos.count { it.getEquiposVisitantes().size() == 3} == 3
			fixture.grupos.get(0).equipos.count { it.getEquiposVisitantes().size() == 2} == 3
		and: 'el segundo y tercer grupo deberian tener 5 equipos con 2 visitas'
			fixture.grupos.get(1).equipos.count { it.getEquiposVisitantes().size() == 2 } == 5
			fixture.grupos.get(2).equipos.count { it.getEquiposVisitantes().size() == 2 } == 5
		and: 'no deberian haber 2 fechas para un equipo el mismo dia'
			!equipos.any { equipo -> equipo.fechas.any { fecha1 -> equipo.fechas.any { fecha2 -> fecha1 != fecha2 &&
																						 new LocalDate(fecha1.fechaDeJuego) == 
																						 new LocalDate(fecha2.fechaDeJuego)} 
																					  } }
		and: 'no deberia haber mas fechas al mismo tiempo que los trios de canchas que tiene disponible el club'
			def fechasAlMismoTiempoDeMasEnClub = false
			for(club in clubes){
				def fechasDeLocal = club.equipos*.fechas.flatten().findAll{ it.equipoLocal.club == club }
				for(fecha in fechasDeLocal){
					fechasAlMismoTiempoDeMasEnClub = fechasDeLocal.find { it != fecha && new DateTime(it.fechaDeJuego) == new DateTime(fecha.fechaDeJuego) } != null || fechasAlMismoTiempoDeMasEnClub
				}
			}
			!fechasAlMismoTiempoDeMasEnClub
		and: 'Los equipos del mismo club no deberian estar en el mismo grupo'
			def grupos = fixture.grupos
			def estanEnElMismoGrupo = false
			for(grupo in grupos){
				def equiposGrupo = grupo.equipos
				for(equipo in equiposGrupo){
					estanEnElMismoGrupo = equiposGrupo.find { (it != equipo && it.club == equipo.club) } != null || estanEnElMismoGrupo
				}
			}
			!estanEnElMismoGrupo
		and: 'No debe haber ningun equipo en mas de 1 grupo'
			def equipoEnDistintosGrupos = false
			for(grupo in grupos){
				def equiposGrupo = grupo.equipos
				for(equipo in equiposGrupo){
					equipoEnDistintosGrupos = grupos.find { it != grupo && it.equipos.contains(equipo) } != null || equipoEnDistintosGrupos 
				}
			}
			!equipoEnDistintosGrupos
		and: 'No debe haber ningun equipo repetido en el mismo grupo'
			def equipoRepetidoEnGrupo = false
			for(grupo in grupos){
				def equiposGrupo = grupo.equipos
				for(equipo in equiposGrupo){
					equipoRepetidoEnGrupo = equiposGrupo.count { it == equipo } > 1 || equipoRepetidoEnGrupo 
				}
			}
			!equipoRepetidoEnGrupo
	}

	def 'generateGrupos test'(){
		given: '16 equipos de una categoria, de 7 clubes distintos'
			def categoria = Categoria.build(nombre: 'libre', edadLimiteInferior: 5, edadLimiteSuperior: 1000, sexo: 'M')
			List<Equipo> equipos = domainFactoryService.crearXCantidadEquiposDeCategoriaDeXClubesDistintos(16, categoria, 7)
		and: 'una cantidad maxima de equipos por grupo = 6'
			def equiposPorGrupo = 6
		when: 'genero los grupos'
			def grupos = fixtureService.generateGrupos(equipos, 3, equiposPorGrupo, 1)
		then: 'deberia tener 3 grupos, nombrados alfabeticamente a partir de la a mayuscula'
			grupos.size() == 3
			grupos.get(0).nombre == 'A'
			grupos.get(1).nombre == 'B'
			grupos.get(2).nombre == 'C'
		and: 'el primer grupo deberia tener 6 equipos, y el segundo y el tercero 5'
			grupos.get(0).equipos.size() == 6
			grupos.get(1).equipos.size() == 5
			grupos.get(2).equipos.size() == 5
		and: 'Los equipos del mismo club no deberian estar en el mismo grupo'
			!grupos.any { grupo -> grupo.equipos.any { equipo -> grupo.equipos.any { it != equipo && it.club == equipo.club } } }
		and: 'No debe haber ningun equipo en mas de 1 grupo'
			!grupos.any { grupo -> grupo.equipos.any { equipo -> grupos.any { it != grupo && it.equipos.contains(equipo) } } }
		and: 'No debe haber ningun equipo repetido en el mismo grupo'
			!grupos.any { grupo -> grupo.equipos.any { equipo -> grupos.equipos.count { it == equipo } > 1 } }
	}

	def 'calcularCantidadDeGrupos test'(){
		given: 'una x cantidad de equipos'
			List<Equipo> equipos = domainFactoryService.crearXCantidadEquiposDeCategoriaDeXClubesDistintos(ce)
		and: 'una cantidad maxima de equipos por grupo'
			def cantMaximaEquiposPorGrupo = cantMaximaEquiposPorGrupoGrilla
		when: 'calculo la cantidad de grupos'
			def cantidadObtenida = fixtureService.calcularCantidadDeGrupos(equipos, cantMaximaEquiposPorGrupo)
		then: 'una cantidad y'
			cantidadObtenida == cantidadEsperada
		where:
			ce | cantidadEsperada | cantMaximaEquiposPorGrupoGrilla
			5  |        1         |            7
			7  |		1         |            7
			8  |		2         |            7
			16 |		3         |            7
			16 |        2         |            8
	}

	def 'calcularCantidadEquiposPoGrupo'(){
		given: 'una cantidad e de equipos, y una cantidad g de grupos'
			def cantEquipos = e
			def cantGrupos = g
		when: 'calculo la cantidad de equipos por grupo'
			def cantEquiposPorGrupoMap = fixtureService.calcularCantidadEquiposPorGrupo(cantEquipos, cantGrupos)
		then: 'espero'
			cantEquiposPorGrupoMap.cantidad == cantidadEsperada
			cantEquiposPorGrupoMap.cantidadGruposConMayor == cantidadGruposConMayorEsperada
		where:
			e  | g | cantidadEsperada      | cantidadGruposConMayorEsperada
			11 | 2 |			6		   |				1
			16 | 3 |			6		   |				1
			19 | 3 |			7		   |				1
			21 | 3 |		    7  		   |				-1
			23 | 4 |			6		   |				3
			24 | 4 |		    6  		   |				-1
			50 | 8 |			7		   |				2
			58 | 9 |			7		   |				4
	}

	def 'getDiasJugables test'(){
		given: 'una fecha de inicio y una de fin'
			LocalDate inicio = new DateTime(2012, 6, 2, 0,0,0).toLocalDate()
			LocalDate fin = new DateTime(2012, 6, 30, 0,0,0).toLocalDate()
		and: 'una lista de horarios'
			List<BigDecimal> horarios = [10, 12, 14, 16]
		when: 'pido los dias'
			List<DateTime> diasJugables = fixtureService.getDiasJugables(inicio, fin, horarios)
		then:
			diasJugables.size() == 20
	}

	def 'juegaAlgunoEseDia test caso verdadero'(){
		given: '2 equipos, 1 local y otros visitante'
			def local = new Equipo(id: 1)
			def visitante = new Equipo(id: 2)
			def fechaPreviamenteOrganizada = new Fecha(equipoLocal: local, equipoVisitante: visitante, fechaDeJuego: fpo)
			local.fechasLocal.add(fechaPreviamenteOrganizada)
			visitante.fechasVisitante.add(fechaPreviamenteOrganizada)
		and:' una fecha en la que se quiere jugar'
			def fechaNueva = fn
		when: 'se chequea si se puede'
			def juegaAlguno = fixtureService.juegaAlgunoEseDia(local, visitante, fechaNueva)
		then:
			juegaAlguno
		where:
		fpo             									|      fn
		new DateTime(2012, 6, 2, 10,0,0).toDate()			|	new DateTime(2012, 6, 2, 16,0,0)
		new DateTime(2012, 6, 9, 12,0,0).toDate()			|	new DateTime(2012, 6, 9, 12,0,0)
	}

	def 'juegaAlgunoEseDia test caso falso'(){
		given: '2 equipos, 1 local y otro visitante'
			def local = new Equipo(id: 1)
			def visitante = new Equipo(id: 2)
			def fechaPreviamenteOrganizada = new Fecha(equipoLocal: local, equipoVisitante: visitante, fechaDeJuego: fpo)
			local.fechasLocal.add(fechaPreviamenteOrganizada)
			visitante.fechasVisitante.add(fechaPreviamenteOrganizada)
		and:' una fecha en la que se quiere jugar'
			def fechaNueva = fn
		when: 'se chequea si se puede'
			def juegaAlguno = fixtureService.juegaAlgunoEseDia(local, visitante, fechaNueva)
		then:
			!juegaAlguno
		where:
		fpo             									|      fn
		new DateTime(2012, 6, 3, 10,0,0).toDate()			|	new DateTime(2012, 6, 2, 16,0,0)
		new DateTime(2012, 6, 10, 12,0,0).toDate()			|	new DateTime(2012, 6, 9, 12,0,0)
	}

	def 'hayFechaEntreEquipos test'(){
		given: '3 equipos'
			def e1 = new Equipo(); e1.id = 1;
			def e2 = new Equipo(); e2.id = 2;
			def e3 = new Equipo(); e3.id = 3;
		and: ' hay fecha entre e1 y e2 y entre e1 y e3'
			def fechaPreviamenteE1E2 = new Fecha(equipoLocal: e1, equipoVisitante: e2, fechaDeJuego: new DateTime(2012, 6, 3, 10,0,0).toDate())
			e1.fechasLocal.add(fechaPreviamenteE1E2)
			e2.fechasVisitante.add(fechaPreviamenteE1E2)
			def fechaPreviamenteE1E3 = new Fecha(equipoLocal: e1, equipoVisitante: e3, fechaDeJuego: new DateTime(2012, 6, 10, 10,0,0).toDate())
			e1.fechasLocal.add(fechaPreviamenteE1E3)
			e3.fechasVisitante.add(fechaPreviamenteE1E3)
		when: 'pregunto si hay una fecha entre ambos'
			def hayFechaEntreE1E2 = fixtureService.hayFechaEntreEquipos(e1, e2)
			def hayFechaEntreE1E3 = fixtureService.hayFechaEntreEquipos(e1, e3)
			def hayFechaEntreE2E3 = fixtureService.hayFechaEntreEquipos(e2, e3)
		then:
			hayFechaEntreE1E2
			hayFechaEntreE1E3
			!hayFechaEntreE2E3
	}

	def 'devuelve fechas disponibles de acuerdo a preferencias de horarios de los clubes'() {
		given: '2 equipos'
			def equipos = domainFactoryService.crearXCantidadEquiposDeCategoriaDeXClubesDistintos(2, Categoria.build(), 2)
			def e1 = equipos.get(0)
			def e2 = equipos.get(1)
			e1.club.horariosPreferidosParaLocal = horariosE1
			e2.club.horariosPreferidosParaLocal = horariosE2
		when: 'quiero obtener una fecha disponible para ambos'
			def fechasDisponibles = [new DateTime(2012, 6, 2, horariosDisponibles.get(0),0,0), new DateTime(2012, 6, 2, horariosDisponibles.get(1),0,0),
									 new DateTime(2012, 6, 2, horariosDisponibles.get(2),0,0), new DateTime(2012, 6, 2, horariosDisponibles.get(3),0,0)]
			def fechaDisponible = fixtureService.getDiaHorarioDisponibleParaAmbos(e1, e2, fechasDisponibles)
		then: 'obtengo el horario x'
			fechaDisponible.getHourOfDay() == horarioObtenido
		where:
			horariosE1    |    horariosE2    |   horariosDisponibles   |   horarioObtenido
			[20, 22, 10]  |   [22, 18, 20]   |   [12, 22, 20, 16]      |        20
			[22, 10, 3]   |   [3, 5, 23]     |   [20, 5, 3, 22]        |        22
			[]            |   [2, 5, 23]     |   [15, 20, 22, 14]      |        15
	}
}
