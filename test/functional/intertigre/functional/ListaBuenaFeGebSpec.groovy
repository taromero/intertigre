package intertigre.functional

class ListaBuenaFeGebSpec extends BaseControllerGebSpec{

	def 'cambiar las posiciones de la lista de buena fe'() {
		given: 'un equipo con jugadores'
		when: 'cambio el orden de la lista'
		then: 'la lista se actualiza con las posiciones indicadas'
	}
	
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
	
	def 'filtrar jugadores de la lista de propuestos'() {
		given: 'un club con jugadores'
		and: 'un equipo con jugadores de ese club'
		when: 'filtro la lista de jugadores disponibles para agregar con una palabra'
		then: 'se deben mostrar solo los jugadores que pasan el filtro'
	}
	
	def 'ver jugadores disponibles para agregar al equipo'() {
		given: 'un club con jugadores'
		and: 'un equipo con jugadores de ese club de sexo S y categoria C'
		when: 'voy a la pantalla de edicion de lista de buena fe'
		then: 'solo deberia ver jugadores para agregar que sean del club,' +
				'que sean de sexo S y pertenezcan a la categoria C'
		and: 'los jugadores deberian estar ordenados alfabeticamente'
	}
	
}
