
import intertigre.domain.Categoria
import intertigre.domain.Fixture
import intertigre.domain.Jugador
import intertigre.domain.Notificacion
import intertigre.security.SecRole
import intertigre.security.SecUserSecRole

import org.joda.time.DateTime

class BootStrap {

	def springSecurityService
	
	def domainFactoryService
	
	def fixtureService

	def grailsApplication
		
	def init = { servletContext ->
		environments {
            development {
				def dbCreateProperty = grailsApplication.config.dataSource.dbCreate
				if(dbCreateProperty == 'create' || dbCreateProperty == 'create-drop'){
					//Cargar roles
					def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
					def capitanClubRole = SecRole.findByAuthority('ROLE_CAPITAN_CLUB') ?: new SecRole(authority: 'ROLE_CAPITAN_CLUB').save(failOnError: true)
					def capitanEquipoRole = SecRole.findByAuthority('ROLE_CAPITAN_EQUIPO') ?: new SecRole(authority: 'ROLE_CAPITAN_EQUIPO').save(failOnError: true)
					def jugadorRole = SecRole.findByAuthority('ROLE_JUGADOR') ?: new SecRole(authority: 'ROLE_JUGADOR').save(failOnError: true)
					
					domainFactoryService.crearCategorias()
					domainFactoryService.crearClubes()
					domainFactoryService.crearXCantidadEquiposDeCategoria(35, Categoria.find{ nombre == '+25' && sexo == 'M' })
					domainFactoryService.crearEquipoMas19MCanotto()
					domainFactoryService.crearEquipoMas19MElChasqui()
					Fixture fixture = fixtureService.generarFixture(Categoria.find{ nombre == '+19' && sexo == 'M' })
					fixture.save()
					
					def tomas = Jugador.findByUsername('canotto90@gmail.com')
					SecUserSecRole.create tomas, adminRole
					tomas.role = 'Administrador'
	
					def nadal = Jugador.findByUsername('nadal@gmail.com')
					SecUserSecRole.create nadal, capitanClubRole
					nadal.role = 'Capitan club'
	
					def moya = Jugador.findByUsername('moya@gmail.com')
					SecUserSecRole.create moya, capitanEquipoRole
					moya.role = 'Capitan equipo'
	
					new Notificacion(fecha: new Date(), titulo: 'Comienzo ficticio torneo', 
										mensaje: 'Son cosas chiquitas. No acaban con la pobreza, no nos sacan del subdesarrollo, ' + 
													'no socializan los medios de producción y de cambio, no expropian las cuevas de ' + 
													'Alí Babá. Pero quizá desencadenen la alegría de hacer, y la traduzcan en actos.' + 
													' Y al fin y al cabo, actuar sobre la realidad y cambiarla, aunque sea un poquito, ' + 
													'es la única manera de probar que la realidad es transformable.').save()
					new Notificacion(fecha: new DateTime().plusDays(2).toDate(), titulo: 'fecha limite para modificar listas', 
										mensaje: 'Las bombas inteligentes, que tan burras parecen, son las que más saben. Ellas han ' + 
													'revelado la verdad de la invasión. Mientras Rumsfeld decía: “Estos son bombardeos ' + 
													'humanitarios”, las bombas destripaban niños y arrasaban mercados callejeros.').save()
				}
            }
			production {
				def dbCreateProperty = grailsApplication.config.dataSource.dbCreate
				if(dbCreateProperty == 'create' || dbCreateProperty == 'create-drop'){
					//Cargar roles
					def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
					def capitanClubRole = SecRole.findByAuthority('ROLE_CAPITAN_CLUB') ?: new SecRole(authority: 'ROLE_CAPITAN_CLUB').save(failOnError: true)
					def capitanEquipoRole = SecRole.findByAuthority('ROLE_CAPITAN_EQUIPO') ?: new SecRole(authority: 'ROLE_CAPITAN_EQUIPO').save(failOnError: true)
					def jugadorRole = SecRole.findByAuthority('ROLE_JUGADOR') ?: new SecRole(authority: 'ROLE_JUGADOR').save(failOnError: true)
					
					domainFactoryService.crearCategorias()
					domainFactoryService.crearClubes()
					domainFactoryService.crearXCantidadEquiposDeCategoria(35, Categoria.find{ nombre == '+19' && sexo == 'M' })
					domainFactoryService.crearEquipoMas19MCanotto()
					domainFactoryService.crearEquipoMas19MElChasqui()
					fixtureService.generarFixture(Categoria.find{ nombre == '+25' && sexo == 'M' }).save()
					
					def tomas = Jugador.findByUsername('canotto90@gmail.com')
					SecUserSecRole.create tomas, adminRole
					tomas.role = 'Administrador'
	
					def nadal = Jugador.findByUsername('nadal@gmail.com')
					SecUserSecRole.create nadal, capitanClubRole
					nadal.role = 'Capitan club'
	
					def moya = Jugador.findByUsername('moya@gmail.com')
					SecUserSecRole.create moya, capitanEquipoRole
					moya.role = 'Capitan equipo'
					
					new Notificacion(fecha: new Date(), titulo: 'Comienzo ficticio torneo',
						mensaje: 'Son cosas chiquitas. No acaban con la pobreza, no nos sacan del subdesarrollo, ' +
									'no socializan los medios de producción y de cambio, no expropian las cuevas de ' +
									'Alí Babá. Pero quizá desencadenen la alegría de hacer, y la traduzcan en actos.' +
									' Y al fin y al cabo, actuar sobre la realidad y cambiarla, aunque sea un poquito, ' +
									'es la única manera de probar que la realidad es transformable.').save()
					new Notificacion(fecha: new DateTime().plusDays(2).toDate(), titulo: 'fecha limite para modificar listas',
						mensaje: 'Las bombas inteligentes, que tan burras parecen, son las que más saben. Ellas han ' +
									'revelado la verdad de la invasión. Mientras Rumsfeld decía: “Estos son bombardeos ' +
									'humanitarios”, las bombas destripaban niños y arrasaban mercados callejeros.').save()
				}
			}
		}
	}
	def destroy = {
	}
}
