package intertigre.security

import grails.plugins.springsecurity.Secured

import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class SecUserSecRoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [secUserSecRoleInstanceList: SecUserSecRole.list(params), secUserSecRoleInstanceTotal: SecUserSecRole.count()]
    }

    def create() {
        [secUserSecRoleInstance: new SecUserSecRole(params)]
    }

    def save() {
        def secUserSecRoleInstance = new SecUserSecRole(params)
        if (!secUserSecRoleInstance.save(flush: true)) {
            render(view: "create", model: [secUserSecRoleInstance: secUserSecRoleInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), secUserSecRoleInstance.id])
        redirect(action: "show", id: secUserSecRoleInstance.id)
    }

    def show() {
        def secUserSecRoleInstance = SecUserSecRole.get(params.id)
        if (!secUserSecRoleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "list")
            return
        }

        [secUserSecRoleInstance: secUserSecRoleInstance]
    }

    def edit() {
        def secUserSecRoleInstance = SecUserSecRole.get(params.id)
        if (!secUserSecRoleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "list")
            return
        }

        [secUserSecRoleInstance: secUserSecRoleInstance]
    }

    def update() {
        def secUserSecRoleInstance = SecUserSecRole.get(params.id)
        if (!secUserSecRoleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (secUserSecRoleInstance.version > version) {
                secUserSecRoleInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'secUserSecRole.label', default: 'SecUserSecRole')] as Object[],
                          "Another user has updated this SecUserSecRole while you were editing")
                render(view: "edit", model: [secUserSecRoleInstance: secUserSecRoleInstance])
                return
            }
        }

        secUserSecRoleInstance.properties = params

        if (!secUserSecRoleInstance.save(flush: true)) {
            render(view: "edit", model: [secUserSecRoleInstance: secUserSecRoleInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), secUserSecRoleInstance.id])
        redirect(action: "show", id: secUserSecRoleInstance.id)
    }

    def delete() {
        def secUserSecRoleInstance = SecUserSecRole.get(params.id)
        if (!secUserSecRoleInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "list")
            return
        }

        try {
            secUserSecRoleInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'secUserSecRole.label', default: 'SecUserSecRole'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
