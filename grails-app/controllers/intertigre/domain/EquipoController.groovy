package intertigre.domain

import grails.converters.JSON
import grails.gorm.DetachedCriteria
import grails.plugins.springsecurity.Secured

import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_CAPITAN_EQUIPO'])
class EquipoController extends BaseDomainController{

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        def idClub = !['false', ''].contains(params.club?.id) ? params.club?.id as Long : false
        def idCategoria = !['false', ''].contains(params.categoria?.id) ? params.categoria?.id as Long : false
        def jerarquiaParam = !['false', ''].contains(params.jerarquia) ? params.jerarquia : false
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def esBusqueda = idClub || idCategoria || jerarquiaParam

        DetachedCriteria filtro = Equipo.where {
                        if (idClub)
                            club.id == idClub
                        if (idCategoria)
                            categoria.id == idCategoria
                        if(jerarquiaParam)
                            jerarquia == jerarquiaParam   
                    }

        def equiposList = esBusqueda ? filtro.list(params) : Equipo.list(params)
        def equiposTotal = esBusqueda ? filtro.list().size() : Equipo.count
        render(view: 'list', model: [equipoInstanceList: equiposList, equipoInstanceTotal: equiposTotal, idClubSeleccionado: idClub,
                                    idCategoriaSeleccionada: idCategoria, jerarquiaSeleccionada: jerarquiaParam])
    }

	@Secured(['ROLE_JUGADOR'])
    def misEquipos() {
        def loggedUser = getLoggedUser()
        def equipos = loggedUser?.getEquipos()
        if(equipos?.size() == 1){
            redirect(action: 'show', id: equipos.get(0).id)
        }else if(equipos?.size() > 1){
			render(view: 'list', model: [equipoInstanceList: equipos, equipoInstanceTotal: equipos.size()])
        }else{
            flash.message = 'No tenes equipos por el momento'
            redirect(action: 'list')
        }
    }
	
	def listEquiposClub() {
		def loggedUser = getLoggedUser()
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def equipos = Equipo.findAll {club == loggedUser.club}
		render(view: 'list', model: [equipoInstanceList: equipos, equipoInstanceTotal: equipos.size()])
	}

    def create() {
        [equipoInstance: new Equipo(params)]
    }

    def save() {
        def equipoInstance = new Equipo(jerarquia: params.jerarquia)
		equipoInstance.categoria = Categoria.get(params.categoria.id)
		def loggedUser = getLoggedUser()
		equipoInstance.club = loggedUser.club
		loggedUser.club.equipos.add(equipoInstance)
        if (!equipoInstance.save(flush: true, failOnError: true)) {
            render(view: "create", model: [equipoInstance: equipoInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'equipo.label', default: 'Equipo'), equipoInstance.id])
        redirect(action: "show", id: equipoInstance.id)
    }

    def show() {
        def equipoInstance = Equipo.get(params.id)
        if (!equipoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
            return
        }

        [equipoInstance: equipoInstance]
    }

    def edit() {
        def equipoInstance = Equipo.get(params.id)
		redirectIfNotAllowedEdit(equipoInstance)
		if (!equipoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
            return
        }
        [equipoInstance: equipoInstance]
    }

    def editListaBuenaFe() {
        def equipoInstance = Equipo.get(params.id)
		redirectIfNotAllowedEdit(equipoInstance)
        if (!equipoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
            return
        }
		
		def jugadoresClubQueNoEstanEnEquipo = Jugador.findAllByClub(equipoInstance.club).findAll{!equipoInstance.itemsListaBuenaFe*.jugador.contains(it)}
        render(view: "editListaBuenaFe", model: [equipoInstance: equipoInstance, jugadoresClub: jugadoresClubQueNoEstanEnEquipo])
    }

    def cambiarCapitanEquipoAjax() {
        def idNuevoCapitan = params.idNuevoCapitan as Long
        def idEquipo = params.idEquipo as Long
        def equipo = Equipo.get(idEquipo)
        def nuevoCapitan = Jugador.get(idNuevoCapitan)
        equipo.capitan = nuevoCapitan
        render equipo.capitan as JSON
    }

    def update() {
        def equipoInstance = Equipo.get(params.id)
		redirectIfNotAllowedEdit(equipoInstance)
		def listaBuenaFeDnis = params.listaEditada.replaceAll('dni', '').split(',')
		
		def i = 0
		for(dni in listaBuenaFeDnis){
			def item = equipoInstance.itemsListaBuenaFe.find{ it.jugador.dni == dni }
			if(item != null){
				item.posicion = i
			}else{
				def jugador = Jugador.findByDni(dni) 
                def itemLista = new ItemListaBuenaFe(jugador: jugador, posicion: i, equipo: equipoInstance)
                jugador.itemsListasBuenaFe.add(itemLista)
				equipoInstance.itemsListaBuenaFe.add(itemLista)
			}
			i++
		}

		def itemsAEliminar = equipoInstance.itemsListaBuenaFe.findAll{!listaBuenaFeDnis.contains(it.jugador.dni)}
		itemsAEliminar.each{ equipoInstance.itemsListaBuenaFe.remove(it); it.jugador.itemsListasBuenaFe.remove(it); it.delete() }
				
        if (!equipoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (equipoInstance.version > version) {
                equipoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'equipo.label', default: 'Equipo')] as Object[],
                          "Another user has updated this Equipo while you were editing")
                render(view: "show", model: [equipoInstance: equipoInstance])
                return
            }
        }

        equipoInstance.properties = params

        if (!equipoInstance.save(flush: true)) {
            render(view: "show", model: [equipoInstance: equipoInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'equipo.label', default: 'Equipo'), equipoInstance.id])
        redirect(action: "show", id: equipoInstance.id)
    }

    def delete() {
        def equipoInstance = Equipo.get(params.id)
        if (!equipoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
            return
        }

        try {
            equipoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'equipo.label', default: 'Equipo'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
	
	def void redirectIfNotAllowedEdit(Equipo equipo){
		if(!esCapitanClub() && equipo.capitan != getLoggedUser()){
			flash.message = 'No puede editar este equipo porque usted no es su capitan'
			redirect(action: "show", id: equipo.id)
			return
		}
	}
}
