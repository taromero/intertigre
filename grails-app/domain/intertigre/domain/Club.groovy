package intertigre.domain

class Club {
	static hasMany = [equipos: Equipo, horariosPreferidosParaLocal: Integer, jugadores: Jugador]
	
	List<Equipo> equipos = new ArrayList<Equipo>()
	String nombre
	String direccion
	String telefono
	String email
	String localidad
	Integer triosDeCanchasDisponibles
	List<Integer> horariosPreferidosParaLocal = new ArrayList<Integer>()
	
	static constraints = {
		nombre blank:false, unique: ['localidad']
		direccion blank:false
		telefono blank:false
		email blank:false, email: true
		localidad blank:false
		triosDeCanchasDisponibles matches:'[0-9]'
		equipos display:false, editable:false
	}
	
	def beforeDelete() {
		throw new UnsupportedOperationException('Delete not allowed')
	}
	
	def String toString(){
		return this.nombre + ', ' + localidad
	}
	
	def boolean equals(club) {
		if (this.is(club)) return true
		
		if (!club || getClass() != club.class) return false
		
		return this.nombre == club.nombre && this.localidad == club.localidad
	}
	
}
