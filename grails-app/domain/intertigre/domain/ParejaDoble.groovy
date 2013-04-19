package intertigre.domain

class ParejaDoble {
	
	static belongsTo = [doble: Doble]
	
	Jugador doblista1
	Jugador doblista2
	
	def ParejaDoble(doblista1, doblista2) {
		this.doblista1 = doblista1
		this.doblista2 = doblista2
	}

	def String toString(){
		return doblista1.toString() + "/" + doblista2.toString()
	}
}
