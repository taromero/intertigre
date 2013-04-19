package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime

import extension.custom.Report

@Report
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
	
	def 'un equipo no puede tener 2 fechas de local el mismo dia'() {
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
	
	def 'un equipo no puede tener 2 fechas de visitante el mismo dia'() {
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
	
	def 'un equipo no puede tener una fecha de local y otra de visitante el mismo dia'() {
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

	def 'un equipo puede tener 2 fechas de distinto dia'() {
		given: 'una fecha de un equipo'
			createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
		when: 'quiero guardar otra fecha para otro dia'
			createFecha(equipoNahuel, equipoCanotto, fechaDeJuegoParaOtroDia)
		then: 'Se deberian guardar las 2 fechas'
			Fecha.count() == 2
		where:
			fechaDeJuegoParaOtroDia << [new DateTime(fechaDeJuego).minusDays(1).toDate(), new DateTime(fechaDeJuego).plusDays(2).toDate()]
	}

	def 'un jugador no puede estar en mas de un partido de la misma fecha'() {
		given: 'una fecha con un mismo jugador en mas de un partido'
			def fecha = createFecha(equipoCanotto, equipoChasqui, fechaDeJuego)
			fecha.single1 = new Single(jugadorLocal: equipoCanotto.jugadores.first(), 
											jugadorVisitante: equipoChasqui.jugadores.first(),
											primerSet: Sett.build(), segundoSet: Sett.build(), 
											fecha: fecha, equipoGanador: equipoCanotto).save(failOnError: true)
			fecha.single2 = new Single(jugadorLocal: equipoCanotto.jugadores.first(), 
											jugadorVisitante: equipoChasqui.jugadores.get(2),
											primerSet: Sett.build(), segundoSet: Sett.build(),
											fecha: fecha, equipoGanador: equipoChasqui).save(failOnError: true)
		when: 'quiero guardar la fecha'
			fecha.save(failOnError: true)
		then: 'no me lo deberia permitir'
			Exception ex = thrown(UnsupportedOperationException)
			ex.message == "hay un jugador que figura en mas de un partido para esta serie"
	}
	
}
