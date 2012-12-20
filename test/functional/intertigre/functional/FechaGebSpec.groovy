package intertigre.functional

import static intertigre.util.DomainFactoryService.createFechasParaReprogramar
import intertigre.domain.Fecha
import intertigre.domain.Jugador
import intertigre.functional.pages.FechasListPage
import intertigre.functional.pages.HomePage
import intertigre.functional.pages.ReprogramarFechasPage
import intertigre.security.SecUserSecRole
import spock.lang.Ignore

class FechaGebSpec extends BaseControllerGebSpec{

	def 'un usuario administrador quiere ver la lista de fechas a reprogramar'() {
		given: 'un usuario administrador logeado'
			Jugador usuarioAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioAdmin, roleAdmin).save()
			logearse(usuarioAdmin.email, passwordDefault)
		and: 'una serie de fechas a reprogramar'
			createFechasParaReprogramar(new Date(), 10)
		when: 'voy a ver las fechas a reprogramar'
			to ReprogramarFechasPage
			at ReprogramarFechasPage
		then: 'deberia poder ver todas las fechas'
			fechasField.size() == 10
			fechasField.each { it.displayed == true }
		and: 'con un checkbox en cada una para poder marcalas como resueltas'
			checkboxes.size() == fechasField.size()
			checkboxes.each { it.displayed == true }
		and: 'y un checkbox para marcar todas como resueltas'
			checkearTodosCheck.displayed == true
	}
	
	@Ignore
	def 'un usuario NO administrador quiere ver la lista de fechas a reprogramar'() {
		given: 'un usuario NO administrador logeado'
			Jugador usuarioNoAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioNoAdmin, roleCapitanClub).save()
			logearse(usuarioNoAdmin.email, passwordDefault)
		and: 'una serie de fechas a reprogramar'
			createFechasParaReprogramar(new Date(), 10)
		when: 'voy a ver las fechas a reprogramar'
			to ReprogramarFechasPage
		then: 'me deberia redireccionar al home'
			at HomePage
		and: 'indicando que no tengo permisos para ver esto'
			$('.message').text() == 'Solo podes editar tus datos'
	}
	
	def 'un usuario administrador quiere reprogramar todas las fechas'() {
		given: 'un usuario administrador logeado'
			Jugador usuarioAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioAdmin, roleAdmin).save()
			logearse(usuarioAdmin.email, passwordDefault)
		and: 'una serie de fechas a reprogramar'
			def fechasAReprogramar = createFechasParaReprogramar(new Date(), 50)
		when: 'voy a ver las fechas a reprogramar'
			to ReprogramarFechasPage
			at ReprogramarFechasPage
		and: 'toco el boton de reprogramar todas'
			checkearTodosCheck.click()
			reprogramarButton.click()
		then: 'me deberia redirigir a una pagina mostrandome las nuevas fechas de juego para cada fecha'
			at FechasListPage
		and: 'mostrandome las nuevas fechas de juego para cada fecha seleccionada'
			for(fecha in fechasAReprogramar) {
				$("#fechaDeJuego" + fecha.id).text() == fecha.fechaDeJuego
			}
//		when: 'voy a ver el detalle de alguna de las fechas'
//			
//		then: 'me muestra un texto indicando que la fecha fue reprogramada'
	}
	
	def 'un usuario administrador quiere reprogramar una serie de fechas'() {
		given: 'un usuario administrador logeado'
			Jugador usuarioAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioAdmin, roleAdmin).save()
			logearse(usuarioAdmin.email, passwordDefault)
		and: 'una serie de fechas a reprogramar'
			createFechasParaReprogramar(new Date(), 10)
		when: 'voy a ver las fechas a reprogramar'
			to ReprogramarFechasPage
			at ReprogramarFechasPage
		and: 'selecciono los checkbox de las fechas que quiero reprogramar'
			def fechasAReprogramar = Fecha.findAll { fechaReprogramacion != null }[1..3]
			for(fecha in fechasAReprogramar) {
				def selector = "#id" + fecha.id
				def fechaCheck = $(selector)
				fechaCheck.value(true)
			}
		and: 'toco el boton de reprogramar algunas'
			reprogramarButton.click()
		then: 'me deberia redirigir a la pagina de listado de fechas'
			at FechasListPage
		and: 'mostrandome las nuevas fechas de juego para cada fecha seleccionada'
			for(fecha in fechasAReprogramar) {
				$("#fechaDeJuego" + fecha.id).text() == fecha.fechaDeJuego
			}
	}
	
	def 'un capitan de equipo quiere reprogramar una fecha'() {
		given: 'un capitan de equipo loggeado'
		and: 'una serie de fechas para el equipo del capitan'
		when: 'va a ver el detalle de una fecha'
		and: 'toca el boton de reprogramar'
		then: 'deberia volverse a la pagina de detalle'
		and: 'mostrando la fecha de reprogramacion'
	}
	
}
