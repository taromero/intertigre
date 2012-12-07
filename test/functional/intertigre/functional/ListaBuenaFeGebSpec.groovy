package intertigre.functional

import intertigre.domain.Equipo
import intertigre.domain.Jugador
import intertigre.functional.pages.EquipoShowPage
import intertigre.functional.pages.ListaBuenaFeEditPage
import intertigre.security.SecUserSecRole
import intertigre.util.DomainFactoryService

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import spock.lang.IgnoreRest

class ListaBuenaFeGebSpec extends BaseControllerGebSpec{

	DomainFactoryService domainFactoryService = new DomainFactoryService()
	
	static Jugador admin
	
	def setup() {
		admin = Jugador.build(email: adminMail, password: passwordDefault)
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
	
	def 'agregar jugadores a la lista de buena fe'() {
		given: 'un club con jugadores'
			def jugadores = domainFactoryService.crearJugadoresLibresCanotto()
		and: 'un equipo con jugadores de ese club'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		when: 'agrego un jugador nuevo a la lista en ultima posicion'
			to ListaBuenaFeEditPage, equipo.id
			at ListaBuenaFeEditPage
			def jugadorAMover = $('#dni' + jugadores.find { it.nombre == 'Novak' }.dni)
			interact {
				dragAndDrop(jugadorAMover, jugadoresDelEquipoDiv)
			}
			submitButton.click()
		then: 'deberia llevarme a la pagina de show del equipo'
			at EquipoShowPage
		then: 'el equipo debe contar con el jugador nuevo'
			itemsListaBuenaFeField.find { it.text() == 'Novak Djokovic'} != null
		and: 'en la ultima posicion'
			itemsListaBuenaFeField.findIndexOf { it.text() == 'Novak Djokovic'} == (itemsListaBuenaFeField.size() -1)
	}
	
	def 'sacar un jugador de la lista de buena de'() {
		given: 'un equipo con jugadores'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		when: 'saco un jugador de la lista de buena fe'
			to ListaBuenaFeEditPage, equipo.id
			at ListaBuenaFeEditPage
			def jugadorAMover = $('#dni' + equipo.jugadores.find { it.nombre == 'Juan Martin' }.dni)
			interact {
				dragAndDrop(jugadorAMover, jugadoresDelClubDiv)
			}
			submitButton.click()
		then: 'deberia llevarme a la pagina de show del equipo'
			at EquipoShowPage
		and: 'el equipo no debe poseer mas al jugador'
			itemsListaBuenaFeField.find { it.text() == 'Juan Martin Del Potro'} == null
	}
	
	def 'ver jugadores disponibles para agregar al equipo'() {
		given: 'jugadores libres de clubes distintos, de ambos sexos'
			def jugadoresLibresHombresCanotto = domainFactoryService.crearJugadoresLibresCanotto()
			def JugadorasLibresMujeresCanotto = domainFactoryService.crearJugadorasMujeresLibresCanotto()
			def jugadoresLibresHombresNahuel = domainFactoryService.crearJugadoresLibresNahuel()
		and: 'un equipo A y un equipo B del mismo club y categoria'
			Equipo equipoCanottoHombresA = domainFactoryService.crearEquipoMas19MCanotto()
			Equipo equipoCanottoHombresB = domainFactoryService.crearEquipoBMas19MCanotto()
		when: 'voy a la pantalla de edicion de lista de buena fe'
			to ListaBuenaFeEditPage, equipoCanottoHombresA.id
			at ListaBuenaFeEditPage
		
		then: 'deberia mostrar los jugadores libres del mismo club y sexo que el equipo'
			jugadoresLibresHombresCanotto.each { jugadorLibreCanotto ->
				jugadoresDelClub.find { it.text()== jugadorLibreCanotto.nombre + ' ' + jugadorLibreCanotto.apellido }
			}
		and: 'los jugadores del equipo B'
			equipoCanottoHombresB.jugadores.each { jugadorEquipoBCanotto ->
				jugadoresDelClub.find { it.text()== jugadorEquipoBCanotto.nombre + ' ' + jugadorEquipoBCanotto.apellido }
			}
			jugadoresDelClub.size() == (jugadoresLibresHombresCanotto.size() + equipoCanottoHombresB.jugadores.size())
		and: 'que no pertenezcan al equipo'
			!jugadoresDelClub*.text().any { nombreSugerido -> equipoCanottoHombresA.jugadores*.nombre.find { nombre -> nombre =~ nombreSugerido } }
		and: 'los jugadores deberian estar ordenados alfabeticamente por nombre y apellido'
			jugadoresDelClub*.text() == jugadoresDelClub*.text().sort { a,b -> a.compareToIgnoreCase(b) }
	}
}
