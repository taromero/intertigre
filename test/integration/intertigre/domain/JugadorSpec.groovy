package intertigre.domain
import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

import org.joda.time.DateTime

class JugadorSpec extends IntegrationSpec{

	def 'no deberia permitir eliminar un jugador'(){
		given:
			def jugador = new Jugador(nombre: 'roger')
			jugador.save()
		when:
			jugador.delete(flush: true)
		then: 'no se permite la eliminacion'
			thrown(UnsupportedOperationException)
	}
	
	def 'calculo de edad al finalizar anio'(){
		given: 'Un jugador nacido un dia x'
			DateTime fecha = new DateTime(anio, mes, dia, 0, 0, 0)
			def jugador = new Jugador(nacimiento: fecha.toDate())
		expect: 'su edad al finalizar el anio sera y'
			jugador.getEdadAlFinalizarAnio() == edadFinalAnio
		where:
			anio | mes | dia | edadFinalAnio
			1990 | 05  | 04  | 22
			1990 | 01  | 01  | 22
			1990 | 12  | 31  | 22
			1989 | 12  | 31  | 23
	}
	
	def 'crear jugador con dni existente'(){
		given: 'un jugador con dni x'
			new Jugador(dni: '1').save(failOnError: true, flush: true)
		when: 'creo otro jugador con el mismo dni'
			new Jugador(dni: '1').save(failOnError:true, flush: true)
		then:
			Jugador.findAll().size() == 1
			Exception ex = thrown(ValidationException)
			ex.message.contains('Jugador.dni.unique.error')
	}
	
}
