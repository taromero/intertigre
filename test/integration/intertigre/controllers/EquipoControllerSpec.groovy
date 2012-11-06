package intertigre.controllers;

import intertigre.domain.Categoria;
import intertigre.domain.Club;
import intertigre.domain.Equipo;
import intertigre.domain.EquipoController;
import intertigre.domain.ItemListaBuenaFe;
import intertigre.domain.Jugador;

import org.antlr.runtime.DFA;

import spock.lang.Ignore;
import grails.plugin.spock.IntegrationSpec;

public class EquipoControllerSpec extends BaseControllerSpec{

	EquipoController controller = new EquipoController()
	
	def 'cambiar capitan equipo'() {
		given: 'un equipo con un capitan'
		    def canottoTeam = df.crearEquipoCanotto()
		    controller.metaClass.esCapitanClub = { true }
		when: 'cambio el capitan'
			controller.params.idNuevoCapitan = canottoTeam.jugadores.find { it.nombre == 'Roger' }.id
			controller.params.idEquipo = canottoTeam.id
			controller.cambiarCapitanEquipoAjax()
		then: 'veo reflejado el cambio'
			canottoTeam.capitan == Jugador.findAll().find { it.nombre == 'Roger' } //El find de GORM no funcionaba bien
	}
	
	def 'cambiar orden de la lista de buena fe'() {
		given: 'un equipo con una lista con orden determinado 1-tomas, 2-delpo, 3-roger'
			def canottoTeam = df.crearEquipoAPartirDeNombres('tomas', 'delpo', 'roger', 'willy', 'chucho')
			
			def sampras = Jugador.build(apellido: 'sampras', dni: '1234')
			controller.metaClass.esCapitanClub = { true }
		when: 'cambiamos ese orden. 1-roger, 2-tomas, delpo lo sacamos, y agregamos 3ero a sampras'
			def rogerDni = canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'roger' }.jugador.dni
			def tomasDni = canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'tomas' }.jugador.dni
			def delpoDni = canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'delpo' }.jugador.dni
			def willyDni = canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'willy' }.jugador.dni
			
			String listaDnis = canottoTeam.jugadores*.dni.inject('') { accum, dni -> accum + dni + ',' }
			listaDnis = listaDnis.replace(rogerDni, 'aux')
			listaDnis = listaDnis.replace(tomasDni, rogerDni)
			listaDnis = listaDnis.replace('aux', tomasDni)
			listaDnis = listaDnis.replace(delpoDni + ',', '')
			listaDnis = listaDnis.replace(willyDni, sampras.dni + ',' + willyDni)
			if(listaDnis[listaDnis.length()-1] == ','){ listaDnis = listaDnis[0 .. listaDnis.length()-2] }//quito la ultima coma
			
			controller.params.id = canottoTeam.id
			controller.params.listaEditada = listaDnis
			controller.update()
		then: 'los cambios se deben ver reflejados'
			canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'roger' }.posicion == 0
			canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'tomas' }.posicion == 1
			canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'delpo' } == null
			canottoTeam.itemsListaBuenaFe.find { it.jugador.apellido == 'sampras' }.posicion == 2
			canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'willy' }.posicion == 3
			canottoTeam.itemsListaBuenaFe.find { it.jugador.nombre == 'chucho' }.posicion == 4
	}
	
	def 'obtener mis equipos'() {
		given: 'un jugador loggeado con equipos'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't')
			def equipoChasqui = df.crearEquipoElChasqui();
			def equipoNahuel = df.crearEquipoNahuel();
			def itemsListaBuenaFe = new TreeSet([new ItemListaBuenaFe(jugador: loggedUser, equipo: equipoNahuel, posicion: 4).save(),
													new ItemListaBuenaFe(jugador: loggedUser, equipo: equipoChasqui, posicion: 4).save()])
			loggedUser.itemsListasBuenaFe = itemsListaBuenaFe
			def equipos = [equipoChasqui, equipoNahuel]
		when: 'pido los equipos del jugador'
			controller.misEquipos()
		then: 'debo obtenerlos'
			renderMap.view == 'list'
			renderMap.model.equipoInstanceList.containsAll(equipos)
			renderMap.model.equipoInstanceList.size() == equipos.size()
	}
	
	def 'obtener mis equipos, cuando no tengo equipos'() {
		given: 'un jugador loggeado sin equipos'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't')
			def equipoChasqui = df.crearEquipoElChasqui()
			def equipoNahuel = df.crearEquipoNahuel()
			def equipos = [equipoChasqui, equipoNahuel]
		when: 'pido los equipos del jugador'
			controller.misEquipos()
		then: 'debe volver a la vista de listado de equipos general y mostrar un mensaje indicativo de que no se encontraron equipos'
			controller.response.redirectedUrl == '/equipo/list'
			controller.flash.message == 'No tenes equipos por el momento'
	}
	
	def 'obtener los equipos de mi club'() {
		given: 'un jugador de un club. El club tiene 4 equipos'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't')
			def equiposClubes = df.crearXCantidadEquiposDeCategoriaDeXClubesDistintos(4)
			equiposClubes.equipos.each { it.save(failOnError:true) }
			equiposClubes.clubes.get(0).save(flush: true, failOnError:true)
			loggedUser.club = equiposClubes.equipos.get(0).club
		when: 'pido los equipos del club'
			controller.listEquiposClub()
		then: 'me debe traer los equipos'
			renderMap.model.equipoInstanceList.size() == 4
	}
	
	def 'crear equipo'(){
		given: 'un usuario loggeado'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't', dni: '1', club: df.crearClubCanotto())
			def categoria = new Categoria(nombre: '+19', sexo: 'M', edadLimiteInferior: 19, edadLimiteSuperior: 25)
							.save(failOnError: true, flush: true)
		when: 'creo un equipo nuevo'
			controller.params.categoria = ['id': categoria.id]
			controller.params.jerarquia = 'A'
			controller.save()
			def equipoNuevo = Equipo.findAll().get(0)
		then: 'se debe crear el equipo'
			equipoNuevo.categoria == Categoria.get(categoria.id)
			equipoNuevo.jerarquia == 'A'
			controller.response.redirectedUrl == '/equipo/show/' + equipoNuevo.id
	}
    
	@Ignore	
	def 'crear equipo, para una categoria/jerarquia/club existente'(){
		given: 'un usuario loggeado'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't', club: df.crearClubCanotto())
		and: 'un equipo con categoria x y jerarquia y'
			def cat = new Categoria(nombre: '+19', sexo: 'M', edadLimiteInferior: 19, edadLimiteSuperior: 25)
							.save(failOnError: true, flush: true)
			new Equipo(jerarquia: 'A', categoria: cat, club: loggedUser.club).save(flush: true)
		when: 'creo un equipo nuevo con la misma categoria/jerarquia/club'
			controller.params.categoria = ['id': cat.id]
			controller.params.jerarquia = 'A'
			controller.save()
			Equipo equipo1 = Equipo.findAll().get(0)
			Equipo equipo2 = Equipo.findAll().get(1)
			Club club1 = equipo1.club
			Club club2 = equipo2.club
		then: 'no se debe crear el equipo, y se debe volver a la vista de creacion con un mensaje indicativo'
			equipo1.jerarquia == equipo2.jerarquia
			club1 == club2
			equipo1.club == equipo2.club 
			equipo1.categoria == equipo2.categoria
			Equipo.count() == 1
			renderMap.view == '/equipo/create'
			renderMap.model.equipoInstance.errors.getAt('club').code == 'unique'
	}
	
	def 'buscar equipos'(){
		given:
//			def canotto = df.crearClubCanotto()
//			def mas25 = Categoria.build()
			for(i in 1..20){
				Equipo.build()
			}
//			for(i in 1..5){
//				Equipo.build(club: canotto, mas25, 'A')
//			}
		when:
			controller.params.club = ['id': clubId]
			controller.params.categoria = ['id': categoriaId]
			controller.params.jerarquia = jerarquiaId
			controller.params.offset = 0
			controller.params.max = 10
			controller.list()
		then:
			renderMap.view == 'list'
			renderMap.model.equipoInstanceList.size() == 10
		where:
			clubId | categoriaId | jerarquiaId
			  ''   |     ''      |      ''
		   'false' |   'false'   |   'false'
//		   'false' |   Categoria.build(nombre: '+25')
	}
}