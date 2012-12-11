package intertigre.domain

import org.joda.time.LocalDate


class Fecha implements Comparable<Fecha>{
	
	Fixture fixture
	Grupo grupo
	Categoria categoria
	Equipo equipoLocal
	Equipo equipoVisitante
	Single single1
	Single single2
	Doble doble
	Date fechaDeJuego
	Date fechaReprogramacion
	List<String> observaciones
	Date fechaSubidaResultado
	Boolean aprobadoPorRival = false
	Boolean desaprobadoPorRival = false
	Boolean aprobadoPorAdmin = false
	Boolean formacionIncorrectaLocal = false
	Boolean formacionIncorrectaVisitante = false
	Boolean wo = false
	Boolean fueReprogramada = false

	static constraints = {
		single1 nullable: true
		single2 nullable: true
		doble nullable: true
		grupo nullable: true
		fixture nullable: true
		fechaReprogramacion nullable: true
	}
	
	def beforeDelete() {
		if(aprobadoPorRival || aprobadoPorAdmin || desaprobadoPorRival){
			throw new UnsupportedOperationException('Delete not allowed')	
		}
	}
	
	def beforeInsert() {
		if(equipoLocal.fechas.find { new LocalDate(it.fechaDeJuego) == new LocalDate(fechaDeJuego)  && !it.equals(this) }){
			throw new UnsupportedOperationException("El equipo '$equipoLocal' ya tiene una fecha para ese dia")
		}else if(equipoVisitante.fechas.find { new LocalDate(it.fechaDeJuego) == new LocalDate(fechaDeJuego) && !it.equals(this) }){
			throw new UnsupportedOperationException("El equipo '$equipoVisitante' ya tiene una fecha para ese dia")
		}
	}

	def void reprogramar() {
		fechaDeJuego = fechaReprogramacion
		fechaReprogramacion = null
		fueReprogramada = true
	}
	
	def Equipo getEquipoGanador(){
		def ganadores = [single1?.equipoGanador, single2?.equipoGanador, doble?.equipoGanador]
		if(ganadores.get(0) == null){
			return null
		}
		ganadores.count{it == equipoLocal} > ganadores.count{it == equipoVisitante} ? equipoLocal
																					: equipoVisitante
	}
	
	def Equipo getEquipoPerdedor(){
		def ganadores = [single1?.equipoGanador, single2?.equipoGanador, doble?.equipoGanador]
		if(ganadores.get(0) == null){
			return null
		}
		ganadores.count{it == equipoLocal} < ganadores.count{it == equipoVisitante} ? equipoLocal
																					: equipoVisitante
	}
	
	def Boolean verificarFormacionIncorrectaLocal(){
		return equipoLocal.itemsListaBuenaFe.find{ it.jugador == single1.jugadorLocal}.posicion >
				equipoLocal.itemsListaBuenaFe.find{ it.jugador == single2.jugadorLocal}.posicion
	}
	
	def Boolean verificarFormacionIncorrectaVisitante(){
		return equipoVisitante.itemsListaBuenaFe.find{ it.jugador == single1.jugadorVisitante}.posicion >
				equipoVisitante.itemsListaBuenaFe.find{ it.jugador == single2.jugadorVisitante}.posicion
	}
	
	public int compareTo(fecha) {
		return this.fechaDeJuego <=> fecha.fechaDeJuego
	}
	
	def boolean equals(fecha){
		if (this.is(fecha)) return true
				
		if (!fecha || getClass() != fecha.class) return false
						
		return this.id == fecha.id
	}
	
	def String toString(){
		equipoLocal.toString() + ' vs ' + equipoVisitante.toString() + ' ' + fechaDeJuego.toString() 
	}
}
