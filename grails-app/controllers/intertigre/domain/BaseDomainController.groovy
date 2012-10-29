package intertigre.domain

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_JUGADOR'])
class BaseDomainController {
	
	def springSecurityService

	def Jugador getLoggedUser(){
		return springSecurityService.currentUser
	}
	
	def Boolean esAdmin(){
		return getLoggedUser().getAuthorities()*.authority.contains('ROLE_ADMIN')
	}
	
	def Boolean esCapitanClub(){
		return getLoggedUser().getAuthorities()*.authority.contains('ROLE_CAPITAN_CLUB') || esAdmin()
	}
	
	def Boolean esCapitanEquipo(){
		return getLoggedUser().getAuthorities()*.authority.contains('ROLE_CAPITAN_EQUIPO') || esCapitanClub() || esAdmin()
	}

	def checkIsUsuarioEsDelClub(Club clubInstance){
		Jugador loggedUser = getLoggedUser()
		if(!esAdmin()){	
			if(loggedUser.club.id != clubInstance.id){
				throw new RuntimeException('No podes modificar los datos del club '+ clubInstance.nombre +', solo podes modificar los datos de tu club')
			}
		}
	}
}
