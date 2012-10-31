package intertigre.domain

import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

class ClubSpec extends IntegrationSpec{

	def 'no deberia permitir eliminar un club'(){
		given:
			def club = new Club(nombre: 'Canottieri')
			club.save()
		when:
			club.delete(flush: true)
		then: 'no se permite la eliminacion'
			thrown(UnsupportedOperationException)
	}
	
	def 'no deberia permitir crear dos clubes con el mismo nombre/localidad'(){
		given: 'un club'
			new Club(nombre: 'Canottieri', localidad: 'Tigre').save(failOnError: true, flush: true)
		when: 'creo otro club con el mismo nombre y localidad'
			new Club(nombre: 'Canottieri', localidad: 'Tigre').save(failOnError: true, flush: true)
		then:
			Club.findAll().size() == 1
			thrown(ValidationException)
	}
}
