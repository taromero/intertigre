package intertigre.domain

import grails.plugin.spock.IntegrationSpec

class ItemListaBuenaFeSpec extends BaseIntegrationSpec{

	def 'crear un equipo, con items de la lista de buena fe en posiciones correctas'(){
		given: '1 equipo con algunos items'
			List<Jugador> jugadores = domainFactoryService.crearXJugadores(4)
			def equipo = new Equipo(itemsListaBuenaFe: new ArrayList<ItemListaBuenaFe>())
			for(i in 0..(jugadores.size()-1)){
				def itemLista = new ItemListaBuenaFe(equipo: equipo, jugador: jugadores.getAt(i), posicion: i)
				equipo.itemsListaBuenaFe.add(itemLista)
			}
		expect: 'deberia estar bien formado'
			equipo.estaBienFormado
	}
	
}
