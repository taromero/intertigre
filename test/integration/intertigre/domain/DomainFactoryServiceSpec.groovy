package intertigre.domain

import grails.plugin.spock.IntegrationSpec;
import intertigre.util.DomainFactoryService;
import static intertigre.util.DomainFactoryService.createFechasParaReprogramar;

class DomainFactoryServiceSpec extends IntegrationSpec{

	def 'el metodo crearFechasParaReprogramar anda'() {
		when:
			def fechas = createFechasParaReprogramar(new Date(), 10)
		then:
			fechas.size() == 10
	}
	
}
