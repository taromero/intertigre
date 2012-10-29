package intertigre.domain

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class NotificacionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [notificacionInstanceList: Notificacion.list(params), notificacionInstanceTotal: Notificacion.count()]
    }

    def create() {
        [notificacionInstance: new Notificacion(params)]
    }

    def save() {
        def notificacionInstance = new Notificacion(params)
        if (!notificacionInstance.save(flush: true)) {
            render(view: "create", model: [notificacionInstance: notificacionInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), notificacionInstance.id])
        redirect(action: "show", id: notificacionInstance.id)
    }

    def show() {
        def notificacionInstance = Notificacion.get(params.id)
        if (!notificacionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "list")
            return
        }

        [notificacionInstance: notificacionInstance]
    }

    def edit() {
        def notificacionInstance = Notificacion.get(params.id)
        if (!notificacionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "list")
            return
        }

        [notificacionInstance: notificacionInstance]
    }

    def update() {
        def notificacionInstance = Notificacion.get(params.id)
        if (!notificacionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (notificacionInstance.version > version) {
                notificacionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'notificacion.label', default: 'Notificacion')] as Object[],
                          "Another user has updated this Notificacion while you were editing")
                render(view: "edit", model: [notificacionInstance: notificacionInstance])
                return
            }
        }

        notificacionInstance.properties = params

        if (!notificacionInstance.save(flush: true)) {
            render(view: "edit", model: [notificacionInstance: notificacionInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), notificacionInstance.id])
        redirect(action: "show", id: notificacionInstance.id)
    }

    def delete() {
        def notificacionInstance = Notificacion.get(params.id)
        if (!notificacionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "list")
            return
        }

        try {
            notificacionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'notificacion.label', default: 'Notificacion'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
