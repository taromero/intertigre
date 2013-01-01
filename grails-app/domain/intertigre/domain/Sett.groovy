package intertigre.domain

class Sett { //no podia usar el nombre 'Set' porque genera problemas con GORM para generar la tabla
	Integer gamesGanador
	Integer gamesPerdedor
	
	static constraints = {
		gamesGanador range: 0..7, nullable: true
		gamesPerdedor range: 0..7, nullable: true
	}

	def Boolean terminoDeJugarse() {
		return gamesGanador in [7, 6, null] || gamesPerdedor in [7, 6, null]
	}

	def Boolean esCorrectaLaDiferenciaDeGames() {
		if(!esTiebreak() && !esSieteCinco()){
			return Math.abs(gamesGanador - gamesPerdedor) >= 2 && gamesGanador != 7 && gamesPerdedor != 7
		} else {
			return true
		}
	}

	def Boolean esTiebreak() {
		return gamesGanador != gamesPerdedor && ((gamesGanador == 7 && gamesPerdedor == 6) || (gamesGanador == 6 && gamesPerdedor == 7))
	}
	
	def Boolean esSieteCinco() {
		return gamesGanador != gamesPerdedor && ((gamesGanador == 7 && gamesPerdedor == 5) || (gamesGanador == 5 && gamesPerdedor == 7))
	}
	
	def String toString(){
		gamesGanador.toString() + '-' + gamesPerdedor.toString()
	}
}
