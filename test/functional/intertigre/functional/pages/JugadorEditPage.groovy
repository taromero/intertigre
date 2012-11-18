package intertigre.functional.pages

import geb.Page;

class JugadorEditPage extends Page{
	static url = 'jugador/edit/'
	static at = { waitFor { title == 'Edit Jugador' } }
	
	static content = { 
		dniField { $('#dni') }
		passwordField { $('#password') }
		passwordConfirmField { $('#passwordConfirm') }
		usernameField { $('#username') }
		submitButton { $('.save') }
	}
}
