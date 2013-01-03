package intertigre.domain

import extension.custom.Report;
import spock.lang.IgnoreRest;
import grails.plugin.spock.IntegrationSpec

@Report
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
