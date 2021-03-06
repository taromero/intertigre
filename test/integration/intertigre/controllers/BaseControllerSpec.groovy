package intertigre.controllers

import grails.plugin.spock.IntegrationSpec
import grails.plugins.springsecurity.SpringSecurityService
import intertigre.security.SecRole
import intertigre.util.DomainFactoryService

import java.lang.invoke.MethodHandleImpl.BindCaller.T

abstract class BaseControllerSpec extends IntegrationSpec{
	
	SpringSecurityService springSecurityService
	
	DomainFactoryService domainFactoryService
	
	def static roleAdmin = new SecRole(authority: 'ROLE_ADMIN').save()
	def static roleCapitanClub = new SecRole(authority: 'ROLE_CAPITAN_CLUB').save()
	def static roleCapitanEquipo = new SecRole(authority: 'ROLE_CAPITAN_EQUIPO').save()
	def static roleJugador = new SecRole(authority: 'ROLE_JUGADOR').save(flush: true, failOnError: true)
	
	def loggedUser
	Map renderMap
	
	def setup(){
		controller.metaClass.getLoggedUser = { loggedUser }
		controller.metaClass.esAdmin = { true }
		controller.metaClass.esCapitanClub = { true }
		
		controller.metaClass.render = {Map m ->
		  renderMap = m
		}
		grails.buildtestdata.TestDataConfigurationHolder.reset()
	}

	abstract getController();
	
}
