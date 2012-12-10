package intertigre.domain

import grails.plugins.springsecurity.Secured;
import intertigre.security.SecRole;
import intertigre.services.FixtureService;

import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_CAPITAN_EQUIPO'])
class FechaController extends BaseDomainController{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	FixtureService fixtureService
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.sort = 'fechaDeJuego'
        def loggedUser = springSecurityService.currentUser
        def equipos = loggedUser?.getAuthorities()?.contains(SecRole.findByAuthority('ROLE_ADMIN')) ? loggedUser?.club?.equipos : loggedUser?.getEquipos()
        def equipoId = params?.equipo?.id != null && params?.equipo?.id != 'null' ? params?.equipo?.id as Long : null
        equipoId = equipoId== null ? equipos.get(0).id : equipoId 
        def fechas = equipoId != null ? Fecha.findAll{ equipoLocal.id == equipoId || equipoVisitante.id == equipoId} : Fecha.list(params)
        def fechasSize = equipoId != null ? fechas.size() : Fecha.count()
        fechas.sort(true){ it.fechaDeJuego }
        [fechaInstanceList: fechas, fechaInstanceTotal: fechasSize, equipos: equipos, idEquipoSeleccionado: equipoId]
    }

	@Secured(['ROLE_ADMIN'])
    def create() {
        [fechaInstance: new Fecha(params)]
    }
	
	def createPartido(){
		def fecha = Fecha.findById(params.id)
		redirectIfNotAllowedEdit(fecha)
		getModelForCreatePartido(fecha)
	}
	
	private Map getModelForCreatePartido(fecha){
		return [fecha: fecha, jugadoresLocales: Equipo.findById(fecha.equipoLocal.id).getJugadores() ,
					jugadoresVisitantes: Equipo.findById(fecha.equipoVisitante.id).getJugadores(), fechaId: params.fechaId,
					equipos: [fecha.equipoLocal, fecha.equipoVisitante]]
	}

	def savePartidos(){
		def fecha = Fecha.get(params.id)
		redirectIfNotAllowedEdit(fecha)
		
		def single1Set1 = new Sett(gamesGanador: params.single1.primerSet.gamesGanador, gamesPerdedor: params.single1.primerSet.gamesPerdedor)
		def single1Set2 = new Sett(gamesGanador: params.single1.segundoSet.gamesGanador, gamesPerdedor: params.single1.segundoSet.gamesPerdedor)
		def single1Set3 = params.single1.tercerSet.gamesGanador != 'null' &&  params.single1.tercerSet.gamesPerdedor != 'null' ? 
							new Sett(gamesGanador: params.single1.tercerSet.gamesGanador, gamesPerdedor: params.single1.tercerSet.gamesPerdedor)
							: null
		def single1 = new Single(equipoGanador: Equipo.get(params.single1.equipoGanador.id), jugadorLocal: Jugador.get(params.single1.jugadorLocal.id),
								 jugadorVisitante: Jugador.get(params.single1.jugadorVisitante.id), primerSet: single1Set1, 
								 segundoSet: single1Set2, tercerSet: single1Set3, abandono: params.abandonoSingle1 ?: false)
		fecha.single1 = single1
		
		def single2Set1 = new Sett(gamesGanador: params.single2.primerSet.gamesGanador, gamesPerdedor: params.single2.primerSet.gamesPerdedor)
		def single2Set2 = new Sett(gamesGanador: params.single2.segundoSet.gamesGanador, gamesPerdedor: params.single2.segundoSet.gamesPerdedor)
		def single2Set3 = params.single2.tercerSet.gamesGanador != 'null' &&  params.single2.tercerSet.gamesPerdedor != 'null' ?
							new Sett(gamesGanador: params.single2.tercerSet.gamesGanador, gamesPerdedor: params.single2.tercerSet.gamesPerdedor)
							: null
		def single2 = new Single(equipoGanador: Equipo.get(params.single2.equipoGanador.id), jugadorLocal: Jugador.get(params.single2.jugadorLocal.id),
								 jugadorVisitante: Jugador.get(params.single2.jugadorVisitante.id), primerSet: single2Set1,
								 segundoSet: single2Set2, tercerSet: single2Set3, abandono: params.abandonoSingle2 ?: false)
		fecha.single2 = single2
		
		def dobleSet1 = new Sett(gamesGanador: params.doble.primerSet.gamesGanador, gamesPerdedor: params.doble.primerSet.gamesPerdedor)
		def dobleSet2 = new Sett(gamesGanador: params.doble.segundoSet.gamesGanador, gamesPerdedor: params.doble.segundoSet.gamesPerdedor)
		def dobleSet3 = params.doble.tercerSet.gamesGanador != 'null' &&  params.doble.tercerSet.gamesPerdedor != 'null' ?
							new Sett(gamesGanador: params.doble.tercerSet.gamesGanador, gamesPerdedor: params.doble.tercerSet.gamesPerdedor)
							: null
		def parejaLocal = new ParejaDoble(doblista1: Jugador.get(params.doble.parejaLocal.doblista1.id), 
											doblista2: Jugador.get(params.doble.parejaLocal.doblista2.id))
		def parejaVisitante = new ParejaDoble(doblista1: Jugador.get(params.doble.parejaVisitante.doblista1.id), 
											doblista2: Jugador.get(params.doble.parejaVisitante.doblista2.id))
		def doble = new Doble(equipoGanador: Equipo.get(params.doble.equipoGanador.id), parejaLocal: parejaLocal,
								 parejaVisitante: parejaVisitante, primerSet: dobleSet1,
								 segundoSet: dobleSet2, tercerSet: dobleSet3, abandono: params.abandonoDoble ?: false)
		fecha.doble = doble
		fecha.wo = params.wo
		fecha.fechaSubidaResultado = new Date()
		fecha.formacionIncorrectaLocal = fecha.verificarFormacionIncorrectaLocal()
		fecha.formacionIncorrectaVisitante = fecha.verificarFormacionIncorrectaVisitante()
		if(!fecha.save(flush:true)){
			render(view: 'createPartido', model: getModelForCreatePartido(fecha))
			return
		}
		redirect(action: "show", id: fecha.id)
	}
	
	@Secured(['ROLE_ADMIN'])
    def save() {
        def fechaInstance = new Fecha(params)
        if (!fechaInstance.save(flush: true)) {
            render(view: "create", model: [fechaInstance: fechaInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'fecha.label', default: 'Fecha'), fechaInstance.id])
        redirect(action: "show", id: fechaInstance.id)
    }

    def show() {
        def fechaInstance = Fecha.get(params.id)
        if (!fechaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "list")
            return
        }

        [fechaInstance: fechaInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def edit() {
        def fechaInstance = Fecha.get(params.id)
		redirectIfNotAllowedEdit(fechaInstance)
        if (!fechaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "list")
            return
        }

        [fechaInstance: fechaInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def update() {
        def fechaInstance = Fecha.get(params.id)
		
        if (!fechaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (fechaInstance.version > version) {
                fechaInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'fecha.label', default: 'Fecha')] as Object[],
                          "Another user has updated this Fecha while you were editing")
                render(view: "edit", model: [fechaInstance: fechaInstance])
                return
            }
        }

        fechaInstance.properties = params

        if (!fechaInstance.save(flush: true)) {
            render(view: "edit", model: [fechaInstance: fechaInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fecha.label', default: 'Fecha'), fechaInstance.id])
        redirect(action: "show", id: fechaInstance.id)
    }

	@Secured(['ROLE_ADMIN'])
    def delete() {
        def fechaInstance = Fecha.get(params.id)
        if (!fechaInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "list")
            return
        }

        try {
            fechaInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fecha.label', default: 'Fecha'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
	
	@Secured(['ROLE_ADMIN'])
	def aprobarComoAdmin(){
		def fecha = Fecha.get(params.id)
		fecha.desaprobadoPorRival = false
		fecha.aprobadoPorAdmin = true
		fecha.save()
		flash.message = 'Resultado aprobado'
		redirect(action: "show", id: fecha.id)
	}
	
	def aprobarComoRival(){
		def fecha = Fecha.get(params.id)
		if(fecha.single1 == null || fecha.single2 == null || fecha.doble == null){
			flash.message = 'No podes aprobar el resultado porque falta completar el mismo'
			redirect(action: "show", id: params.id)
			return
		}
		if(fecha.equipoPerdedor == null || !(fecha.equipoPerdedor.capitan == getLoggedUser())){
			flash.message = 'No podes aprobar el resultado porque no sos el capitan del equipo perdedor'
			redirect(action: "show", id: params.id)
			return
		}
		fecha.desaprobadoPorRival = false
		fecha.aprobadoPorRival = true
		fecha.save()
		flash.message = 'Resultado aprobado'
		redirect(action: "show", id: fecha.id)
	}
	
	def desaprobar(){
		def fecha = Fecha.get(params.id)
		if((fecha.equipoPerdedor == null || !(fecha.equipoPerdedor.capitan == getLoggedUser())) && !esAdmin()){
			flash.message = 'No podes aprobar el resultado porque no sos el capitan del equipo perdedor'
			redirect(action: "show", id: params.id)
			return
		}
		fecha.aprobadoPorRival = false
		fecha.desaprobadoPorRival = true
		fecha.save()
		flash.message = 'Resultado desaprobado. Contacta al equipo rival para que corrijan el resultado. ' + 
							'Si el resultado no se corrige, deberas traer a la administracion tu copia de la planilla firmada'
		redirect(action: "show", id: fecha.id)
	}
	
	def pedirReprogramacionFecha() {
		def fecha = Fecha.get(params.id)
		def fechaReprogramacion = fixtureService.getPrimeraFechaDeJuegoDisponible(fecha)
		fecha.pedidoCambioDeFecha = true
		fecha.fechaReprogramacion = fechaReprogramacion
		fecha.save()
	}
	
	def void redirectIfNotAllowedEdit(Fecha fecha){
		if(fecha.aprobadoPorRival){
			flash.message = 'Los datos de los partidos no pueden modificarse porque ya han sido aprobados por el rival'
			redirect(action: "show", id: fecha.id)
			return
		}
		if(fecha.aprobadoPorAdmin){
			flash.message = 'Los datos de los partidos no pueden modificarse porque ya han sido aprobados por el administrador'
			redirect(action: "show", id: fecha.id)
			return
		}
	}
	
}
