package intertigre.util

import org.joda.time.DateTime

import intertigre.domain.Categoria
import intertigre.domain.Club
import intertigre.domain.Equipo
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
		new Club(nombre: 'Canottieri', localidad: 'Tigre', direccion: 'Mitre 123',
				email: 'canottieri@gmail.com', telefono: 1234, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [10, 18, 12]).save()
		new Club(nombre: 'El Chasqui', localidad: 'El Talar', direccion: 'Belgrano 467',
				email: 'elchasqui@gmail.com', telefono: 9085, triosDeCanchasDisponibles: 1, 
				horariosPreferidosParaLocal: [10, 18, 12]).save()
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
				horariosPreferidosParaLocal: [10, 18, 12]).save()
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

	def Equipo crearEquipoMas19MCanotto(){
		def canotto = Club.findByNombre('Canottieri')

		def jugadores = [
			new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Tomas', apellido: 'Romero', 
							dni: '12345678', email: 'canotto90@gmail.com', password: 't', 
							urlImagen: 'http://a7.sphotos.ak.fbcdn.net/hphotos-ak-snc6/251940_3763810087241_391676411_n.jpg'),
			new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1987,8,23,0,0,0).toDate(), nombre: 'Juan Martin', apellido: 'Del Potro', 
							dni: '12345676', email: 'delpotro@gmail.com'),
			new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,9,14,0,0,0).toDate(), nombre: 'Roger', apellido: 'Federer', 
							dni: '12345675', email: 'federer@gmail.com'),
			new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Chucho', apellido: 'Acasusso', 
							dni: '12345674', email: 'acasusso@gmail.com'),
			new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1989,6,27,0,0,0).toDate(), nombre: 'Guillermo', apellido: 'Cañas', 
							dni: '12345673', email: 'canas@gmail.com')
		]

		new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Dominic', apellido: 'Hrbati',
						dni: '46877843', email: 'hrbati@gmail.com').save()
		new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Pete', apellido: 'Sampras',
						dni: '46877844', email: 'sampras@gmail.com').save()
		new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Andre', apellido: 'Agassi',
						dni: '46877845', email: 'agassi@gmail.com').save()
		new Jugador(club: canotto, sexo: 'M', nacimiento: new DateTime(1990,3,3,0,0,0).toDate(), nombre: 'Juan Carlos', apellido: 'Ferrero',
						dni: '46877846', email: 'ferrero@gmail.com').save()

		jugadores.each{it.save(flush:true)}

		def equipo = new Equipo(club: canotto, categoria: Categoria.find{nombre ==  '+25' && sexo == 'M'}, jerarquia: 'Z', capitan: jugadores.get(0), estaConfirmado: false, estaBienFormado: true)

		canotto.equipos.add(equipo)

		def itemsListaBuenaFe = new TreeSet()

		jugadores.eachWithIndex { jugador, index -> itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index)) }
		
		itemsListaBuenaFe.each { it.jugador.itemsListasBuenaFe.add(it) }

		itemsListaBuenaFe.each { it.save() }

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;
		
		equipo.save()
		
		return equipo
	}

	def Equipo crearEquipoMas19MElChasqui(){
		def elChasqui = Club.findByNombre('El Chasqui')

		def jugadores = [
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,1,8,0,0,0).toDate(), nombre: 'Rafael', apellido: 'Nadal', 
							dni: '87654321', email: 'nadal@gmail.com', password: 'n'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,9,23,0,0,0).toDate(), nombre: 'David', apellido: 'nalbandian', 
							dni: '87654323', email: 'nalbandian@gmail.com'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1989,7,4,0,0,0).toDate(), nombre: 'Charlie', apellido: 'Berlocq', 
							dni: '87654324', email: 'berlocq@gmail.com'),
			new Jugador(club: elChasqui, sexo: 'M', nacimiento: new DateTime(1990,2,15,0,0,0).toDate(), nombre: 'Carlos', apellido: 'Moya', 
							dni: '87654325', email: 'moya@gmail.com', password: 'm')
		]

		jugadores.each{it.save(flush:true)}

		def equipo = new Equipo(club: elChasqui, categoria: Categoria.find{nombre ==  '+25' && sexo == 'M'}, jerarquia: 'Z', capitan: jugadores.get(0), estaConfirmado: false, estaBienFormado: true)

		elChasqui.equipos.add(equipo)

		def itemsListaBuenaFe = new TreeSet()

		jugadores.eachWithIndex { jugador, index -> itemsListaBuenaFe.add(new ItemListaBuenaFe(equipo: equipo, jugador: jugador, posicion: index)) }

		itemsListaBuenaFe.each { it.jugador.itemsListasBuenaFe.add(it) }

		itemsListaBuenaFe.each { it.save() }

		equipo.itemsListaBuenaFe = itemsListaBuenaFe;
		
		equipo.save()
		
		return equipo
	}
}
