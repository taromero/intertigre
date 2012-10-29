package intertigre.domain

class Doble extends Partido{
	ParejaDoble parejaLocal
	ParejaDoble parejaVisitante

	def String toString(){
		return parejaLocal.toString() + ' - ' + parejaVisitante.toString() + ' : ' +
				primerSet.toString() + ' ' + segundoSet.toString() + ' ' + (tercerSet != null ? tercerSet.toString() : '') +
				' gano ' + equipoGanador.toString();
	}
	
}
