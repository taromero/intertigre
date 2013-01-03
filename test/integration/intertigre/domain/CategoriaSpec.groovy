package intertigre.domain

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import extension.custom.Report
import grails.plugin.spock.IntegrationSpec

@Report
class CategoriaSpec extends IntegrationSpec{

	def '2 categorias con los mismos atributos deberian ser iguales'() {
		given: 'una categoria con un set de atributos x'
			def nombre = '+19'
			def edadLimiteInferior = 19
			def edadLimiteSuperior = 24
			def sexo = 'M'
			def cat1 = new Categoria(nombre: nombre, edadLimiteInferior: edadLimiteInferior,
										edadLimiteSuperior: edadLimiteSuperior, sexo: sexo)
		and: 'otra categoria con el mismo set de atributos'
			def cat2 = new Categoria(nombre: nombre, edadLimiteInferior: edadLimiteInferior,
										edadLimiteSuperior: edadLimiteSuperior, sexo: sexo)
		expect: 'las categorias deberian ser iguales'
			cat1 == cat2
	}	
	
	def '2 categorias con los atributos distintos deberian ser distintos'() {
		given: 'una categoria con un set de atributos x'
			def nombre = '+19'
			def edadLimiteInferior = 19
			def edadLimiteSuperior = 24
			def sexo = 'M'
			def cat1 = new Categoria(nombre: nombre, edadLimiteInferior: edadLimiteInferior,
										edadLimiteSuperior: edadLimiteSuperior, sexo: sexo)
		and: 'otra categoria con el mismo set de atributos'
			def cat2 = new Categoria(nombre: nombre + 'blah', edadLimiteInferior: edadLimiteInferior,
										edadLimiteSuperior: edadLimiteSuperior, sexo: sexo)
		expect: 'las categorias deberian ser iguales'
			cat1 != cat2
	}
	
}
