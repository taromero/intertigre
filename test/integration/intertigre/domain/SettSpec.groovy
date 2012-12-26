package intertigre.domain

import spock.lang.IgnoreRest;
import grails.plugin.spock.IntegrationSpec


class SettSpec extends IntegrationSpec{

	def 'no deberia dejar guardar singles con sets de mas de 7 games'(){
		given:
			def set = new Sett(gamesGanador: 8, gamesPerdedor: 5)
		when:
			def isGuardado = set.save(failOnError: true, flush: true)
		then:
			!isGuardado
			thrown(Exception)
	}
	
	def 'un set no puede tener una diferencia de games menor a 2 si es normal o mayor 1 si tuvo tie-break'(){
		given: 'un set con diferencia de menos de 2 games o de mas de 1 con tie-break'
			Sett set = sett
		expect: 'no pasa la validacion'
			set.validate() == false
			set.errors.allErrors.toString().contains('La diferencia de games en el set no es correcta')
		where:
			sett << [new Sett(gamesGanador: 7, gamesPerdedor: 4),
						new Sett(gamesGanador: 6, gamesPerdedor: 5),
						new Sett(gamesGanador: 3, gamesPerdedor: 7),
						new Sett(gamesGanador: 7, gamesPerdedor: 7),
						new Sett(gamesGanador: 6, gamesPerdedor: 6),
						new Sett(gamesGanador: 5, gamesPerdedor: 5)]
	}
	
	def 'un set no puede tener una diferencia de games mayor o igual a 2 si es normal o  igual a 1 si tuvo tie-break'(){
		given: 'un set con diferencia de mas de 2 games o de 1 con tie-break'
			Sett set = sett
		expect: 'pasa la validacion'
			set.validate() == true
		where:
			sett << [new Sett(gamesGanador: 7, gamesPerdedor: 5),
						new Sett(gamesGanador: 7, gamesPerdedor: 6),
						new Sett(gamesGanador: 6, gamesPerdedor: 2)]
	}
}
