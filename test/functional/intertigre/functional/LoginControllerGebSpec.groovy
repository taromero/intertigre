package intertigre.functional	

import geb.spock.GebReportingSpec
import intertigre.domain.Jugador
import intertigre.functional.pages.HomePage
import intertigre.functional.pages.LoginPage

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class LoginControllerGebSpec extends GebReportingSpec{

	def 'logearse bien'() {
		given: 'un usuario del sistema'
			def email = 'a@b.com'
			def password = 'p'
			def j = Jugador.build(email: email, password: password)
			j.save(flush: true, failOnError: true)
		when: 'voy a la pantalla de login e ingreso los datos del usuario'
			to LoginPage
		    emailField.value(email)
			passwordField.value(password)
			submitButton.click()
		then: 'me debe llevar a la pagina home'
			at HomePage
	}
	
	def 'logearse mal'() {
		given: 'un usuario del sistema'
			String email = 'b@b.com'
			def password = 'p'
			Jugador.build(email: email, password: password)
		when: 'voy a la pantalla de login e ingreso datos incorrectos'
			to LoginPage
			emailField.value(email + 'blah')
			passwordField.value(password)
			submitButton.click()
		then: 'deberia devolverme a la pagina de login mostrandome un mensaje de error'
			at LoginPage
			$('.login_message').text() == 'Sorry, we were not able to find a user with that username and password.'
	}
	
}
