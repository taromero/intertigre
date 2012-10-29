package intertigre.services
import intertigre.domain.Categoria;
import intertigre.domain.Jugador;


class CategoriaService {

	def Categoria findByJugador(Jugador jugador){
		return Categoria.find { edadLimiteInferior >= jugador.edadAlFinalizarAnio &&
							     edadLimiteSuperior <= jugador.edadAlFinalizarAnio &&
								 sexo == jugador.sexo }
	}
}
