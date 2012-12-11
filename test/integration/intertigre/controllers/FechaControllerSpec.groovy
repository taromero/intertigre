package intertigre.controllers

import intertigre.domain.Categoria
import intertigre.domain.Equipo
import intertigre.domain.Fecha
import intertigre.domain.FechaController

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime
import org.joda.time.LocalDate

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
			def fecha = new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui, 
				fechaDeJuego: new Date(), fechaSubidaResultado: new Date(), categoria: Categoria.build())
			equipoCanotto.fechasLocal.add(fecha)
			equipoChasqui.fechasVisitante.add(fecha)
			equipoCanotto.save()
			equipoChasqui.save()
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()
		when: 'creo partidos para la fecha'
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
			Fecha fecha = new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
								fechaDeJuego: new Date(), fechaSubidaResultado: new Date(), categoria: Categoria.build())
			equipoCanotto.fechasLocal.add(fecha)
			equipoChasqui.fechasVisitante.add(fecha)
			equipoCanotto.save()
			equipoChasqui.save()
			loggedUser = equipoCanotto.jugadores.find { it.email == 'canotto90@gmail.com' }
			
			def idsJresCanotto = equipoCanotto.jugadores*.id.toArray()
			def idsJresChasqui = equipoChasqui.jugadores*.id.toArray()
		when: 'creo partidos para la fecha con datos incorrectos para los games'
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
			Fecha fecha = new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
								fechaDeJuego: fechaDeJuego, fechaSubidaResultado: new Date(), categoria: Categoria.build())
			Fecha fechaPrevia = new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
								fechaDeJuego: new DateTime().minusWeeks(1).toDate(), fechaSubidaResultado: new Date(), categoria: Categoria.build())
			Fecha fechaPosterior = new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
								fechaDeJuego: fechaDeJuegoPosterior, fechaSubidaResultado: new Date(), categoria: Categoria.build())
			equipoCanotto.fechasLocal = [fecha, fechaPrevia, fechaPosterior]
			equipoChasqui.fechasVisitante = [fecha, fechaPrevia, fechaPosterior]
			equipoCanotto.save()
			equipoChasqui.save()
		when: 'pido reprogramar la fecha la fecha'
			controller.params.id = fecha.id
			controller.pedirReprogramacionFecha()
		then: 'la fecha debe quedar en estado de pedido de cambio de fecha'
			fecha.pedidoCambioDeFecha == true
		and: 'la fecha de reprogramacion debe ser la primera disponible, dejando pasar por lo menos una semana'
			new LocalDate(fecha.fechaReprogramacion) == diaDeReprogramacionEsperado
		and: 'respetando en lo posible los horarios disponibles del club local'
			fecha.equipoLocal.club.horariosPreferidosParaLocal.contains(new DateTime(fecha.fechaReprogramacion).hourOfDay)
		where:
			fechaDeJuegoPosterior         		   | diaDeReprogramacionEsperado
		     new DateTime().plusWeeks(1).toDate()  |  new LocalDate().plusWeeks(2)
			 new DateTime().plusWeeks(2).toDate()  |  new LocalDate().plusWeeks(1)
			 new DateTime().plusWeeks(3).toDate()  |  new LocalDate().plusWeeks(1)
			
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
