package intertigre.functional	

import geb.spock.GebReportingSpec
import intertigre.domain.Jugador

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class LoginControllerGebSpec extends GebReportingSpec{

	def 'logearse bien'() {
		given: 'un usuario del sistema'
			def email = 'a@b.com'
			def password = 'p'
			def j = Jugador.build(email: email, password: password)
			j.save(flush: true, failOnError: true)
		when: 'voy a la pantalla de login e ingreso los datos del usuario'
			go 'login/auth'
			waitFor { title == 'Login' }
			$('#username').value(email)
			$('#password').value(password)
			$('#submit').click()
		then: 'me debe llevar a la pagina home'
			title == 'Intertigres'
	}
	
	def 'logearse mal'() {
		given: 'un usuario del sistema'
			String email = 'b@b.com'
			def password = 'p'
			Jugador.build(email: email, password: password)
		when: 'voy a la pantalla de login e ingreso datos incorrectos'
			go 'login/auth'
			waitFor { title == 'Login' }
			$('#username').value(email + 'blah')
			$('#password').value(password)
			$('#submit').click()
		then: 'deberia devolverme a la pagina de login'
			title == 'Login'
			$('.login_message').text() == 'Sorry, we were not able to find a user with that username and password.'
	}
	
}
