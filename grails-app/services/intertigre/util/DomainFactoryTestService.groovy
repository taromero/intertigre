package intertigre.util

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
			def jugador = Jugador.build()
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

	def Map crearXCantidadEquiposDeCategoriaDeXClubesDistintos(Integer cantidadEquipos, Categoria categoria = null, Integer cantClubes = 1){
		categoria = categoria ?: Categoria.build()
		List<Equipo> equipos = new ArrayList<Equipo>()
		def clubes = []
		cantClubes.times { clubes.add(Club.build(triosDeCanchasDisponibles: 1)) }
		def clubIndex = 0
		cantidadEquipos.times {
			def club = clubes.get(clubIndex)
			def jerarquia = getJerarquiaDisponibleParaElClubCategoria(club, categoria)
			def equipo = Equipo.build(categoria: categoria, club: club, jerarquia: jerarquia)
			equipos.add(equipo)
			club.equipos.add(equipo)
			clubIndex++
			if(clubIndex == cantClubes){ clubIndex = 0 }
		}
		return [equipos: equipos, clubes: clubes]
	}
	
	private String getJerarquiaDisponibleParaElClubCategoria(club, categoria) {
		def jerarquias = Equipo.findAll().findAll { it.categoria == categoria && it.club == club }*.jerarquia
		def jerarquiaPosibles = getPosiblesJerarquias()
		def jerarquia = jerarquiaPosibles.get(0)
		for(int i = 1; jerarquias.contains(jerarquia); i++) {
			jerarquia = jerarquiaPosibles.get(i)
		}
		return jerarquia
	}
	
	private List<String> getPosiblesJerarquias(){
		List<String> jerarquias = []
		char jerarquia = 'A'
        char jerarquiaAux = jerarquia
        24.times { i -> 
            jerarquias.add(jerarquiaAux.toString())
            jerarquiaAux = jerarquia + 1
            jerarquia++
        }
        jerarquia = 'A'
        24.times { i ->
            jerarquias.add(jerarquiaAux.toString() + jerarquiaAux.toString())
            jerarquiaAux = jerarquia + 1
            jerarquia++
        }
		return jerarquias
	}
	
	def Equipo crearEquipoAPartirDeNombres(String... nombres){
		def canotto = crearClubCanotto()
		def jugadores = []
		nombres.eachWithIndex{
			nombre, i ->
			jugadores.add(Jugador.build(nombre: nombre, dni: i, club: canotto))
		}
		return crearEquipoMas19MCanotto(jugadores)
	}
	
	def Fecha crearFechaCanottoChasqui(fecha){
		def equipoCanotto = crearEquipoMas19MCanotto()
		def equipoChasqui = crearEquipoMas19MElChasqui()
		return new Fecha(equipoLocal: equipoCanotto, equipoVisitante: equipoChasqui,
							fechaDeJuego: fecha).save(flush: true)
	}
	
	def Club crearClubCanotto(){
		def club = Club.findAll().find { it.nombre == 'Canottieri' && it.localidad == 'Tigre' }
		return  club ?: new Club(nombre: 'Canottieri', localidad: 'Tigre', direccion: 'Mitre 123',
							email: 'canottieri@gmail.com', telefono: 1234, triosDeCanchasDisponibles: 1).save(failOnError: true, flush:true)
	}
	
	def Club crearClubElChasqui(){
		def club = Club.findAll().find { it.nombre == 'El Chasqui' && it.localidad == 'El Talar' }
		return club ?: new Club(nombre: 'El Chasqui', localidad: 'El Talar', direccion: 'Belgrano 467',
					email: 'elchasqui@gmail.com', telefono: 9085, triosDeCanchasDisponibles: 1).save(failOnError: true, flush:true)
	}
	
	def Club crearClubNahuel(){
		def club = Club.findAll().find { it.nombre == 'Nahuel' && it.localidad == 'Tigre' }
		return club ?: new Club(nombre: 'Nahuel', localidad: 'Tigre', direccion: 'Lavalle 467',
				email: 'nahuel@gmail.com', telefono: 9084, triosDeCanchasDisponibles: 1).save(failOnError: true, flush:true)
	}
	
	def Equipo crearEquipoMas19MCanotto(List<Jugador> jugadores){
		def canotto = crearClubCanotto()

		if(jugadores == null){
			jugadores = [
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Tomas', apellido: 'Romero', 
							dni: '12345678', email: 'canotto90@gmail.com', password: 't', 
							urlImagen: 'http://a7.sphotos.ak.fbcdn.net/hphotos-ak-snc6/251940_3763810087241_391676411_n.jpg'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1987,8,23,0,0,0).toDate(), nombre: 'Juan Martin', apellido: 'Del Potro', 
								dni: '12345676', email: 'delpotro@gmail.com', password: 'd'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,9,14,0,0,0).toDate(), nombre: 'Roger', apellido: 'Federer', 
								dni: '12345675', email: 'federer@gmail.com', password: 'f'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Chucho', apellido: 'Acasusso', 
								dni: '12345674', email: 'acasusso@gmail.com', password: 'a'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1989,6,27,0,0,0).toDate(), nombre: 'Guillermo', apellido: 'Cañas', 
								dni: '12345673', email: 'canas@gmail.com', password: 'c')
			]
		}

		def equipo = setUpEquipo(jugadores, canotto)
		
		return equipo
	}
	
	def Equipo crearEquipoMas19MElChasqui(){
		def elChasqui = crearClubElChasqui()

		def jugadores = [
			Jugador.build(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,1,8,0,0,0).toDate(), nombre: 'Rafael', apellido: 'Nadal', 
							dni: '87654321', email: 'nadal@gmail.com', password: 'n'),
			Jugador.build(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,9,23,0,0,0).toDate(), nombre: 'David', apellido: 'nalbandian', 
							dni: '87654323', email: 'nalbandian@gmail.com', password: 'n'),
			Jugador.build(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,7,4,0,0,0).toDate(), nombre: 'Charlie', apellido: 'Berlocq', 
							dni: '87654324', email: 'berlocq@gmail.com', password: 'b'),
			Jugador.build(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Carlos', apellido: 'Moya', 
							dni: '87654325', email: 'moya@gmail.com', password: 'm')
		]

		def equipo = setUpEquipo(jugadores, elChasqui)
		
		return equipo
	}

	private Equipo setUpEquipo(jugadores, club){
		def equipo = new Equipo(club: club, categoria: Categoria.build(nombre: '+25', sexo: 'M'), jerarquia: 'A', capitan: jugadores.get(0), estaConfirmado: false)
		
		def itemsListaBuenaFe = new TreeSet()
		
		jugadores.eachWithIndex { jugador, index ->
										itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index))
										jugador.save(failOnError:true)
								}

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;

		equipo.save(failOnError: true, flush: true)

		club.equipos.add(equipo)
				
		return equipo
	}
	
	def Equipo crearEquipoNahuel(){
		def nahuel = crearClubNahuel()

		def jugadores = [
			Jugador.build(club: nahuel, sexo: 'M', nacimiento: new DateTime(1990,1,8,0,0,0).toDate(), nombre: 'Robin', apellido: 'Soderling', 
							dni: '87654637', email: 'soderling@gmail.com'),
			Jugador.build(club: nahuel, sexo: 'M', nacimiento: new DateTime(1989,9,23,0,0,0).toDate(), nombre: 'Pete', apellido: 'Sampras', 
							dni: '87659463', email: 'sampras@gmail.com'),
			Jugador.build(club: nahuel, sexo: 'M', nacimiento: new DateTime(1989,7,4,0,0,0).toDate(), nombre: 'Andre', apellido: 'Agassi', 
							dni: '32154324', email: 'agassi@gmail.com'),
			Jugador.build(club: nahuel, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Dominic', apellido: 'Hrbati', 
							dni: '32154325', email: 'hrbati@gmail.com')
		]

		def equipo = setUpEquipo(jugadores, nahuel)
		
		return equipo
	}
}