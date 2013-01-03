package intertigre.domain

import static intertigre.util.DomainFactoryService.createFechasParaReprogramar

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import extension.custom.Report
import grails.plugin.spock.IntegrationSpec
@Report
class DomainFactoryServiceSpec extends IntegrationSpec{

	def 'el metodo crearFechasParaReprogramar anda'() {
		when:
			def fechas = createFechasParaReprogramar(new Date(), 10)
		then:
			fechas.size() == 10
	}
	
}
