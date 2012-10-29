package intertigre.domain
import java.util.Date;

import intertigre.security.SecUser

import org.joda.time.DateTime;

class Jugador extends SecUser{
	def categoriaService
	
	static hasMany = [itemsListasBuenaFe: ItemListaBuenaFe]
	static transients = ['email']

	Club club
	Set<ItemListaBuenaFe> itemsListasBuenaFe = new TreeSet<ItemListaBuenaFe>()
	String sexo
	Date nacimiento
	String nombre
	String apellido
	String dni
	String telefono
	String role
	String urlImagen
	
	def Jugador(){
		this.enabled = true
	}

	def beforeDelete() {
		throw new UnsupportedOperationException('Delete not allowed')
	}
	
	static constraints = {
		role editable:false, inList: ['Administrador', 'Capitan club', 'Capitan equipo', 'Jugador normal']
		dni unique:true, matches:'[0-9]*'
		nombre matches: '[a-zA-ZñÑ ]*', unique:true
		apellido matches: '[a-zA-ZñÑ ]*'
		sexo inList:['M', 'F'], editable:false
		password password:true
	}

	def List<Equipo> getEquipos(){
		return itemsListasBuenaFe*.equipo
	}

	def Integer getEdadAlFinalizarAnio(){
		DateTime fechaNacimiento = new DateTime(this.nacimiento)
		int anioNacimiento = fechaNacimiento.getYear()
		int anioActual = Calendar.getInstance().get(Calendar.YEAR)
		return anioActual - anioNacimiento
	}

	def Categoria getCategoria(){
		return categoriaService.findByJugador(this)
	}
	def String getNombreCompleto(){
		apellido + ', ' + nombre
	}
	
	def String getEmail(){
		return this.username
	}
	
	def void setEmail(String email){
		this.username = email
	}
	
	def boolean equals(jugador){
		if (this.is(jugador)) return true

    	if (!jugador || getClass() != jugador.class) return false
    	
		return this.dni == jugador.dni
	}
	
	def String toString(){
		nombre + ' ' + apellido
	}
}
