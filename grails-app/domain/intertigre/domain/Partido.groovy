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
		tercerSet nullable: true, validator: { Sett set, Partido partido -> 
															if(partido.tieneMasSetsGanadosPorDerechaQuePorIzquierda()) {
																return 'El partido tiene mal los datos de los sets.' +
																'Los games del ganador deben ir del lado izquierdo de cada set'
															}
															if(!(set.gamesGanador > set.gamesPerdedor)){
														   		return 'El ultimo set debe tener un numero mayor ' + 
																   'de games al principio que final (por ej. 6-3 y no 3-6)'
															}
											 }
	}
	
	private boolean tieneMasSetsGanadosPorDerechaQuePorIzquierda(){
		def ps = [primerSet.gamesGanador, primerSet.gamesPerdedor]
		def ss = [segundoSet.gamesGanador, segundoSet.gamesPerdedor]
		def ts = [tercerSet.gamesGanador, tercerSet.gamesPerdedor]
		def aux = 0
		ps[0] < ps[1] ? aux-- : aux++
		ss[0] < ss[1] ? aux-- : aux++
		(ts[0] != null) ? ((ts[0] < ts[1]) ? aux-- : aux++) : null
		return aux < 1
	}
}
