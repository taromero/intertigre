package intertigre.functional

import geb.spock.GebReportingSpec
import intertigre.domain.Equipo
import intertigre.functional.pages.HomePage
import intertigre.functional.pages.LoginPage
import intertigre.functional.pages.LogoutPage
import intertigre.security.SecRole

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class BaseControllerGebSpec extends GebReportingSpec{
	def static roleAdmin = SecRole.build(authority: 'ROLE_ADMIN')
	def static roleCapitanClub = SecRole.build(authority: 'ROLE_CAPITAN_CLUB')
	def static roleCapitanEquipo = SecRole.build(authority: 'ROLE_CAPITAN_EQUIPO')
	def static roleJugador = SecRole.build(authority: 'ROLE_JUGADOR')
	
	def static adminMail = 'admin@mail.com'
	def static passwordDefault = 'p'
	
	def cleanup() {
		Equipo.executeUpdate("delete ItemListaBuenaFe")
		Equipo.executeUpdate("delete Equipo")
		Equipo.executeUpdate("delete SecUserSecRole")
		Equipo.executeUpdate("delete SecUser")
	}
	
	def logearse(email, password){
		to LoginPage
		at LoginPage
		emailField.value(email)
		passwordField.value(password)
		submitButton.click()
		at HomePage
	}
	
	def desloguearse() {
		to LogoutPage
	}
}
