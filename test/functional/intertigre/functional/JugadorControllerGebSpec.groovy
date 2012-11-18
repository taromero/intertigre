package intertigre.functional

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;

import geb.spock.GebReportingSpec
import grails.plugins.springsecurity.SpringSecurityService
import intertigre.domain.Jugador
import intertigre.functional.pages.HomePage
import intertigre.functional.pages.JugadorEditPage
import intertigre.functional.pages.LoginPage;
import intertigre.security.SecRole
import intertigre.security.SecUserSecRole

class JugadorControllerGebSpec extends GebReportingSpec{
//	SpringSecurityService springSecurityService = new SpringSecurityService()
//	def static roleCapitanEquipo = new SecRole(authority: 'ROLE_CAPITAN_EQUIPO').save(flush: true, failOnError: true)
	
	def 'un usuario no administrador quiere editar informacion de otro jugador'() {
		given: 'un usuario no administrador logeado y otro jugador a editar'
			def password = 'p'
			Jugador usuarioNoAdmin = Jugador.build(password: password)
			SecRole roleCapitanEquipo = SecRole.build(authority: 'ROLE_CAPITAN_EQUIPO')
			SecUserSecRole.create usuarioNoAdmin, roleCapitanEquipo
			usuarioNoAdmin.save(flush: true, failOnError: true)
			logearse(usuarioNoAdmin.email, password)
			def jugadorAEditar = Jugador.build()
		when: 'quiere editar informacion de otro jugador'
			to JugadorEditPage, jugadorAEditar.id
		then: 'no deberia poder acceder a la pagina de edicion, y deberia ser redirigido al home'
			waitFor { title == 'Show Jugador' }
			$('.message').text() == 'Solo podes editar tus datos'
	}
	
	
	private void logearse(email, password){
		to LoginPage
		emailField.value(email)
		passwordField.value(password)
		submitButton.click()
		at HomePage
	}
}
