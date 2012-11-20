package intertigre.functional.pages

import geb.Page

class LoginPage extends Page{
	static url = 'login/auth'
	static at = { waitFor { title == "Login" } }
	
	static content = {
		emailField { $('#email') }
		passwordField { $('#password') }
		submitButton { $('#submit') }
	}
}
