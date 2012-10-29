package intertigre.domain

class Categoria {
	String nombre
	Integer edadLimiteInferior
	Integer edadLimiteSuperior
	String sexo

	def boolean equals(categoria){
		return true
		if (this.is(categoria)) return true
		
		if (!categoria || getClass() != categoria.class) return false
		return this.hashCode() == categoria.hashCode()
	}
	
	def int hashCode(){
		int result = (nombre ? nombre.hashCode() : 0)
		result = 31 * result + (edadLimiteInferior ? edadLimiteInferior.hashCode() : 0)
		result = 31 * result + (edadLimiteSuperior ? edadLimiteSuperior.hashCode() : 0)
		result = 31 * result + (sexo ? sexo.hashCode() : 0)
		return result
	}
		
	def String toString(){
		this.nombre + ' ' + this.sexo
	}
}
