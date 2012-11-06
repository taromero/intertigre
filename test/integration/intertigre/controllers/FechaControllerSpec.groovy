package intertigre.controllers

import intertigre.domain.Equipo
import intertigre.domain.Fecha
import intertigre.domain.FechaController
import spock.lang.Ignore

class FechaControllerSpec extends BaseControllerSpec{

	FechaController controller = new FechaController()
	
	//tengo que lograr que se me cree la fecha con equipo_visitante_id != null
	@Ignore()
	def 'crear partidos para una fecha'(){
		given: 'un usuario loggeado que pertenece a alguno de los equipos de la fecha'
			def canotto = df.crearClubCanotto()
			def elChasqui = df.crearClubElChasqui()
			Equipo equipoCanotto = df.crearEquipoCanotto()
			Equipo equipoChasqui = df.crearEquipoElChasqui()
			def fecha = Fecha.build(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui, fechaDeJuego: new Date())
			fecha.save(flush: true, failOnError: true)
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()
		when: 'creo partidos para la fecha'
			controller.params.id = fecha.id
			controller.params.single1 = crearPartido(idsJresCanotto[0], idsJresChasqui[0], '7-5', '3-6', '6-2', canotto.id)
			controller.params.single2 = crearPartido(idsJresCanotto[1], idsJresChasqui[1], '6-4', '7-6', null, canotto.id)
			controller.params.doble = crearPartido([idsJresCanotto[3], idsJresCanotto[2]], [idsJresChasqui[3], idsJresChasqui[2]],
																				'6-3,', '7-5', null, elChasqui.id)
			controller.savePartidos()
		then: 'se debe crear correctamente y mostrarse por pantalla la fecha con los resultados actualizados'
			controller.response.redirectedUrl == '/fecha/show/' + fecha.id
			fecha.single1.primerSet.gamesGanador == 7
	}

	//tengo que lograr que se me cree la fecha con equipo_visitante_id != null
	@Ignore()
	def 'crear partidos para una fecha con datos incorrectos para los games'(){
		given: 'un usuario loggeado que pertenece a alguno de los equipos de la fecha'
			def canotto = df.crearClubCanotto()
			def elChasqui = df.crearClubElChasqui()
			def equipoCanotto = df.crearEquipoCanotto()
			def equipoChasqui = df.crearEquipoElChasqui()
			Fecha fecha = Fecha.build(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
								fechaDeJuego: new Date())
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()
		when: 'creo partidos para la fecha con datos incorrectos para los games'
			controller.params.id = fecha.id
			controller.params.single1 = crearPartido(idsJresCanotto[0], idsJresChasqui[0], s1ps, s1ss, s1ts, canotto.id)
			controller.params.single2 = crearPartido(idsJresCanotto[1], idsJresChasqui[1], '6-4', '7-6', null, canotto.id)
			controller.params.doble = crearPartido([idsJresCanotto[2], idsJresCanotto[3]], [idsJresChasqui[2], idsJresChasqui[3]],
																				'6-3,', '7-5', null, elChasqui.id)
			controller.savePartidos()
			fecha = Fecha.first()
		then:
			renderMap.view == 'createPartido'

//			No puedo probar que se haga el roolback por ahora			
//			fecha.single1 == null
//			fecha.single2 == null
//			fecha.doble == null
		where:
			  s1ps  | s1ss | s1ts
			 '123-4'  | '3-6'| '6-2'
	}
	
	private Map crearPartido(jugadorLocalId, jugadorVisitanteId, primerSet, segundoSet, tercerSet, equipoGanadorId){
		def ps = primerSet.tokenize('-').toArray()
		def ss = segundoSet.tokenize('-').toArray()
		def ts = tercerSet != null ? tercerSet.tokenize('-').toArray() : ['null', 'null']
		if(!jugadorLocalId.respondsTo('size')){ // Me fijo si es un single o un doble
			return [primerSet: [gamesGanador: ps[0], gamesPerdedor: ps[1]],
					  segundoSet: [gamesGanador: 3, gamesPerdedor: 6],
					  tercerSet: [gamesGanador: 6, gamesPerdedor: 2],
					  equipoGanador: [id: equipoGanadorId],
					  jugadorLocal: [id: jugadorLocalId],
					  jugadorVisitante: [id: jugadorVisitanteId]]
		}else{
			return [primerSet: [gamesGanador: ps[0], gamesPerdedor: ps[1]],
				segundoSet: [gamesGanador: 3, gamesPerdedor: 6],
				tercerSet: [gamesGanador: 6, gamesPerdedor: 2],
				equipoGanador: [id: equipoGanadorId],
				parejaLocal: [doblista1: [id: jugadorLocalId[0]], doblista2: [id: jugadorLocalId[1]]],
				parejaVisitante: [doblista1: [id: jugadorVisitanteId[0]], doblista2: [id: jugadorVisitanteId[1]]]]
		}
	}
}
