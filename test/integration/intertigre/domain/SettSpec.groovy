package intertigre.domain

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
	
}
