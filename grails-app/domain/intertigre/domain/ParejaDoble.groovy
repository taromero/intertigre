package intertigre.domain

class ParejaDoble {
	
	static belongsTo = [doble: Doble]
	
	Jugador doblista1
	Jugador doblista2
	
	def String toString(){
		return doblista1.toString() + "/" + doblista2.toString()
	}
}
