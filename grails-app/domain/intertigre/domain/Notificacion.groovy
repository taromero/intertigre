package intertigre.domain

class Notificacion{
	Date fecha
	String titulo
	String mensaje

	static constraints = {
		mensaje maxSize: 500
	}
}