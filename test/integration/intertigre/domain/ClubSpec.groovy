package intertigre.domain

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import extension.custom.Report
import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

@Report
class ClubSpec extends IntegrationSpec{

	def 'no deberia permitir eliminar un club'(){
		given:
			def club = Club.build(nombre: 'Canottieri')
		when:
			club.delete(flush: true)
		then: 'no se permite la eliminacion'
			thrown(UnsupportedOperationException)
	}
	
	def 'no deberia permitir crear dos clubes con el mismo nombre/localidad'(){
		given: 'un club'
			Club.build(nombre: 'Canottieri', localidad: 'Tigre').save(failOnError: true, flush: true)
		when: 'creo otro club con el mismo nombre y localidad'
			Club.build(nombre: 'Canottieri', localidad: 'Tigre')
		then:
			Club.findAll().size() == 1
			Exception ex = thrown(Exception)
			ex.message.contains("Club.nombre.unique")
	}
	
	def 'no deberia permitir crear un club con email incorrecto'(){
		given:
			def club = new Club(nombre: 'Canottieri', email: 'a')
		when:
			club.save(flush: true, failOnError: true)
		then: 'no se permite la eliminacion'
			Exception ex = thrown(ValidationException)
			ex.message.contains('Club.email.email.invalid')
	}
}
