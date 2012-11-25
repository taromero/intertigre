package intertigre.functional

import intertigre.domain.Equipo
import intertigre.domain.Jugador
import intertigre.functional.pages.EquipoShowPage
import intertigre.functional.pages.ListaBuenaFeEditPage
import intertigre.security.SecUserSecRole
import intertigre.util.DomainFactoryService

import java.lang.invoke.MethodHandleImpl.BindCaller.T

class ListaBuenaFeGebSpec extends BaseControllerGebSpec{

	DomainFactoryService domainFactoryService = new DomainFactoryService()
	
	Jugador admin
	
	def setup() {
		admin = Jugador.build(password: passwordDefault)
		SecUserSecRole.create(admin, roleAdmin).save()
		logearse(admin.email, passwordDefault)
	}
	
	def 'cambiar las posiciones de la lista de buena fe'() {
		given: 'un equipo con jugadores'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		when: 'cambio el orden de la lista'
			to ListaBuenaFeEditPage, equipo.id
			at ListaBuenaFeEditPage
			def jugadorAMover = $("li#dni" + equipo.jugadores.find { it.apellido = 'Romero'}.dni)
			interact {
				dragAndDropBy(jugadorAMover, 0, 50)
			}
			submitButton.click()
		then: 'deberia llevarme a la pagina de show del equipo'
			at EquipoShowPage
		and: 'la lista se deberia mostrar con las posiciones actualizadas'
			itemsListaBuenaFeField.findIndexOf { it.text() == 'Juan Martin Del Potro'} < itemsListaBuenaFeField.findIndexOf { it.text() == 'Tomas Romero'}
	}
	
	def 'filtrar jugadores de la lista de propuestos'() {
		given: 'un club con jugadores'
			def jugadores = domainFactoryService.crearJugadoresLibresCanotto()
		and: 'un equipo con jugadores de ese club'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		when: 'filtro la lista de jugadores disponibles para agregar con una palabra'
			to ListaBuenaFeEditPage, equipo.id
			at ListaBuenaFeEditPage
			filtroJugadoresClub.value('Tommy') 
		then: 'se deben mostrar solo los jugadores que pasan el filtro'
			$('#dni' + jugadores.find { it.apellido == 'Robredo' }.dni).displayed
			$('#dni' + jugadores.find { it.apellido == 'Haas' }.dni).displayed
			for(jugador in jugadores.find { it.nombre != 'Tommy' }) {
				$('#dni' + jugador.dni).displayed == false
			}
	}
	
	/*
	def 'agregar jugadores a la lista de buena fe'() {
		given: 'un club con jugadores'
		and: 'un equipo con jugadores de ese club'
		when: 'agrego un jugador nuevo a la lista en la posicion x'
		then: 'el equipo debe contar con el jugador nuevo en la posicion correcta'
	}
	
	def 'sacar un jugador de la lista de buena de'() {
		given: 'un equipo con jugadores'
		when: 'saco un jugador de la lista de buena fe'
		then: 'el equipo no debe poseer mas al jugador'
		and: 'las posiciones de la lista se deben actualizar'
	}
	
	def 'ver jugadores disponibles para agregar al equipo'() {
		given: 'un club con jugadores'
		and: 'un equipo con jugadores de ese club de sexo S y categoria C'
		when: 'voy a la pantalla de edicion de lista de buena fe'
		then: 'solo deberia ver jugadores para agregar que sean del club,' +
				'que sean de sexo S y pertenezcan a la categoria C'
		and: 'los jugadores deberian estar ordenados alfabeticamente'
	}
*/	
}
