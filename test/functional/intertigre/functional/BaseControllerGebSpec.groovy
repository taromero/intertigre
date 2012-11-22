package intertigre.functional

import geb.spock.GebReportingSpec
import intertigre.security.SecRole

class BaseControllerGebSpec extends GebReportingSpec{
	def static roleAdmin = SecRole.build(authority: 'ROLE_ADMIN')
	def static roleCapitanClub = SecRole.build(authority: 'ROLE_CAPITAN_CLUB')
	def static roleCapitanEquipo = SecRole.build(authority: 'ROLE_CAPITAN_EQUIPO')
	def static roleJugador = SecRole.build(authority: 'ROLE_JUGADOR')
	def static passwordDefault = 'p'
	
}
