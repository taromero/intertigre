package intertigre.domain

import static intertigre.util.DomainFactoryService.createFecha

class PartidoSpec extends BaseIntegrationSpec{

	static Equipo equipoL = Equipo.build()
	static Equipo equipoV = Equipo.build()
	static Fecha fecha = createFecha(equipoL, equipoV, new Date())
	
	def 'el ultimo set de un partido debe tener un numero de games mas grande al final que al principio (6-3 y no 3-6)'() {
		given: 'un partido donde el ganador pierde el ultimo set'
			def partido = singleODoble
		when: 'guardo el partido'
			partido.save()
		then: 'no se deberia guardar'
			Partido.findAll().size() == 0
		and: 'deberia mostrar un mensaje de error acorde'
			partido.errors.allErrors.toString().contains('El ultimo set debe tener un numero mayor de ' + 
															'games al principio que final (por ej. 6-3 y no 3-6)')
		where: 'el partido es un single o un doble'
			singleODoble << [Single.buildWithoutSave(primerSet: Sett.build(gamesGanador: 7, gamesPerdedor: 5), 
									segundoSet: Sett.build(gamesGanador: 6, gamesPerdedor: 4), 
									tercerSet: Sett.build(gamesGanador: 3, gamesPerdedor: 6), fecha: fecha, equipoGanador: equipoL),
							 Doble.buildWithoutSave(primerSet: Sett.build(gamesGanador: 7, gamesPerdedor: 5), 
									segundoSet: Sett.build(gamesGanador: 6, gamesPerdedor: 4), 
									tercerSet: Sett.build(gamesGanador: 3, gamesPerdedor: 6), fecha: fecha, equipoGanador: equipoL)]
	}

}
