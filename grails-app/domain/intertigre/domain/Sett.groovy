package intertigre.domain
import javax.persistence.Embeddable;

import org.grails.datastore.gorm.finders.MethodExpression.InRange;

class Sett { //no podia usar el nombre 'Set' porque genera problemas con GORM para generar la tabla
	Integer gamesGanador
	Integer gamesPerdedor
	
	static constraints = {
		gamesGanador range: 0..7, nullable: true
		gamesPerdedor range: 0..7, nullable: true
	}
	
	def String toString(){
		gamesGanador.toString() + '-' + gamesPerdedor.toString()
	}
}
