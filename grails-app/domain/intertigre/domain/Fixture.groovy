package intertigre.domain

class Fixture{

	static hasMany = [fechas: Fecha, grupos: Grupo]

	List<Fecha> fechas = new ArrayList<Fecha>()
	List<Grupo> grupos = new ArrayList<Grupo>()

	Categoria categoria

	static constraints = {
		fechas display:false
		grupos display:false
	}

	def List<List<Fecha>> getFechasSeparadasEnGruposByUsuario(usuario){
		def fechas = this.fechas.findAll{ it.equiposLocal.itemsListaBuenaFe*.jugador.contains(usuario) || it.equiposVisitante.itemsListaBuenaFe*.jugador.contains(usuario) }
		def fechasAgrupadas = fechas.groupBy{ it.grupo }
		fechasAgrupadas.sort(true){ it.get(0).grupo.nombre }
		return fechas
	}

	def List<Fecha> getFechasByEquipo(equipo){
		def fechas = this.fechas.findAll{ it.equipoLocal == equipo || it.equipoVisitante == equipo }
		return fechas
	}
}