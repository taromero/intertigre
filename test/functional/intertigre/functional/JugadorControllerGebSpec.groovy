package intertigre.functional

import geb.spock.GebReportingSpec
import intertigre.domain.Club
import intertigre.domain.Jugador
import intertigre.functional.pages.HomePage
import intertigre.functional.pages.JugadorEditPage
import intertigre.functional.pages.JugadorShowPage;
import intertigre.functional.pages.LoginPage
import intertigre.security.SecRole
import intertigre.security.SecUserSecRole

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class JugadorControllerGebSpec extends GebReportingSpec{
	def static roleAdmin = SecRole.build(authority: 'ROLE_ADMIN')
	def static roleCapitanClub = SecRole.build(authority: 'ROLE_CAPITAN_CLUB')
	def static roleCapitanEquipo = SecRole.build(authority: 'ROLE_CAPITAN_EQUIPO')
	def static roleJugador = SecRole.build(authority: 'ROLE_JUGADOR')
	def static passwordDefault = 'p'
		
	def 'un usuario no administrador quiere editar informacion de otro jugador'() {
		given: 'un usuario no administrador logeado'
			Jugador usuarioNoAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioNoAdmin, role).save()
			logearse(usuarioNoAdmin.email, passwordDefault)
		and: 'un usuario x a editar'
			def jugadorAEditar = Jugador.buildLazy(dni: '2')
		when: 'quiere editar informacion x'
			to JugadorEditPage, jugadorAEditar.id
		then: 'no deberia poder acceder a la pagina de edicion, y deberia ser redirigido al show'
			at JugadorShowPage
			$('.message').text() == 'Solo podes editar tus datos'
		where:
			role << [roleCapitanClub, roleCapitanEquipo, roleJugador]
	}
	
	def 'un usuario administrador quiere editar informacion de otro jugador'() {
		given: 'un usuario administrador logeado'
			Jugador usuarioNoAdmin = Jugador.build(password: passwordDefault)
			SecUserSecRole.create(usuarioNoAdmin, roleAdmin).save()
			logearse(usuarioNoAdmin.email, passwordDefault)
		and: 'un usuario x a editar'
			def jugadorAEditar = Jugador.buildLazy(dni: '2')
		when: 'quiere editar informacion de otro jugador'
			to JugadorEditPage, jugadorAEditar.id
		then: 'deberia poder acceder a la pagina de edicion'
			at JugadorEditPage
		when: 'le cambia el valor al dni'
			dniField.value('5000')
			passwordField.value('p')
			passwordConfirmField.value('p')
			usernameField.value('1234@intertigre.com')
			submitButton.click()
		then: 'se debe redireccionar a show, mostrandose el cambio'
			at JugadorShowPage
			dniField.text() == '5000'
	}
	
	private void logearse(email, password){
		to LoginPage
		at LoginPage
		emailField.value(email)
		passwordField.value(password)
		submitButton.click()
		at HomePage
	}
}
