package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha
import intertigre.util.DomainFactoryService

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import spock.lang.Ignore

class EquipoSpec extends BaseIntegrationSpec{

	def 'no permitir borrar equipo si esta en alguna fecha'(){
		given: 'un equipo con una fecha'
			Equipo canottoTeam = Equipo.build()
			Equipo equipoVisitante = Equipo.build()
			canottoTeam.fechasLocal.add(createFecha(canottoTeam, equipoVisitante, new Date()))
		when: 'intento eliminar el equipo'
			canottoTeam.delete()
		then: 'no se permite la eliminacion'
			thrown(UnsupportedOperationException)
	}
	
	def 'permitir borrar equipo que no esta en ninguna fecha'(){
		given: 'un equipo sin fechas'
			Equipo canottoTeam = Equipo.build()
		when: 'intento eliminar el equipo'
			canottoTeam.delete()
		then: 'el equipo es eliminado'
			Equipo.findAll().isEmpty()
	}
	
	def 'obtener el equipo ganador de una fecha'(){
		given: '2 equipos (canotto y elChasqui)'
			Equipo canotto = domainFactoryService.crearEquipoMas19MCanotto()
			Equipo elChasqui = domainFactoryService.crearEquipoMas19MElChasqui()
		and: 'una fecha con 2 partidos ganados por canotto y 1 ganado por el Chasqui'
			Fecha fecha = new Fecha(equipoLocal: canotto, equipoVisitante: elChasqui, 
						single1: new Single(equipoGanador: canotto), single2: new Single(equipoGanador: elChasqui), 
						doble: new Doble(equipoGanador: canotto))
		expect: 'el equipo ganador es canotto y el perdedor es eChasqui'
			fecha.equipoGanador == canotto
			fecha.equipoPerdedor == elChasqui
	}
	
	def 'obtener null si pregunto por ganador de una fecha'(){
		given: '2 equipos (canotto y elChasqui) y una fecha sin partidos jugados'
			Equipo canotto = domainFactoryService.crearEquipoMas19MCanotto()
			Equipo elChasqui = domainFactoryService.crearEquipoMas19MElChasqui()
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
			def canotto = domainFactoryService.crearClubCanotto()
			new Equipo(categoria: cat, jerarquia: 'A', club: canotto).save(flush: true)
		when: 'quiero crear un club b con la misma clave categoria/jerarquia/club'
			new Equipo(categoria: cat, jerarquia: 'A', club: canotto).save(failOnError:true, flush: true)
		then:
			Equipo.findAll().size() == 1
			thrown(Exception)
	}
	
	def 'borrar un equipo'() {
		given: 'un equipo'
			Equipo equipo = domainFactoryService.crearEquipoMas19MCanotto()
		when: 'lo borro'
			equipo.club.equipos.remove(equipo)
			equipo.delete()
		then: 'se deberian borrar todos los items de la lista de buena fe'
			Equipo.count() == 0
			ItemListaBuenaFe.count() == 0
	}
	
}
