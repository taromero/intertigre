package intertigre.domain

class Equipo {
	static belongsTo = [club: Club]
	static hasMany = [itemsListaBuenaFe: ItemListaBuenaFe, fechasLocal: Fecha, fechasVisitante: Fecha]
	static mappedBy = [fechasLocal: 'equipoLocal', fechasVisitante: 'equipoVisitante']
	static hasOne = [grupo: Grupo]
	List<Fecha> fechasLocal = new ArrayList<Fecha>()
	List<Fecha> fechasVisitante = new ArrayList<Fecha>()
	Categoria categoria
	String jerarquia
	Jugador capitan
	Club club
	
	//TreeSet me asegura no tener items repetidos, no tener items con la misma posicion y
	//me mantiene los items ordenados
	SortedSet<ItemListaBuenaFe> itemsListaBuenaFe = new TreeSet<ItemListaBuenaFe>()
	Boolean estaConfirmado // Si ya paso la fecha en que se puede modificar libremente la lista de buena fe
	Boolean estaBienFormado
	
	static constraints = {
		club nullable:true, editable:false, unique: ['categoria', 'jerarquia']
		categoria nullable:false, editable:false
		jerarquia matches:'[A-Z]', size:0..1, editable:false
		capitan blank:false, nullable: true
		estaBienFormado blank:false, editable:false
		estaConfirmado blank:false
		grupo nullable: true
		fechasLocal nullable: true
		fechasVisitante nullable: true
	}
	
	def beforeDelete() {
		if(this.participaEnAlgunaFecha()){
			throw new UnsupportedOperationException('No se puede eliminar el equipo porque participan en al menos 1 fecha.')
		}
	}
	
	def Equipo(){
		estaConfirmado = false
	}
	
	def boolean participaEnAlgunaFecha(){
		return !fechasLocal.isEmpty() || !fechasVisitante.isEmpty()
	}

	def List<Jugador> getJugadores(){
		return itemsListaBuenaFe*.jugador
	}
	
	def List<Fecha> getFechas(){
		return fechasLocal.plus(fechasVisitante).sort()
	}
	
	def List<Equipo> getEquiposVisitantes(){
		return this.fechas*.equipoVisitante.findAll{it != this}
	}

	def Boolean getEstaBienFormado(){
		return !itemsListaBuenaFe.any{ hayOtroItemEnMismaPosicion(it) }
	}
	
	def Boolean hayOtroItemEnMismaPosicion(ItemListaBuenaFe item){
		return this.itemsListaBuenaFe.count{ it.posicion == item.posicion } > 1
	}
	
	def boolean equals(equipo){
		if (this.is(equipo)) return true
		
		if (!equipo || getClass() != equipo.class) return false
		
		return this.id == equipo.id
	}
	
	def String toString(){
		club.toString() + ' ' + categoria.toString() + ' ' +jerarquia
	}
}
