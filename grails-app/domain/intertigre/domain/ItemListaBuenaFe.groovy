package intertigre.domain



class ItemListaBuenaFe  implements Comparable<ItemListaBuenaFe>{

	static belongsTo = [equipo: Equipo, jugador: Jugador]

	Jugador jugador
	Equipo equipo
	Integer posicion

	def String toString(){
		jugador.toString() + ', pos:' + posicion
	}

	def int compareTo(item){
		this.posicion * (this.equipo.id ? this.equipo.id + 100 : 1) <=> item.posicion * (item.equipo.id ? item.equipo.id + 100 : 1)
	}
	
	def boolean equals(item){
		if (this.is(item)) return true
		
		if (!item || getClass() != item.class) return false
		
		return this.jugador == item.jugador && this.equipo == item.equipo && this.posicion == item.posicion
	}

	static constraints = {
//		jugador unique:'equipo', nullable:false
//		equipo unique: 'posicion', nullable:false
//		posicion nullable: false
	}
}
