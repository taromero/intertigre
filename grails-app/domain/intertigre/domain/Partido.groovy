package intertigre.domain

abstract class Partido {
	
	static belongsTo = [fecha: Fecha]
	
	Fecha fecha
	Equipo equipoGanador
	Sett primerSet
	Sett segundoSet
	Sett tercerSet
	Boolean abandono = false  
	
	static embedded = ['primerSet', 'segundoSet', 'tercerSet']
	
	static constraints = {
		fecha display:false
	}
}
