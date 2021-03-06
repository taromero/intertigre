package intertigre.domain

import grails.plugins.springsecurity.Secured

import javax.naming.LimitExceededException

class ClubController extends BaseDomainController{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [clubInstanceList: Club.list(params), clubInstanceTotal: Club.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        [clubInstance: new Club(params)]
    }

    def save() {
		List<Integer> horariosAux = params.horariosPreferidosParaLocal
															.replaceAll(' ', '').split(",")
		List<Integer> horariosPreferidosParaLocal = new ArrayList<Integer>()
		def triosDeCanchasDisponibles = new Integer(params.triosDeCanchasDisponibles)
		params.remove('horariosPreferidosParaLocal')
		params.remove('triosDeCanchasDisponibles')
        def clubInstance = new Club(params)
		clubInstance.triosDeCanchasDisponibles = triosDeCanchasDisponibles
		for(horarioString in horariosAux) { 
			def horario = new Integer(horarioString)
			horariosPreferidosParaLocal.add(horario) 
			if(horario > 24 || horario < 1){
				render(view: "create", model: [clubInstance: clubInstance])
				flash.message = 'Ingresaste un numero mayor a 24 o menor a 0 para los horarios'
				return
			}	
			
		}
		clubInstance.horariosPreferidosParaLocal = horariosPreferidosParaLocal
		
        if (!clubInstance.save(flush: true)) {
            render(view: "create", model: [clubInstance: clubInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'club.label', default: 'Club'), clubInstance.id])
        redirect(action: "show", id: clubInstance.id)
    }

    def show() {
        def clubInstance = Club.get(params.id)
        if (!clubInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'club.label', default: 'Club'), params.id])
            redirect(action: "list")
            return
        }

        [clubInstance: clubInstance]
    }
	
	@Secured(['ROLE_CAPITAN_CLUB'])
    def edit() {
        def clubInstance = Club.get(params.id)
		this.checkIsUsuarioEsDelClub(clubInstance)
		
        if (!clubInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'club.label', default: 'Club'), params.id])
            redirect(action: "list")
            return
        }

        [clubInstance: clubInstance]
    }
	
	@Secured(['ROLE_CAPITAN_CLUB'])
    def update() {
        def clubInstance = Club.get(params.id)
		this.checkIsUsuarioEsDelClub(clubInstance)
		
        if (!clubInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'club.label', default: 'Club'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (clubInstance.version > version) {
                clubInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'club.label', default: 'Club')] as Object[],
                          "Another user has updated this Club while you were editing")
                render(view: "edit", model: [clubInstance: clubInstance])
                return
            }
        }
		
		params.horariosPreferidosParaLocal = params.horariosPreferidosParaLocal.replaceAll(/\[|\]/, '').split(',')
		
        clubInstance.properties = params

        if (!clubInstance.save(flush: true)) {
            render(view: "edit", model: [clubInstance: clubInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'club.label', default: 'Club'), clubInstance.id])
        redirect(action: "show", id: clubInstance.id)
    }
	
}
