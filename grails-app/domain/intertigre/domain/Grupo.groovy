package intertigre.domain

class Grupo {
	static belongsTo = [fixture: Fixture]
	static hasMany = [equipos: Equipo, fechas: Fecha]
	
	Fixture fixture
	String nombre

	static constraints = {
		fixture nullable: true
	}
	
	def String toString(){
		return nombre
	}
}
