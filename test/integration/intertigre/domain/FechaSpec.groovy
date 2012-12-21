package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

import org.joda.time.DateTime

import spock.lang.Unroll

class FechaSpec extends BaseIntegrationSpec{

	Equipo equipoCanotto
	Equipo equipoChasqui
	Equipo equipoNahuel
	static Date fechaDeJuego = new DateTime(2012, 11, 12, 18, 0, 0).toDate()
	
	def setup() {
		equipoCanotto = domainFactoryService.crearEquipoMas19MCanotto()
		equipoChasqui = domainFactoryService.crearEquipoMas19MElChasqui()
		equipoNahuel = domainFactoryService.crearEquipoNahuel()
	}
	
	def 'no deberia dejar guardar 2 fechas de local el mismo dia para un equipo'() {
		given: 'una fecha de un equipo'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'quiero guardar otra fecha para el mismo dia en otro horario'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuegoParaElMismoDia)
		then: 'deberia fallar'
			Exception ex = thrown(UnsupportedOperationException)
			ex.message == "El equipo '$equipoCanotto' ya tiene una fecha para ese dia"
		where:
			fechaDeJuegoParaElMismoDia << [fechaDeJuego, new DateTime(fechaDeJuego).plusHours(2).toDate()]
	}
	
	def 'no deberia dejar guardar 2 fechas de visitante el mismo dia para un equipo'() {
		given: 'una fecha de un equipo'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'quiero guardar otra fecha para el mismo dia'
			createFecha(equipoNahuel, equipoChasqui, fechaDeJuegoParaElMismoDia)
		then: 'deberia fallar'
			Exception ex = thrown(UnsupportedOperationException)
			ex.message == "El equipo '$equipoChasqui' ya tiene una fecha para ese dia"
		where:
			fechaDeJuegoParaElMismoDia << [fechaDeJuego, new DateTime(fechaDeJuego).plusHours(2).toDate()]
	}
	
	def 'no deberia dejar guardar 1 fecha de local y otra de visitante el mismo dia para un equipo'() {
		given: 'una fecha de un equipo'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'quiero guardar otra fecha para el mismo dia'
			createFecha(equipoNahuel, equipoCanotto, fechaDeJuegoParaElMismoDia)
		then: 'deberia fallar'
			Exception ex = thrown(UnsupportedOperationException)
			ex.message == "El equipo '$equipoCanotto' ya tiene una fecha para ese dia"
		where:
			fechaDeJuegoParaElMismoDia << [fechaDeJuego, new DateTime(fechaDeJuego).plusHours(2).toDate()]
	}

	def 'deberia dejar guardar 2 fechas de distinto dia para un equipo'() {
		given: 'una fecha de un equipo'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'quiero guardar otra fecha para otro dia'
			createFecha(equipoNahuel, equipoCanotto, fechaDeJuegoParaOtroDia)
		then: 'Se deberian guardar las 2 fechas'
			Fecha.count() == 2
		where:
			fechaDeJuegoParaOtroDia << [new DateTime(fechaDeJuego).minusDays(1).toDate(), new DateTime(fechaDeJuego).plusDays(2).toDate()]
	}
	
}
