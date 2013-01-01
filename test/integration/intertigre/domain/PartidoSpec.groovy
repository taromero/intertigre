package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import spock.lang.IgnoreRest
import extension.custom.Report

@Report
class PartidoSpec extends BaseIntegrationSpec{

	static Equipo equipoL
	static Equipo equipoV
	static Jugador jugador1
	static Jugador jugador2
	static ParejaDoble pareja1
	static ParejaDoble pareja2
	static Fecha fecha
	
	def setupSpec() {
		equipoL = Equipo.build()
		equipoV = Equipo.build()
		jugador1 = Jugador.build()
		jugador2 = Jugador.build()
		pareja1 = new ParejaDoble(doblista1: jugador1, doblista2: jugador2)
		pareja2 = new ParejaDoble(doblista1: jugador1, doblista2: jugador2)
		fecha = createFecha(equipoL, equipoV, new Date())
	}
	
	def cleanupSpec() {
		Equipo.executeUpdate("delete Fecha")
		Equipo.executeUpdate("delete Equipo")
		Equipo.executeUpdate("delete ParejaDoble")
		Equipo.executeUpdate("delete SecUser")
	}
	
	def 'el ultimo set de un partido debe tener un numero de games mas grande al final que al principio (6-3 y no 3-6)'() {
		given: 'un partido donde el ganador pierde el ultimo set'
			def partido = crearSingleFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El ultimo set debe tener un numero mayor de ' + 
															'games al principio que final (por ej. 6-3 y no 3-6)')
		where: ''
			sets << ['7-5 6-4 3-6']
	}
	
	def 'un partido debe tener mas sets que ganados por la izquierda que por la derecha (6-3 y no 3-6)'() {
		given: 'un partido que tiene mas sets ganados por la derecha que por la izquierda'
			def partido = crearSingleFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido tiene mal los datos de los sets.' +
																'Los games del ganador deben ir del lado izquierdo de cada set')
		where: ''
			    sets << ['7-5 4-6 3-6', '5-7 4-6 3-6', '5-7 4-6', '5-7 4-6 6-3', '4-7 6-4 3-6']
	}
	
	def 'un partido con mas sets ganados por izquierda que por derecha y con el ultimo set ganado por la izquierda se debe poder guardar'() {
		given: 'un partido con mas sets ganados por izquierda que por derecha y con el ultimo set ganado por la izquierda'
			def partido = crearSingleFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'se deberia guardar'
			Partido.findAll().size() > 0
			Partido.get(partido.id) != null
		where: ''
			sets << ['7-5 3-6 6-4', '5-7 6-3 6-4', '7-6 6-3']
	}

	def 'un partido abandonado no puede tener un resultado normal'() {
		given: 'un partido abandonado con resultado de un partido normal'
			def partido = crearSingleAbandonadoFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido esta marcado como abandono, pero ' +
															'tiene un resultado normal')
		where: ''
			sets << ['7-5 6-3', '7-5 3-6 7-6', '5-7 6-3 6-1', '7-5 6-3']
	}

	def 'un partido abandonado no puede tener los 3 sets completos'(){
		given: 'un partido abandonado con los 3 sets completos'
			def partido = crearSingleAbandonadoFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido esta marcado como abandono, pero ' +
															'tiene todos los sets terminados')
		where: ''
			sets << ['7-5 3-6 3-6']
	}

	def 'un partido abandonado puede tener un resultado anormal'() {
		given: 'un partido abandonado con resultado anormal'
			def partido = crearSingleAbandonadoFromString(sets)
		when: 'guardo el partido'
			partido.save()
		then: 'se deberia guardar'
			Partido.findAll().size() > 0
			Partido.get(partido.id) != null
		where: ''
			sets << ['7-5 3-6', '7-5', '2-1']
	}
	
	def 'un partido abandonado no puede tener games en un set si el set anterior no fue completado'() {
		given: 'un partido abandonado con games en el segundo o tercer set, sin tener el primero terminado'
			def partido = crearSingleAbandonadoFromString(sets)
		when: 'guardo'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido tiene games para un set cuando el set anterior no esta terminado')
		where: ''
			sets << ['2-1 6-4', '2-1 3-2 6-7', '6-4 4-5 7-5', '2-1 2-3']
	}

	def 'un partido no puede tener sets sin terminar con games'() {
		given: 'un partido con sets sin terminar (por ej. 5-4)'
			def partido = crearSingleFromString(sets)
		when: 'guardo'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('Falta completar el resultado en alguno de los sets')
		where: ''
			sets << ['7-5 5-3', '7-5 6-3 3-4', '2-1 5-3',
					 '2-1 6-3 7-6', '2-1 3-2 3-5', '2-1 3-2 5-3']
	}
	
	private Map crearSingle(primerSet, segundoSet = null, tercerSet = null){
		def ps = primerSet != null ? primerSet.tokenize('-').toArray() : null
		def ss = segundoSet != null ? segundoSet.tokenize('-').toArray() : null
		def ts = tercerSet != null ? tercerSet.tokenize('-').toArray() : null
		return [primerSet: [gamesGanador: ps[0], gamesPerdedor: ps[1]],
				  segundoSet: ss != null ? [gamesGanador: ss[0], gamesPerdedor: ss[1]] : null,
				  tercerSet: ts != null ? [gamesGanador: ts[0], gamesPerdedor: ts[1]] : null,
				  equipoGanador: equipoL,
				  fecha: fecha,
				  jugadorLocal: jugador1, jugadorVisitante: jugador2,
				  abandono: false]
	}
	
	private Map crearDoble(primerSet, segundoSet = null, tercerSet = null){
		pareja1.save()
		pareja2.save()
		return crearSingle(primerSet, segundoSet, tercerSet)
					.plus([parejaLocal: pareja1, parejaVisitante: pareja2])
					.minus([jugadorLocal: jugador1, jugadorVisitante: jugador2])
	}
	
	private Single crearSingleFromString(setsString){
		def sets = setsString.tokenize(' ')
		def primerSet = sets[0]
		def segundoSet = sets.size() >= 2 ?  sets[1] : null
		def tercerSet = sets.size() >= 3 ? sets[2] : null
		return Single.buildWithoutSave(crearSingle(primerSet, segundoSet, tercerSet))
	}

	private Single crearSingleAbandonadoFromString(setsString){
		def single = crearSingleFromString(setsString)
		single.abandono = true
		return single
	}
}
