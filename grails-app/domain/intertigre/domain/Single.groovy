package intertigre.domain

import java.util.List;

class Single extends Partido{
	Jugador jugadorLocal
	Jugador jugadorVisitante

	def String toString(){
		return jugadorLocal.toString() + ' - ' + jugadorVisitante.toString() + ' : ' +
					 primerSet.toString() + ' ' + segundoSet.toString() + ' ' + (tercerSet != null ? tercerSet.toString() : '') + 
					 ' gano ' + equipoGanador.toString();
	}
	
	public List<Jugador> jugadores() {
		return [jugadorLocal, jugadorVisitante]
	}
}
