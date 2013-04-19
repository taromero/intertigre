package intertigre.domain

import java.util.List;

class Doble extends Partido{
	ParejaDoble parejaLocal
	ParejaDoble parejaVisitante

	def String toString(){
		return parejaLocal.toString() + ' - ' + parejaVisitante.toString() + ' : ' +
				primerSet.toString() + ' ' + segundoSet.toString() + ' ' + (tercerSet != null ? tercerSet.toString() : '') +
				' gano ' + equipoGanador.toString();
	}
	
	public List<Jugador> jugadores() {
		return [parejaLocal.doblista1, parejaLocal.doblista2, 
				parejaVisitante.doblista1, parejaLocal.doblista2]
	}
	
}
