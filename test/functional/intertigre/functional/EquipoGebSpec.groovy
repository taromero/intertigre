package intertigre.functional

import intertigre.domain.Equipo
import intertigre.domain.Jugador
import intertigre.functional.pages.EquipoShowPage
import intertigre.functional.pages.JugadorShowPage
import intertigre.security.SecUserSecRole

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class EquipoGebSpec extends BaseControllerGebSpec{

	def setup() {
		admin = Jugador.build(email: adminMail, password: passwordDefault)
		SecUserSecRole.create(admin, roleAdmin).save()
		logearse(admin.email, passwordDefault)
	}
	
	def 'deberia poder acceder a los jugadores de un equipo en la vista del equipo'() {
		given: 'un equipo'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		and: 'jugadores pertenecientes al equipo'
			def jugadores = equipo.jugadores
		when: 'voy a ver al equipo'
			to EquipoShowPage, equipo.id
			at EquipoShowPage
		then: 'deberia ver links de todos los jugadores'
			itemsListaBuenaFeField.size() == jugadores.size()
			jugadores*.apellido.every { apellido -> itemsListaBuenaFeField*.text().find { it.contains(apellido) } }
		when: 'hago click en un link'
			$("a[name='${jugadores.first().id}']").click()
		then: 'deberia ver el detalle del jugador'
			at JugadorShowPage
	}
	
}
