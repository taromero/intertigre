package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import spock.lang.IgnoreRest;

class PartidoSpec extends BaseIntegrationSpec{

	static Equipo equipoL = Equipo.build()
	static Equipo equipoV = Equipo.build()
	static Fecha fecha = createFecha(equipoL, equipoV, new Date())
	
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
			singleODoble << [Single.buildWithoutSave(crearPartido('7-5', '6-4', '3-6')),
							 Doble.buildWithoutSave(crearPartido('7-5', '6-4', '3-6'))]
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
			singleODoble << [
				Single.buildWithoutSave(crearPartido('7-5', '4-6', '3-6')),
							 Single.buildWithoutSave(crearPartido('5-7', '4-6', '3-6')),
							 Single.buildWithoutSave(crearPartido('5-7', '4-6')),
							 Single.buildWithoutSave(crearPartido('5-7', '4-6', '6-3')),
							 Single.buildWithoutSave(crearPartido('5-7', '6-4', '3-6')),
							 Doble.buildWithoutSave(crearPartido('7-5', '4-6', '3-6')),
							 Doble.buildWithoutSave(crearPartido('5-7', '4-6', '3-6')),
							 Doble.buildWithoutSave(crearPartido('5-7', '4-6')),
							 Doble.buildWithoutSave(crearPartido('5-7', '4-6', '6-3')),
							 Doble.buildWithoutSave(crearPartido('5-7', '6-4', '3-6'))
							 ]
	}

	private Map crearPartido(primerSet, segundoSet, tercerSet = null){
		def ps = primerSet.tokenize('-').toArray()
		def ss = segundoSet.tokenize('-').toArray()
		def ts = tercerSet != null ? tercerSet.tokenize('-').toArray() : ['null', 'null']
		return [primerSet: [gamesGanador: ps[0], gamesPerdedor: ps[1]],
				  segundoSet: [gamesGanador: ss[0], gamesPerdedor: ss[1]],
				  tercerSet: [gamesGanador: ts[0], gamesPerdedor: ts[1]],
				  equipoGanador: equipoL,
				  fecha: fecha]
	}
	
}
