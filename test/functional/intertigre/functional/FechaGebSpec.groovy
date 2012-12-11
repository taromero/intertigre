package intertigre.functional

class FechaGebSpec extends BaseControllerGebSpec{

	def 'un usuario administrador quiere ver la lista de fechas a reprogramar'() {
		given: 'un usuario administrador logeado'
		and: 'una serie de fechas a reprogramar'
		when: 'voy a ver las fechas a reprogramar'
		then: 'deberia poder ver todas las fechas'
		and: 'con un checkbox en cada una para poder marcalas como resueltas'
		and: 'y un boton para marcar todas como resueltas'
	}
	
	def 'un usuario NO administrador quiere ver la lista de fechas a reprogramar'() {
		given: 'un usuario administrador logeado'
		and: 'una serie de fechas a reprogramar'
		when: 'voy a ver las fechas a reprogramar'
		then: 'me deberia redireccionar al home'
		and: 'indicando que no tengo permisos para ver esto'
	}
	
	def 'un usuario administrador quiere reprogramar todas las fechas'() {
		given: 'un usuario administrador logeado'
		and: 'una serie de fechas a reprogramar'
		when: 'voy a ver las fechas a reprogramar'
		and: 'toco el boton de reprogramar todas'
		then: 'me deberia redirigir a una pagina mostrandome las nuevas fechas de juego para cada fecha'
		when: 'voy a ver el detalle de alguna de las fechas'
		then: 'me muestra un texto indicando que la fecha fue reprogramada'
	}
	
	def 'un usuario administrador quiere reprogramar una serie de fechas'() {
		given: 'un usuario administrador logeado'
		and: 'una serie de fechas a reprogramar'
		when: 'voy a ver las fechas a reprogramar'
		and: 'selecciono los checkbox de las fechas que quiero reprogramar'
		and: 'toco el boton de reprogramar algunas'
		then: 'me deberia redirigir a una pagina mostrandome las nuevas fechas de juego para cada fecha seleccionada'
	}
	
	def 'un capitan de equipo quiere reprogramar una fecha'() {
		given: 'un capitan de equipo loggeado'
		and: 'una serie de fechas para el equipo del capitan'
		when: 'va a ver el detalle de una fecha'
		and: 'toca el boton de reprogramar'
		then: 'deberia volverse a la pagina de detalle'
		and: 'mostrando la fecha de reprogramacion'
	}
	
}
