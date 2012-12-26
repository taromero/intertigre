package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

import java.lang.invoke.MethodHandleImpl.BindCaller.T

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
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El ultimo set debe tener un numero mayor de ' + 
															'games al principio que final (por ej. 6-3 y no 3-6)')
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(crearSingle('7-5', '6-4', '3-6')),
							 Doble.buildWithoutSave(crearDoble('7-5', '6-4', '3-6'))]
	}
	
	def 'un partido debe tener mas sets que ganados por la izquierda que por la derecha (6-3 y no 3-6)'() {
		given: 'un partido que tiene mas sets ganados por la derecha que por la izquierda'
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido tiene mal los datos de los sets.' +
																'Los games del ganador deben ir del lado izquierdo de cada set')
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(crearSingle('7-5', '4-6', '3-6')),
							 Single.buildWithoutSave(crearSingle('5-7', '4-6', '3-6')),
							 Single.buildWithoutSave(crearSingle('5-7', '4-6')),
							 Single.buildWithoutSave(crearSingle('5-7', '4-6', '6-3')),
							 Single.buildWithoutSave(crearSingle('5-7', '6-4', '3-6')),
							 Doble.buildWithoutSave(crearDoble('7-5', '4-6', '3-6')),
							 Doble.buildWithoutSave(crearDoble('5-7', '4-6', '3-6')),
							 Doble.buildWithoutSave(crearDoble('5-7', '4-6')),
							 Doble.buildWithoutSave(crearDoble('5-7', '4-6', '6-3')),
							 Doble.buildWithoutSave(crearDoble('5-7', '6-4', '3-6'))]
	}
	
	def 'un partido con mas sets ganados por izquierda que por derecha y con el ultimo set ganado por la izquierda se debe poder guardar'() {
		given: 'un partido con mas sets ganados por izquierda que por derecha y con el ultimo set ganado por la izquierda'
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'se deberia guardar'
			Partido.findAll().size() > 0
			Partido.get(partido.id) != null
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(crearSingle('7-5', '3-6', '6-4')),
							 Single.buildWithoutSave(crearSingle('5-7', '6-3', '6-4')),
							 Single.buildWithoutSave(crearSingle('7-6', '6-3')),
							 Doble.buildWithoutSave(crearDoble('7-5', '3-6', '6-4')),
							 Doble.buildWithoutSave(crearDoble('5-7', '6-3', '6-4')),
							 Doble.buildWithoutSave(crearDoble('7-5', '6-3'))]
	}

	def 'un partido abandonado no puede tener un resultado normal'() {
		given: 'un partido abandonado con resultado de un partido normal'
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El partido esta marcado como abandono, pero ' +
															'tiene todos los sets terminados')
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(crearSingle('7-5', '6-3') + [abandono: true])]
	}

	def 'un partido abandonado puede tener un resultado anormal'() {
		given: 'un partido abandonado con resultado anormal'
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'se deberia guardar'
			Partido.findAll().size() > 0
			Partido.get(partido.id) != null
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(crearSingle('7-5', '3-6') + [abandono: true])]
	}
	
	private Map crearSingle(primerSet, segundoSet, tercerSet = null){
		def ps = primerSet.tokenize('-').toArray()
		def ss = segundoSet.tokenize('-').toArray()
		def ts = tercerSet != null ? tercerSet.tokenize('-').toArray() : ['null', 'null']
		return [primerSet: [gamesGanador: ps[0], gamesPerdedor: ps[1]],
				  segundoSet: [gamesGanador: ss[0], gamesPerdedor: ss[1]],
				  tercerSet: [gamesGanador: ts[0], gamesPerdedor: ts[1]],
				  equipoGanador: equipoL,
				  fecha: fecha,
				  jugadorLocal: jugador1, jugadorVisitante: jugador2]
	}
	
	private Map crearDoble(primerSet, segundoSet, tercerSet = null){
		pareja1.save()
		pareja2.save()
		return crearSingle(primerSet, segundoSet, tercerSet)
					.plus([parejaLocal: pareja1, parejaVisitante: pareja2])
					.minus([jugadorLocal: jugador1, jugadorVisitante: jugador2])
	}
	
}
