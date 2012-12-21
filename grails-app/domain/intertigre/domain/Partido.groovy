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
		tercerSet nullable: true, validator: { val, obj -> if(!(val.gamesGanador > val.gamesPerdedor)){
														   		return 'El ultimo set debe tener un numero mayor ' + 
																   'de games al principio que final (por ej. 6-3 y no 3-6)'
															}
											 }
	}
}
