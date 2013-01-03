package intertigre.domain
import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils

import extension.custom.Report
import grails.plugin.spock.IntegrationSpec
import grails.validation.ValidationException

@Report
class JugadorSpec extends IntegrationSpec{

	def setupSpec() {
		DateTimeUtils.setCurrentMillisFixed(new DateTime(2012, 1, 1, 0,0,0).getMillis())
	}
	
	def cleanupSpec() {
		DateTimeUtils.setCurrentMillisSystem()
	}
	
	def 'no deberia permitir eliminar un jugador'(){
		given:
			def jugador = Jugador.build(nombre: 'roger')
		when:
			jugador.club.jugadores.remove(jugador) //Para evitar que error de Hibernate
			jugador.delete()
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
			Jugador.build(dni: '1')
		when: 'creo otro jugador con el mismo dni'
			Jugador j = Jugador.buildWithoutSave(dni: '1')
			j.club.save(flush: true, failOnError: true)
			j.save(flush: true, failOnError: true)
		then:
			Jugador.findAll().size() == 1
			def ex = thrown(ValidationException)
			ex.message.contains('Jugador.dni.unique.error')
	}
	
	def 'no deberia dejar crear un jugador con atributos nulos'(){
		given: 'un jugador con atributos nulos'
			def jugador = new Jugador(nombre: nombre, apellido: apellido, email: email, password: password, 
										telefono: telefono, dni: dni, club: club, sexo: sexo, role: role)
		when: 'intento guardar al jugador'
			jugador.save(flush: true)
		then: 'no se deberia guardar'
			Jugador.findAll().size() == 0
		where:
			nombre | apellido | email | password | telefono | dni | club          | sexo | role
			null   |   'ab'   | 'ab'  | 'ab'     | 'ab'     | 'ab'|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   null   | 'ab'  | 'ab'     | 'ab'     | 'ab'|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | null  | 'ab'     | 'ab'     | 'ab'|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | null     | 'ab'     | 'ab'|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | 'ab'     | null     | 'ab'|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | 'ab'     | 'ab'     | null|Club.build()   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | 'ab'     | 'ab'     | 'ab'|        null   |'M'   |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | 'ab'     | 'ab'     | 'ab'|Club.build()   | null |'Jugador normal'
			'ab'   |   'ab'   | 'ab'  | 'ab'     | 'ab'     | 'ab'|Club.build()   |'M'   |null
			
	}
}
