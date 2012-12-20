package intertigre.controllers

import intertigre.domain.Jugador
import intertigre.domain.JugadorController
import intertigre.security.SecUserSecRole

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime

class JugadorControllerSpec extends BaseControllerSpec{
	
	JugadorController controller = new JugadorController()
	
	def setup(){
		controller.metaClass.esAdmin { controller.getLoggedUser().getAuthorities()*.authority.contains('ROLE_ADMIN') }
	}
	
	def 'editar informacion de un jugador, siendo administrador'(){
		given: 'un administrador loggeado y un jugador de un club y'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't',
										role: 'Capitan equipo', dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, roleAdmin
			Jugador jugador = Jugador.build(username: usernameOld, password: 'p',
										role: roleOld, dni: '2', club: domainFactoryService.crearClubElChasqui())
			SecUserSecRole.create jugador, rolePostaOld
		when: 'quiero editar los datos del jugador'
			controller.params.id = jugador.id
			controller.params.username = usernameNew
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.nacimiento = '27-03-1990'
			controller.params.role = roleNew
			controller.params.dni = '3'
			controller.update()
		then: 'se debe actualizar el jugador'
			jugador.username == usernameNew
			jugador.role == roleNew || roleNew == null || roleNew == ''
			jugador.dni == '3'
			jugador.getAuthority() == rolePostaNew
			new DateTime(jugador.nacimiento).getDayOfMonth() == 27
		where:
			usernameOld        | usernameNew      | roleOld          | roleNew        | rolePostaNew    | rolePostaOld
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Jugador normal' | ''             | roleJugador     |  roleJugador
//			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Jugador normal' | null           | roleJugador     |  roleJugador
//			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan equipo' | 'Capitan club' | roleCapitanClub | roleCapitanEquipo
	}

	def 'caso no permitido, editar informacion de un jugador'(){
		given: 'un usuario loggeado que no es administrador y un jugador de un club y'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't',
										role: 'Capitan equipo', dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, roleLoggedUser
			def jugador = Jugador.build(username: usernameOld, password: 'p',
										role: roleOld, dni: '2', club: domainFactoryService.crearClubElChasqui())
			SecUserSecRole.create jugador, roleJugador
		when: 'quiero editar los datos del jugador'
			controller.params.id = jugador.id
			controller.params.username = usernameNew
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.role = roleNew
			controller.params.dni = '2'
			controller.update()
		then: 'no se debe actualizar al jugador, y se debe lanzar una excepcion'
			jugador.username == usernameOld
			jugador.role == roleOld
			jugador.getAuthority() == roleJugador
			thrown(Exception)
		where:
			usernameOld        | usernameNew      | roleOld          | roleNew        | rolePostaNew      | roleLoggedUser
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan equipo' | ''             | roleCapitanEquipo | roleCapitanClub
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan equipo' | null           | roleCapitanEquipo | roleCapitanClub
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan equipo' | 'Capitan club' | roleCapitanClub   |  roleCapitanClub
	}
	
	def 'el jugador loggeado edita su informacion'(){
		given: 'un usuario loggeado con cualquier rol'
			loggedUser = Jugador.build(username: usernameOld, password: 't',
										role: roleOld, dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, rolePostaOld
		when: 'quiere editar sus datos'
			controller.params.id = loggedUser.id
			controller.params.username = usernameNew
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.role = roleNew
			controller.params.dni = '1'
			controller.update()
		then: 'se deben actualizar sus datos (solo puede cambiar su rol si es administrador)'
			loggedUser.username == usernameNew
			loggedUser.role == roleNew || roleNew == null || roleNew == ''
			loggedUser.getAuthority() == rolePostaNew
		where:
			usernameOld        | usernameNew      | roleOld          | roleNew        | rolePostaOld      | rolePostaNew
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Jugador normal' | ''             |  roleJugador      | roleJugador
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Jugador normal' | null           |  roleJugador      | roleJugador
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Administrador' | 'Capitan club' |  roleAdmin        | roleCapitanClub
	}
	
	def 'caso no permitido, el jugador loggeado edita su informacion queriendo cambiar de rol'(){
		given: 'un usuario loggeado con cualquier rol menos el de administrador'
			loggedUser = Jugador.build(username: usernameOld, password: 't',
										role: roleOld, dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, rolePostaOld
		when: 'quiere editar sus datos, cambiando de rol'
			controller.params.id = loggedUser.id
			controller.params.username = usernameNew
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.role = roleNew
			controller.params.dni = '1'
			controller.update()
		then: 'no se debe cambiar el rol del jugador'
			loggedUser.getAuthority() == rolePostaOld
			loggedUser.role == roleOld
		where:
			usernameOld        | usernameNew      | roleOld          | roleNew          | rolePostaOld      | rolePostaNew
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan equipo' | 'Capitan club'   | roleCapitanEquipo | roleCapitanClub
			'pirulo@gmail.com' | 'pepe@gmail.com' | 'Capitan club'   | 'Capitan equipo' | roleCapitanEquipo | roleCapitanEquipo
	}
	def 'crear un jugador'(){
		given: 'un usuario loggeado'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't', dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, roleLoggedUser
		when: 'crea un jugador nuevo'
			controller.params.nacimiento = '03-03-1990'
			controller.params.telefono = 'a'
			controller.params.nombre = 'a'
			controller.params.apellido = 'a'
			controller.params.sexo = 'M'
			controller.params.username = username
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.role = role
			controller.params.dni = '2'
			controller.save()
			def jugadores = Jugador.findAll().find { it.dni == '2' }
			def jugadorNuevo = Jugador.findAll().find { it.dni == '2' }
		then: 'se debe crear el jugador'
			jugadorNuevo.username == usernameCorregido
			jugadorNuevo.password != 't'
			jugadorNuevo.password == springSecurityService.encodePassword('t')
			jugadorNuevo.role == ([null, ''].contains(role) ? 'Jugador normal' : role)
			jugadorNuevo.dni == '2'
			jugadorNuevo.authority == rolePosta
			new DateTime(jugadorNuevo.nacimiento).getDayOfMonth() == 3
		where:
			username         | usernameCorregido | role              | rolePosta         | roleLoggedUser
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Jugador normal'  | roleJugador       | roleCapitanEquipo
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Jugador normal'  | roleJugador       | roleCapitanClub
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Jugador normal'  | roleJugador       | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan equipo'  | roleCapitanEquipo | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan club'    | roleCapitanClub   | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Administrador'   | roleAdmin         | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | ''                | roleJugador       | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | null              | roleJugador       | roleAdmin
			'Pepe@gmail.com' | 'pepe@gmail.com'  | null              | roleJugador       | roleCapitanEquipo
			'Pepe@gmail.com' | 'pepe@gmail.com'  | null              | roleJugador       | roleCapitanClub
	}
	
	
	
	def 'caso no permitido, crear un jugador'(){
		given: 'un usuario loggeado'
			loggedUser = Jugador.build(username: 'canotto90@gmail.com', password: 't',
										role: role, dni: '1', club: domainFactoryService.crearClubCanotto())
			SecUserSecRole.create loggedUser, roleLoggedUser
		when: 'crea un jugador nuevo'
			controller.params.nacimiento = '03-03-1990'
			controller.params.telefono = 'a'
			controller.params.nombre = 'a'
			controller.params.apellido = 'a'
			controller.params.sexo = 'M'
			controller.params.username = username
			controller.params.password = 't'
			controller.params.passwordConfirm = 't'
			controller.params.role = role
			controller.params.dni = '2'
			controller.save()
			def jugadorNuevo = Jugador.get(2)
		then: 'se debe crear el jugador'
			jugadorNuevo == null
			Exception ex = thrown(Exception)
			ex.message == 'Estas queriendo asignar roles, y no esta permitido'
		where:
			username         | usernameCorregido | role              | roleLoggedUser
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan equipo'  | roleCapitanEquipo
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan equipo'  | roleCapitanClub
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan club'    | roleCapitanEquipo
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Capitan club'    | roleCapitanClub
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Administrador'   | roleCapitanEquipo
			'Pepe@gmail.com' | 'pepe@gmail.com'  | 'Administrador'   | roleCapitanClub
	}
	
}
