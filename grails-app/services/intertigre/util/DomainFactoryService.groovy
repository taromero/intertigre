package intertigre.util

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime

import intertigre.domain.Categoria
import intertigre.domain.Club
import intertigre.domain.Equipo
import intertigre.domain.Fecha;
import intertigre.domain.ItemListaBuenaFe
import intertigre.domain.Jugador


class DomainFactoryService {

	def List<Categoria> crearCategorias(){
		def categorias = [
			[nombre: '+19', edadLimiteInferior: 19, edadLimiteSuperior: 24, sexo: 'M'] as Categoria,
			[nombre: '+19', edadLimiteInferior: 19, edadLimiteSuperior: 24, sexo: 'F'] as Categoria,
			[nombre: '+25', edadLimiteInferior: 25, edadLimiteSuperior: 35, sexo: 'M'] as Categoria,
			[nombre: '+25', edadLimiteInferior: 25, edadLimiteSuperior: 34, sexo: 'F'] as Categoria
		]
		for(categoria in categorias){
			Categoria.find{nombre == categoria.nombre && sexo == categoria.sexo} ?: categoria.save()
		}
	}

	def void crearClubes(){
		new Club(nombre: 'Buenos Aires Rowing', localidad: 'Tigre', direccion: 'Mitre 978',
				email: 'buenosairesrowing@gmail.com', telefono: 1432, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [10, 18, 12]).save()
		new Club(nombre: 'Nahuel Rowing', localidad: 'Tigre', direccion: 'Lavalle 978',
				email: 'nahuel@gmail.com', telefono: 14572, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [14, 10, 12]).save()
		new Club(nombre: 'Ecosol', localidad: 'Rincon de Milberg', direccion: 'Milberg 543',
				email: 'ecosol@gmail.com', telefono: 57572, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [22, 20, 9]).save()
		new Club(nombre: 'Hacoaj', localidad: 'Tigre', direccion: 'Avenida de las Americas 943',
				email: 'hacoaj@gmail.com', telefono: 8572, triosDeCanchasDisponibles: 2, 
				horariosPreferidosParaLocal: [10, 18, 12]).save()
		new Club(nombre: 'Tigre Set', localidad: 'Tigre', direccion: 'San Martin 6547',
				email: 'tigreset@gmail.com', telefono: 8942, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [20, 22]).save()
		new Club(nombre: 'San fernando', localidad: 'San Fernando', direccion: 'Che Guevara 437',
				email: 'sanfernando@gmail.com', telefono: 8942, triosDeCanchasDisponibles: 2, 
				horariosPreferidosParaLocal: [10, 18, 12]).save(flush: true, failOnError: true)
	}

	def void crearXCantidadEquiposDeCategoria(Integer cantidad, Categoria categoria = null){
		List<Equipo> equipos = new ArrayList<Equipo>()
		def clubes = Club.findAll()
		def jerarquia = 'A'
		def clubIndex = 0
		for(i in 1..cantidad){
			def club = clubes.get(clubIndex)
			def equipo = new Equipo(categoria: categoria, club: club, jerarquia: jerarquia)
			club.equipos.add(equipo)
			equipo.save()
			clubIndex++
			if(clubIndex == (clubes.size()-1)){ 
				clubIndex = 0 
				jerarquia++
			}
		}
		clubes.each{ it.save(flush:true) }
	}

	def List<Equipo> crearXCantidadEquiposDeCategoriaDeXClubesDistintos(Integer cantidadEquipos, Categoria categoria = null, Integer cantClubes = 1){
		categoria = categoria ?: Categoria.build()
		List<Equipo> equipos = new ArrayList<Equipo>()
		def clubes = []
		cantClubes.times { clubes.add(Club.build(triosDeCanchasDisponibles: 1)) }
		def clubIndex = 0
		for(int i = 0; i < cantidadEquipos; i++) {
			def club = clubes.get(clubIndex)
			def equipo = new Equipo(categoria: categoria, club: club, jerarquia: getJerarquiaDisponibleParaElClubCategoria(club, categoria))
			equipos.add(equipo)
			club.equipos.add(equipo)
			equipo.save()
			clubIndex++
			if(clubIndex == cantClubes){ clubIndex = 0 }
		}
		return equipos
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
	
	def Equipo crearEquipoMas19MCanotto(jugadores = null){
		def canotto = crearClubCanotto()
		if(jugadores == null) {
			jugadores = [
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Tomas', apellido: 'Romero', 
							dni: '12345678', username: 'canotto90@gmail.com',
							urlImagen: 'http://a7.sphotos.ak.fbcdn.net/hphotos-ak-snc6/251940_3763810087241_391676411_n.jpg'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1987,8,23,0,0,0).toDate(), nombre: 'Juan Martin', apellido: 'Del Potro', 
								dni: '12345676', username: 'delpotro@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,9,14,0,0,0).toDate(), nombre: 'Roger', apellido: 'Federer', 
								dni: '12345675', username: 'federer@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Chucho', apellido: 'Acasusso', 
								dni: '12345674', username: 'acasusso@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1989,6,27,0,0,0).toDate(), nombre: 'Guillermo', apellido: 'Cañas', 
								dni: '12345673', username: 'canas@gmail.com')
			]
			jugadores.each { it.password = it.nombre.toLowerCase().getAt(0) }
			
		}
		def equipo = setUpEquipo(jugadores, canotto)
		return equipo
	}
	
	def Equipo crearEquipoBMas19MCanotto(jugadores = null){
		def canotto = crearClubCanotto()
		if(jugadores == null) {
			jugadores = [
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'TomasB', apellido: 'RomeroB',
							dni: '1111', username: 'canotto90B@gmail.com',
							urlImagen: 'http://a7.sphotos.ak.fbcdn.net/hphotos-ak-snc6/251940_3763810087241_391676411_n.jpg'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1987,8,23,0,0,0).toDate(), nombre: 'Juan MartinB', apellido: 'Del PotroB',
								dni: '2222', username: 'delpotroB@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,9,14,0,0,0).toDate(), nombre: 'RogerB', apellido: 'FedererB',
								dni: '3333', username: 'federerB@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'ChuchoB', apellido: 'AcasussoB',
								dni: '4444', username: 'acasussoB@gmail.com'),
				Jugador.build(club: canotto, sexo: 'M', nacimiento: new DateTime(1989,6,27,0,0,0).toDate(), nombre: 'GuillermoB', apellido: 'CañasB',
								dni: '5555', username: 'canasB@gmail.com')
			]
			jugadores.each { it.password = it.nombre.toLowerCase().getAt(0) }
			
		}
		def equipo = setUpEquipo(jugadores, canotto, 'B')
		return equipo
	}

	def List<Jugador> crearJugadoresLibresNahuel(List dnis) {
		def nahuel = crearClubNahuel()
		def jugadores
		if(dnis != null) {
			jugadores = [
					Jugador.build(apellido: 'A', nombre: 'A', dni: dnis.get(0), club: nahuel),
					Jugador.build(apellido: 'B', nombre: 'B', dni: dnis.get(1), club: nahuel),
					Jugador.build(apellido: 'C', nombre: 'C', dni: dnis.get(3), club: nahuel),
					Jugador.build(apellido: 'D', nombre: 'D', dni: dnis.get(4), club: nahuel),
					Jugador.build(apellido: 'E', nombre: 'E', dni: dnis.get(2), club: nahuel)
				]
		} else {
			jugadores = [
					Jugador.build(apellido: 'A', nombre: 'A', club: nahuel),
					Jugador.build(apellido: 'B', nombre: 'B', club: nahuel),
					Jugador.build(apellido: 'C', nombre: 'C', club: nahuel),
					Jugador.build(apellido: 'D', nombre: 'D', club: nahuel),
					Jugador.build(apellido: 'E', nombre: 'E', club: nahuel)
				]
		}
//		jugadores.each { canotto.jugadores.add(it) }
		return jugadores
	}
	
	def List<Jugador> crearJugadoresLibresCanotto(List dnis) {
		def canotto = crearClubCanotto()
		def jugadores
		if(dnis != null) {
			jugadores = [
					Jugador.build(apellido: 'Djokovic', nombre: 'Novak', dni: dnis.get(0), club: canotto),
					Jugador.build(apellido: 'Robredo', nombre: 'Tommy', dni: dnis.get(1), club: canotto),
					Jugador.build(apellido: 'Ferrer', nombre: 'David', dni: dnis.get(3), club: canotto),
					Jugador.build(apellido: 'Almagro', nombre: 'Nicolas', dni: dnis.get(4), club: canotto),
					Jugador.build(apellido: 'Haas', nombre: 'Tommy', dni: dnis.get(2), club: canotto)
				]
		} else {
			jugadores = [
					Jugador.build(apellido: 'Djokovic', nombre: 'Novak', club: canotto),
					Jugador.build(apellido: 'Robredo', nombre: 'Tommy', club: canotto),
					Jugador.build(apellido: 'Ferrer', nombre: 'David', club: canotto),
					Jugador.build(apellido: 'Almagro', nombre: 'Nicolas', club: canotto),
					Jugador.build(apellido: 'Haas', nombre: 'Tommy', club: canotto)
				]
		}
//		jugadores.each { canotto.jugadores.add(it) }
		return jugadores
	}
	
	def List<Jugador> crearJugadorasMujeresLibresCanotto(List dnis) {
		def canotto = crearClubCanotto()
		def jugadoras
		if(dnis != null) {
			jugadoras = [
					Jugador.build(apellido: 'Dulko', nombre: 'Gisela', dni: dnis.get(0), club: canotto, sexo: 'F'),
					Jugador.build(apellido: 'Sabatini', nombre: 'Gabriela', dni: dnis.get(1), club: canotto, sexo: 'F'),
					Jugador.build(apellido: 'Williams', nombre: 'Serena', dni: dnis.get(3), club: canotto, sexo: 'F'),
					Jugador.build(apellido: 'Williams', nombre: 'Venus', dni: dnis.get(4), club: canotto, sexo: 'F'),
					Jugador.build(apellido: 'Henin', nombre: 'Justin', dni: dnis.get(2), club: canotto, sexo: 'F')
				]
		} else {
			jugadoras = [
				Jugador.build(apellido: 'Dulko', nombre: 'Gisela', club: canotto, sexo: 'F'),
				Jugador.build(apellido: 'Sabatini', nombre: 'Gabriela', club: canotto, sexo: 'F'),
				Jugador.build(apellido: 'Williams', nombre: 'Serena', club: canotto, sexo: 'F'),
				Jugador.build(apellido: 'Williams', nombre: 'Venus', club: canotto, sexo: 'F'),
				Jugador.build(apellido: 'Henin', nombre: 'Justin', club: canotto, sexo: 'F')
			]
		}
		return jugadoras
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
	
	private Equipo setUpEquipo(jugadores, Club club, String jerarquia = null){
		def equipo = new Equipo(club: club, categoria: Categoria.buildLazy(nombre: '+19', sexo: 'M'), jerarquia: jerarquia ?: 'A', 
									capitan: jugadores.get(0), estaConfirmado: false)
		
		def itemsListaBuenaFe = new TreeSet()
		
		jugadores.eachWithIndex { jugador, index ->
										itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index))
										jugador.save(failOnError:true)
								}

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;

		club.equipos.add(equipo)
		
		equipo.save(failOnError: true)
		
		return equipo
	}
	
	def Club crearClubCanotto(){
		def club = Club.findAll().find { it.nombre == 'Canottieri' && it.localidad == 'Tigre' }
		return  club ?: new Club(nombre: 'Canottieri', localidad: 'Tigre', direccion: 'Mitre 123',
								email: 'canottieri@gmail.com', telefono: 1234, triosDeCanchasDisponibles: 1, 
								horariosPreferidosParaLocal: [10, 18, 12]).save(failOnError: true, flush:true)
	}
	
	def Club crearClubElChasqui(){
		def club = Club.findAll().find { it.nombre == 'El Chasqui' && it.localidad == 'El Talar' }
		return club ?: new Club(nombre: 'El Chasqui', localidad: 'El Talar', direccion: 'Belgrano 467',
								email: 'elchasqui@gmail.com', telefono: 9085, triosDeCanchasDisponibles: 1, 
								horariosPreferidosParaLocal: [10, 18, 12]).save(failOnError: true, flush:true)
	}
	
	def Club crearClubNahuel(){
		def club = Club.findAll().find { it.nombre == 'Nahuel' && it.localidad == 'Tigre' }
		return club ?: new Club(nombre: 'Nahuel', localidad: 'Tigre', direccion: 'Lavalle 467',
				email: 'nahuel@gmail.com', telefono: 9084, triosDeCanchasDisponibles: 1).save(failOnError: true, flush:true)
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
	
	def List<Jugador> crearXJugadores(Integer cantJugadores){
		def jugadores = new ArrayList<Jugador>()
		for(i in 0..(cantJugadores-1)){
			def jugador = Jugador.build()
			jugadores.add(jugador)
		}
		return jugadores
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
	
	public static Fecha createFecha(Equipo equipoLocal, Equipo equipoVisitante, Date fechaDeJuego, fechaReprogramacion = null){
		Fecha fecha = new Fecha(equipoLocal: equipoLocal, equipoVisitante: equipoVisitante,
								fechaDeJuego: fechaDeJuego, fechaSubidaResultado: new Date(), categoria: Categoria.build(),
								fechaReprogramacion: fechaReprogramacion)
		equipoLocal.fechasLocal.add(fecha)
		equipoVisitante.fechasVisitante.add(fecha)
		fecha.save(failOnError: true)
		equipoLocal.save()
		equipoVisitante.save()
		return fecha
	}
	
	public static List<Fecha> createFechasParaReprogramar(Date fechaDeJuego, int cantFechas) {
		def fechas = []
		Club club = Club.build(horariosPreferidosParaLocal: [18, 20, 22], triosDeCanchasDisponibles: 1)
		cantFechas.times {
			def equipoLocal = Equipo.build(club: club)
			def equipoVisitante = Equipo.build(club: club)
			def fechaReprogramacion = new DateTime(fechaDeJuego).plusWeeks(1).toDate()
			fechas.add(createFecha(equipoLocal, equipoVisitante, fechaDeJuego, fechaReprogramacion))
		}
		return fechas
	}
}
