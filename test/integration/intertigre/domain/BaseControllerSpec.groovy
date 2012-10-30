package intertigre.domain

import grails.plugin.spock.IntegrationSpec
import grails.plugins.springsecurity.SpringSecurityService;
import intertigre.security.SecRole

abstract class BaseControllerSpec extends IntegrationSpec{
	
	SpringSecurityService springSecurityService
	
	DomainFactoryTestService df = new DomainFactoryTestService()
	
	def static roleAdmin = new SecRole(authority: 'ROLE_ADMIN').save()
	def static roleCapitanClub = new SecRole(authority: 'ROLE_CAPITAN_CLUB').save()
	def static roleCapitanEquipo = new SecRole(authority: 'ROLE_CAPITAN_EQUIPO').save()
	def static roleJugador = new SecRole(authority: 'ROLE_JUGADOR').save(flush: true, failOnError: true)
	
	def loggedUser
	Map renderMap
	
	def setup(){
		controller.metaClass.getLoggedUser = { loggedUser }
		controller.metaClass.esAdmin = { true }
		
		controller.metaClass.render = {Map m ->
		  renderMap = m
		}
	}

	abstract getController();
	
}
