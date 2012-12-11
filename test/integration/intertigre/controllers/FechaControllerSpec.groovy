package intertigre.controllers

import intertigre.domain.Categoria
import intertigre.domain.Equipo
import intertigre.domain.Fecha
import intertigre.domain.FechaController

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime
import org.joda.time.LocalDate

import spock.lang.IgnoreRest;
import static intertigre.util.DomainFactoryService.createFecha

class FechaControllerSpec extends BaseControllerSpec{

	FechaController controller = new FechaController()
	
	Equipo equipoCanotto
	Equipo equipoChasqui
	
	def setup() {
		equipoCanotto = domainFactoryService.crearEquipoMas19MCanotto()
		equipoChasqui = domainFactoryService.crearEquipoMas19MElChasqui()
	}

	def 'crear partidos para una fecha'(){
		given: 'un usuario loggeado que pertenece a alguno de los equipos de la fecha'
			def fecha = createFecha(equipoCanotto, equipoChasqui, new Date()) 
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
		when: 'creo partidos para la fecha'
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()

			controller.params.id = fecha.id
			controller.params.wo = false
			controller.params.single1 = crearPartido(idsJresCanotto[0], idsJresChasqui[0], '7-5', '3-6', '6-2', equipoCanotto.id)
			controller.params.single2 = crearPartido(idsJresCanotto[1], idsJresChasqui[1], '6-4', '7-6', null, equipoCanotto.id)
			controller.params.doble = crearPartido([idsJresCanotto[3], idsJresCanotto[2]], [idsJresChasqui[3], idsJresChasqui[2]],
																				'6-3,', '7-5', null, equipoChasqui.id)
			controller.savePartidos()
		then: 'se debe crear correctamente y mostrarse por pantalla la fecha con los resultados actualizados'
			controller.response.redirectedUrl == '/fecha/show/' + fecha.id
			fecha.single1.primerSet.gamesGanador == 7
	}

	def 'crear partidos para una fecha con datos incorrectos para los games'(){
		given: 'un usuario loggeado que pertenece a alguno de los equipos de la fecha'
			Fecha fecha = createFecha(equipoCanotto, equipoChasqui, new Date())
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
		when: 'creo partidos para la fecha con datos incorrectos para los games'
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()

			controller.params.id = fecha.id
			controller.params.wo = false
			controller.params.single1 = crearPartido(idsJresCanotto[0], idsJresChasqui[0], s1ps, s1ss, s1ts, equipoCanotto.id)
			controller.params.single2 = crearPartido(idsJresCanotto[1], idsJresChasqui[1], '6-4', '7-6', null, equipoCanotto.id)
			controller.params.doble = crearPartido([idsJresCanotto[2], idsJresCanotto[3]], [idsJresChasqui[2], idsJresChasqui[3]],
																				'6-3,', '7-5', null, equipoChasqui.id)
			controller.savePartidos()
			fecha = Fecha.first()
			def a = renderMap.model.fecha.errors
		then: 'se debe volver a la pagina de creacion de partidos indicando el error'
			renderMap.view == 'createPartido'
			renderMap.model.fecha.errors.getAt('single1.primerSet.gamesGanador').code == 'range.toobig'
			renderMap.model.fecha.errors.allErrors.size() == 1
//			No puedo probar que se haga el roolback por ahora			
//			fecha.single1 == null
//			fecha.single2 == null
//			fecha.doble == null
		where:
			  s1ps  | s1ss | s1ts
			 '123-4'  | '3-6'| '6-2'
	}

	def 'pedir reprogramacion de fecha'() {
		given: '3 fechas entre 2 equipos, para hoy, una antes y otra despues'
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			def fechaDeJuego = new Date()
			Fecha fecha = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
			Fecha fechaPrevia = createFecha(equipoCanotto, equipoChasqui, new DateTime().minusWeeks(1).toDate())
			Fecha fechaPosterior = createFecha(equipoCanotto, equipoChasqui, fechaDeJuegoPosterior)
		when: 'pido reprogramar la fecha la fecha'
			controller.params.id = fecha.id
			controller.pedirReprogramacionFecha()
		then: 'la fecha de reprogramacion debe ser la primera disponible, dejando pasar por lo menos una semana'
			new LocalDate(fecha.fechaReprogramacion) == diaDeReprogramacionEsperado
		and: 'respetando en lo posible los horarios disponibles del club local'
			fecha.equipoLocal.club.horariosPreferidosParaLocal.contains(new DateTime(fecha.fechaReprogramacion).hourOfDay)
		where:
			fechaDeJuegoPosterior         		   | diaDeReprogramacionEsperado
		     new DateTime().plusWeeks(1).toDate()  |  new LocalDate().plusWeeks(2)
			 new DateTime().plusWeeks(2).toDate()  |  new LocalDate().plusWeeks(1)
			 new DateTime().plusWeeks(3).toDate()  |  new LocalDate().plusWeeks(1)
	}
	
	def 'aceptar reprogramacion de fecha'() {
		given: '1 fecha con pedido de reprogramacion'
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			def fechaDeJuego = new Date()
			def fechaDeReprogramacion = new DateTime(fechaDeJuego).plusWeeks(1).toDate()
			Fecha fecha = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego, fechaDeReprogramacion)
		when: 'el administrador acepta la reprogramacion'
			controller.params.id = fecha.id
			controller.aceptarReprogramacionFecha()
			fecha = Fecha.get(fecha.id)
		then: 'la fecha de reprogramacion pasa a ser la fecha de juego'
			fecha.fechaDeJuego == fechaDeReprogramacion
		and: 'la fecha de reprogramacion debe quedar nula'
			fecha.fechaReprogramacion == null
	}
	
//	def 'ver todas las fechas con pedido de reprogramacion'() {
//		given: 'fechas con y sin pedido de reprogramacion'
//			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
//			def fechaDeJuego = new Date()
//			def fechaDeReprogramacion = new DateTime(fechaDeJuego).plusWeeks(1).toDate()
//			Fecha fechaR1 = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego, fechaDeReprogramacion)
//			Fecha fechaR1 = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego, fechaDeReprogramacion)
//		when: 'pido ver las fechas con pedido de reprogramacion'
//		then: 'solo deberia mostrarme las que tienen pedido de reprogramacion'
//	}
	
//	def 'aceptar reprogramacion masiva de fechas'() {
//		given: 'x fechas con pedido de reprogramacion'
//		when: 'el administrador acepta las reprogramaciones'
//		then: 'las fechas de reprogramacion pasan a ser las fechas de juego'
//	}
	
	def 'rechazar la reprogramacion de la fecha'() {
		given: '1 fecha con pedido de reprogramacion'
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			def fechaDeJuego = new Date()
			Fecha fecha = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'el administrador rechaza la reprogramacion'
			controller.params.id = fecha.id
			controller.rechazarReprogramacionFecha()
			fecha = Fecha.get(fecha.id)
		then: 'la fecha de juego debe seguir siendo la misma'
			fecha.fechaDeJuego == fechaDeJuego
		and: 'la fecha de reprogramacion debe quedar nula'
			fecha.fechaReprogramacion == null
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
