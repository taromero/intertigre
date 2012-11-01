package intertigre.domain

import grails.plugin.spock.IntegrationSpec
import intertigre.test.utils.DomainFactoryTestService
import spock.lang.Ignore

class EquipoSpec extends IntegrationSpec{

	DomainFactoryTestService domainFactoryTestService = new DomainFactoryTestService()
	
	def 'no permitir borrar equipo si esta en alguna fecha'(){
		given: 'un equipo con una fecha'
			def canottoTeam = Equipo.build()
			canottoTeam.save()
			new Fecha(equipoLocal: canottoTeam).save()
			def fecha = Fecha.findAll().find { it.equipoLocal == canottoTeam }
			canottoTeam.fechasLocal.add(fecha)
		when: 'intento eliminar el equipo'
			canottoTeam.delete(flush: true)
		then: 'no se permite la eliminacion'
			thrown(UnsupportedOperationException)
	}
	
	def 'permitir borrar equipo que no esta en ninguna fecha'(){
		given: 'un equipo sin fechas'
			def canottoTeam = Equipo.build()
			canottoTeam.save()
		when: 'intento eliminar el equipo'
			canottoTeam.delete(flush: true)
		then: 'el equipo es eliminado'
			Equipo.findAll().isEmpty()
	}
	
	def 'obtener el equipo ganador de una fecha'(){
		given: '2 equipos (canotto y elChasqui) y una fecha con 2 partidos ganados por canotto y 1 ganado por el Chasqui'
			Equipo canotto = domainFactoryTestService.crearEquipoCanotto()
			Equipo elChasqui = domainFactoryTestService.crearEquipoElChasqui()
			Fecha fecha = new Fecha(equipoLocal: canotto, equipoVisitante: elChasqui, 
						single1: new Single(equipoGanador: canotto), single2: new Single(equipoGanador: elChasqui), 
						doble: new Doble(equipoGanador: canotto))
		expect: 'el equipo ganador es canotto y el perdedor es eChasqui'
			fecha.equipoGanador == canotto
			fecha.equipoPerdedor == elChasqui
	}
	
	def 'obtener null si pregunto por ganador de una fecha'(){
		given: '2 equipos (canotto y elChasqui) y una fecha sin partidos jugados'
			Equipo canotto = domainFactoryTestService.crearEquipoCanotto()
			Equipo elChasqui = domainFactoryTestService.crearEquipoElChasqui()
			Fecha fecha = new Fecha(equipoLocal: canotto, equipoVisitante: elChasqui)
		expect: 'no deberia haber equipo ganador ni equipo perdedor'
			fecha.equipoGanador == null
			fecha.equipoPerdedor == null
	}

	@Ignore()
	def 'no permitir guardar un equipo con una clave categoria/jerarquia/club existente'(){
		given: 'un equipo a con un club c, categoria ca y jerarquia j'
			def cat = new Categoria(nombre: '+19', sexo: 'M', edadLimiteInferior: 19, edadLimiteSuperior: 25)
							.save(failOnError: true, flush: true)
			def canotto = domainFactoryTestService.crearClubCanotto()
			new Equipo(categoria: cat, jerarquia: 'A', club: canotto).save(flush: true)
		when: 'quiero crear un club b con la misma clave categoria/jerarquia/club'
			new Equipo(categoria: cat, jerarquia: 'A', club: canotto).save(failOnError:true, flush: true)
		then:
			Equipo.findAll().size() == 1
			thrown(Exception)
	}
	
}