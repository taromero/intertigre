package intertigre.domain

import intertigre.security.SecRole
import intertigre.security.SecUserSecRole
import grails.plugins.springsecurity.Secured;


class JugadorController extends BaseDomainController{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }
	
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def regenerarPassword(){
		def jugador = Jugador.find{ username == params.username }
		if(jugador.password != params.password){
			flash.message = 'No se pudo cambiar la contraseña por un problema en el link'
			redirect(uri: '/')
		}
		def randomPassword = obtenerRandomPassword()
		jugador.password = randomPassword
		jugador.save(flush:true)
		sendMail {
			to jugador.email
			subject "Recuperacion password InterTigres"
			body 'Se password ha sido cambiado a ' + randomPassword +
					'\n Ingresa con ese password y cambialo en la pantalla de edicion de datos personales.'
		  }
		flash.message = 'En breve te llegara un mail a tu casilla con un nuevo password'
		redirect(uri: '/')
	}
	
	@Secured(['ROLE_CAPITAN_EQUIPO'])
    def list() {
        def loggedUser = getLoggedUser()
        [jugadorInstanceList: Jugador.findAllByClub(loggedUser.club), jugadorInstanceTotal: Jugador.count()]
    }

    @Secured(['ROLE_CAPITAN_EQUIPO'])
    def create() {
        [jugadorInstance: new Jugador(params)]
    }

    @Secured(['ROLE_CAPITAN_EQUIPO'])
    def save() {
		params.username = params.username.toLowerCase()
		
        def jugadorInstance = new Jugador(params)
		if(params.password == null || params.password == ''){
			flash.message = 'El password no puede estar vacio'
			render(view: "create", model: [jugadorInstance: jugadorInstance])
			return
		}
		if(params.password != params.passwordConfirm){
			flash.message = 'Los passwords no coinciden'
			render(view: "create", model: [jugadorInstance: jugadorInstance])
			return
		}
		if(Jugador.find{ username == params.username } != null && Jugador.find{ username == params.username }.club != getLoggedUser().club ){
			flash.message = 'Ya existe un usuario con ese mail'
			render(view: "create", model: [jugadorInstance: jugadorInstance])
			return
		}
		jugadorInstance.club = getLoggedUser().club
		
		if (!jugadorInstance.save(flush: true, failOnError: true)) {
			render(view: "create", model: [jugadorInstance: jugadorInstance])
			return
		}
		
		this.asignarRol(jugadorInstance, params.role)
		
		flash.message = message(code: 'default.created.message', args: [message(code: 'jugador.label', default: 'Jugador'), jugadorInstance.id])
        redirect(action: "show", id: jugadorInstance.id)
    }
	
	@Secured(['ROLE_CAPITAN_EQUIPO'])
    def show() {
        def jugadorInstance = Jugador.get(params.id) ?: getLoggedUser()
        this.checkIsUsuarioEsDelClub(Club.get(jugadorInstance.club.id))

        if (!jugadorInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'jugador.label', default: 'Jugador'), params.id])
            redirect(action: "list")
            return
        }

        [jugadorInstance: jugadorInstance]
    }

	@Secured(['ROLE_CAPITAN_EQUIPO'])
    def edit() {
        def jugadorInstance = Jugador.get(params.id)
        this.checkIsUsuarioEsDelClub(Club.get(jugadorInstance.club.id))

        if (!jugadorInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'jugador.label', default: 'Jugador'), params.id])
            redirect(action: "list")
            return
        }

        [jugadorInstance: jugadorInstance]
    }

    @Secured(['ROLE_JUGADOR'])
    def update() {
		params.username = params.username.toLowerCase()
        def jugadorInstance = Jugador.get(params.id)
		
		if(!esAdmin() && jugadorInstance != getLoggedUser()){
			throw new RuntimeException('No tenes permisos para editar a este jugador')
		}
		if(params.password == null || params.password == ''){
			flash.message = 'El password no puede estar vacio'
			render(view: "edit", model: [jugadorInstance: jugadorInstance])
			return
		}
		if(params.password != params.passwordConfirm){
			flash.message = 'Los passwords no coinciden'
			render(view: "edit", model: [jugadorInstance: jugadorInstance])
			return
		}
		if(Jugador.find{ username == params.username } != null && Jugador.find{ username == params.username }.club != getLoggedUser().club ){
			flash.message = 'Ya existe un usuario con ese mail'
			render(view: "edit", model: [jugadorInstance: jugadorInstance])
			return
		}
        if (!jugadorInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'jugador.label', default: 'Jugador'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (jugadorInstance.version > version) {
                jugadorInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'jugador.label', default: 'Jugador')] as Object[],
                          "Another user has updated this Jugador while you were editing")
                render(view: "edit", model: [jugadorInstance: jugadorInstance])
                return
            }
        }

		if(esAdmin()){
			this.asignarRol(jugadorInstance, params.role)
		}else{
			params.role = jugadorInstance.role
		}
		
        jugadorInstance.properties = params

        if (!jugadorInstance.save(flush: true)) {
            render(view: "edit", model: [jugadorInstance: jugadorInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'jugador.label', default: 'Jugador'), jugadorInstance.id])
        redirect(action: "show", id: jugadorInstance.id)
    }

	def String obtenerRandomPassword(){
		def pool = ['a'..'z','A'..'Z',0..9,'_'].flatten()
		Random rand = new Random(System.currentTimeMillis())
		
		def passChars = (0..10).collect { pool[rand.nextInt(pool.size())] }
		def password = passChars.join()
	}
	
	private asignarRol(jugador, rol){
		def esAdmin = esAdmin()
		SecUserSecRole.findAll().findAll { it.secUser == jugador }.each { it.delete() }
		
		rol = (rol == null) ? jugador.role : rol
		
		def roleString
		switch (rol) {
			case 'Administrador':
				roleString = 'ROLE_ADMIN'
				break
			case 'Capitan club':
				roleString = 'ROLE_CAPITAN_CLUB'
				break
			case 'Capitan equipo':
				roleString = 'ROLE_CAPITAN_EQUIPO'
				break
			case 'Jugador nomal':
				roleString = 'ROLE_JUGADOR'
				break
			default:
				roleString = 'ROLE_JUGADOR'
				break
		}
		
		if(rol == 'Jugador normal'){
			def role = SecRole.find { authority == 'ROLE_JUGADOR' }
			SecUserSecRole.create jugador, role
		}else{
			if(esAdmin){
				def role = SecRole.find { authority == roleString }
				SecUserSecRole.create jugador, role
			}else{
				throw new RuntimeException('Estas queriendo asignar roles, y no esta permitido')
			}
		}
		jugador.role = rol
		if (!jugador.save(flush: true)) {
            render(view: "edit", model: [jugadorInstance: jugador])
            return
        }
	}
}
