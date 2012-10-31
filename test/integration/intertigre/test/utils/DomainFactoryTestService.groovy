package intertigre.test.utils

import java.util.List;

import org.joda.time.DateTime

import intertigre.domain.Categoria
import intertigre.domain.Club
import intertigre.domain.Equipo;
import intertigre.domain.Fecha
import intertigre.domain.ItemListaBuenaFe
import intertigre.domain.Jugador

class DomainFactoryTestService {
	
	def List<Jugador> crearXJugadores(Integer cantJugadores){
		def jugadores = new ArrayList<Jugador>()
		for(i in 0..(cantJugadores-1)){
			def jugador = new Jugador(dni: i)
			jugadores.add(jugador)
		}
		return jugadores
	}
	
	def Equipo crearEquipoConXJugadores(Integer cantJugadores, List posiciones = null){
		List<Jugador> jugadores = this.crearXJugadores(cantJugadores)
		def equipo = new Equipo(itemsListaBuenaFe: new ArrayList<ItemListaBuenaFe>())
		for(i in 0..(jugadores.size()-1)){
			def posicion = posiciones != null ? posiciones.get(i) : i
			def itemLista = new ItemListaBuenaFe(equipo: equipo, jugador: jugadores.getAt(i), posicion: posicion)
			equipo.itemsListaBuenaFe.add(itemLista)
		}
		return equipo
	}

	def Map crearXCantidadEquiposDeCategoriaDeXClubesDistintos(Integer cantidad, Categoria categoria = null, Integer cantClubes = 1){
		categoria = categoria ?: new Categoria().save()
		List<Equipo> equipos = new ArrayList<Equipo>()
		def clubes = []
		cantClubes.times { i -> clubes.add(new Club(triosDeCanchasDisponibles: 1, nombre: 'club' + i)) }
		def clubIndex = 0
		for(i in 1..cantidad){
			def club = clubes.get(clubIndex)
			club.id = clubIndex
			def equipo = new Equipo(id: i, categoria: categoria, club: club)
			equipo.id = i
			equipos.add(equipo)
			club.equipos.add(equipo)
			clubIndex++
			if(clubIndex == cantClubes){ clubIndex = 0 }
		}
		return [equipos: equipos, clubes: clubes]
	}
	
	def Equipo crearEquipoAPartirDeNombres(String... nombres){
		def jugadores = []
		nombres.eachWithIndex{
			nombre, i ->
			jugadores.add(new Jugador(nombre: nombre, dni: i))
		}
		return crearEquipoCanotto(jugadores)
	}
	
	def Fecha crearFechaCanottoChasqui(fecha){
		def equipoCanotto = crearEquipoCanotto()
		def equipoChasqui = crearEquipoElChasqui()
		return new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
							fechaDeJuego: fecha).save(flush: true)
	}
	
	def Club crearClubCanotto(){
		def club = Club.find { nombre == 'Canottieri' && localidad == 'Tigre' }
		return  club ?: new Club(nombre: 'Canottieri', localidad: 'Tigre', direccion: 'Mitre 123',
							email: 'canottieri@gmail.com', telefono: 1234, triosDeCanchasDisponibles: 1).save(failOnError: true, flush:true)
	}
	
	def Club crearClubElChasqui(){
		def club = Club.find { nombre == 'El Chasqui' && localidad == 'El Talar' }
		return club ?: new Club(nombre: 'El Chasqui', localidad: 'El Talar', direccion: 'Belgrano 467',
					email: 'elchasqui@gmail.com', telefono: 9085).save(failOnError: true, flush:true)
	}
	
	def Equipo crearEquipoCanotto(List<Jugador> jugadores){
		def canotto = crearClubCanotto()

		if(jugadores == null){
			jugadores = [
				Jugador.build(club: canotto, nombre: 'Tomas', dni: '12345678'),
				Jugador.build(club: canotto, nombre: 'Roger', dni: '12345675'),
				Jugador.build(club: canotto, nombre: 'Juan Martin', dni: '12345676'),
				Jugador.build(club: canotto, nombre: 'Chucho', dni: '12345674'),
				Jugador.build(club: canotto, nombre: 'Guillermo', dni: '12345673'),
			]
		}

		def equipo = new Equipo(club: canotto.save(), categoria: new Categoria(nombre: '+19', sexo: 'M').save(), jerarquia: 'A', capitan: jugadores.get(0), estaConfirmado: false)

		def itemsListaBuenaFe = new TreeSet()

		jugadores.eachWithIndex { jugador, index -> 
										itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index)) 
										jugador.save(failOnError:true)		
								}
		
		itemsListaBuenaFe.each { it.jugador.itemsListasBuenaFe.add(it) }
		
		equipo.itemsListaBuenaFe = itemsListaBuenaFe;
		
		equipo.save(failOnError: true, flush:true)
		
		return equipo
	}
	
	def Equipo crearEquipoElChasqui(){
		def elChasqui = crearClubElChasqui()

		def jugadores = [
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,1,8,0,0,0).toDate(), nombre: 'Rafael', apellido: 'Nadal', 
							dni: '87654321', email: 'nadal@gmail.com'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,9,23,0,0,0).toDate(), nombre: 'David', apellido: 'nalbandian', 
							dni: '87654323', email: 'nalbandian@gmail.com'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,7,4,0,0,0).toDate(), nombre: 'Charlie', apellido: 'Berlocq', 
							dni: '87654324', email: 'berlocq@gmail.com'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Carlos', apellido: 'Moya', 
							dni: '87654325', email: 'moya@gmail.com')
		]

		def equipo = new Equipo(club: elChasqui, categoria: new Categoria(nombre: '+19', sexo: 'M').save(), jerarquia: 'A', capitan: jugadores.get(0), estaConfirmado: false)

		def itemsListaBuenaFe = new TreeSet()

		jugadores.eachWithIndex { jugador, index -> 
										itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index)) 
										jugador.save(failOnError:true)
								}

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;

		equipo.save(failOnError: true, flush: true)
		
		return equipo
	}

	private void setUpEquipo(Equipo equipo, jugadores){
		def itemsListaBuenaFe = new TreeSet()
		
		jugadores.eachWithIndex { jugador, index ->
										itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index))
										jugador.save(failOnError:true)
								}

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;

		equipo.save(failOnError: true, flush: true)
	}
	
	def Equipo crearEquipoNahuel(){
		def nahuel = new Club(nombre: 'Nahuel', localidad: 'Tigre', direccion: 'Lavalle 467',
				email: 'nahuel@gmail.com', telefono: 9084).save()

		def jugadores = [
			new Jugador(club: nahuel, sexo: 'M', nacimiento: new DateTime(1990,1,8,0,0,0).toDate(), nombre: 'Robin', apellido: 'Soderling', 
							dni: '87654637', email: 'soderling@gmail.com'),
			new Jugador(club: nahuel, sexo: 'M', nacimiento: new DateTime(1989,9,23,0,0,0).toDate(), nombre: 'Pete', apellido: 'Sampras', 
							dni: '87659463', email: 'sampras@gmail.com'),
			new Jugador(club: nahuel, sexo: 'M', nacimiento: new DateTime(1989,7,4,0,0,0).toDate(), nombre: 'Andre', apellido: 'Agassi', 
							dni: '32154324', email: 'agassi@gmail.com'),
			new Jugador(club: nahuel, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Dominic', apellido: 'Hrbati', 
							dni: '32154325', email: 'hrbati@gmail.com')
		]

		def equipo = new Equipo(club: nahuel, categoria: new Categoria(nombre: '+19', sexo: 'M').save(), jerarquia: 'A', capitan: jugadores.get(0), estaConfirmado: false)

		setUpEquipo(equipo, jugadores)
		
		return equipo
	}
}
