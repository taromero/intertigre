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
														if(!partido.abandono) {
															if(partido.tieneIgualOMasSetsGanadosPorDerechaQuePorIzquierda()) {
																return 'El partido tiene mal los datos de los sets.' +
																'Los games del ganador deben ir del lado izquierdo de cada set'
															}
															if(set?.gamesGanador != null && !(set?.gamesGanador > set?.gamesPerdedor)){
														   		return 'El ultimo set debe tener un numero mayor ' + 
																   'de games al principio que final (por ej. 6-3 y no 3-6)'
															}
														}
											 }
		abandono validator: { Boolean abandon, Partido partido ->
								if(abandon){
									if(partido.tieneResultadoNormal()){
										return 'El partido esta marcado como abandono, pero ' +
												'tiene todos los sets terminados'
									} else if (partido.tieneSetsConGamesCuandoLosAnterioresEstanSinTerminar()) {
										return 'El partido tiene games para un set cuando el set anterior no esta terminado'
									}
								}
							 }
	}
	
	private boolean tieneIgualOMasSetsGanadosPorDerechaQuePorIzquierda() {
		def ps = [primerSet.gamesGanador, primerSet.gamesPerdedor]
		def ss = [segundoSet.gamesGanador, segundoSet.gamesPerdedor]
		def ts = [tercerSet?.gamesGanador, tercerSet?.gamesPerdedor]
		def aux = 0
		ps[0] < ps[1] ? aux-- : aux++
		ss[0] < ss[1] ? aux-- : aux++
		(ts[0] != null) ? ((ts[0] < ts[1]) ? aux-- : aux++) : null
		return aux <= 0
	}

	private boolean tieneResultadoNormal() {
		return tieneResultadoTerminado() && !tieneIgualOMasSetsGanadosPorDerechaQuePorIzquierda()
	}

	private boolean tieneResultadoTerminado() {
		return primerSet.terminoDeJugarse() && segundoSet.terminoDeJugarse() && (tercerSet == null || tercerSet.terminoDeJugarse()) 
	}

	private boolean tieneSetsConGamesCuandoLosAnterioresEstanSinTerminar() {
		if(!primerSet?.terminoDeJugarse()) {
			if(segundoSet != null || tercerSet != null) {
				return true
			}
		} else if (!segundoSet?.terminoDeJugarse() && tercerSet != null) {
			return true
		} else {
			return false
		}
	}
}
