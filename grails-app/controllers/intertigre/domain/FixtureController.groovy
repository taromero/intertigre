package intertigre.domain

import grails.plugins.springsecurity.Secured;

import org.springframework.dao.DataIntegrityViolationException

@Secured(['ROLE_ADMIN'])
class FixtureController extends BaseDomainController{

    def fixtureService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [fixtureInstanceList: Fixture.list(params), fixtureInstanceTotal: Fixture.count()]
    }

    def create() {
        [fixtureInstance: new Fixture(params)]
    }

    def save() {
		def categoria = Categoria.get(params.categoria.id)
        def fixtureInstance = fixtureService.generarFixture(categoria)
        if (!fixtureInstance.save(flush: true)) {
            render(view: "create", model: [fixtureInstance: fixtureInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'fixture.label', default: 'Fixture'), fixtureInstance.id])
        redirect(action: "show", id: fixtureInstance.id)
    }

    def show() {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "list")
            return
        }

        [fixtureInstance: fixtureInstance]
    }

    def edit() {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "list")
            return
        }

        [fixtureInstance: fixtureInstance]
    }

    def update() {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (fixtureInstance.version > version) {
                fixtureInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'fixture.label', default: 'Fixture')] as Object[],
                          "Another user has updated this Fixture while you were editing")
                render(view: "edit", model: [fixtureInstance: fixtureInstance])
                return
            }
        }

        fixtureInstance.properties = params

        if (!fixtureInstance.save(flush: true)) {
            render(view: "edit", model: [fixtureInstance: fixtureInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'fixture.label', default: 'Fixture'), fixtureInstance.id])
        redirect(action: "show", id: fixtureInstance.id)
    }

    def delete() {
        def fixtureInstance = Fixture.get(params.id)
        if (!fixtureInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "list")
            return
        }

        try {
            fixtureInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'fixture.label', default: 'Fixture'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
