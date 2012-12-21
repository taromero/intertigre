
<%@ page import="intertigre.domain.Fecha" %>
<%@ page import="intertigre.domain.Jugador" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<style type="text/css">
			.fecha{
				text-align: center;
				width: 25%
			}
		</style>
		<title style='display:none'>Show Fecha</title>
	</head>
	<body>
		<div id="show-fecha" class="content scaffold-show" role="main">
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list fecha" style="float:left">

				<g:if test="${fechaInstance?.categoria}">
				<li class="fieldcontain">
					<span id="categoria-label" class="property-label"><g:message code="fecha.categoria.label" default="Categoria" /></span>
					
						<span class="property-value" aria-labelledby="categoria-label"><g:link controller="categoria" action="show" id="${fechaInstance?.categoria?.id}">${fechaInstance?.categoria?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${fechaInstance?.equipoLocal}">
				<li class="fieldcontain">
					<span id="equipoLocal-label" class="property-label"><g:message code="fecha.equipoLocal.label" default="Equipo Local" /></span>
					
						<span class="property-value" aria-labelledby="equipoLocal-label"><g:link controller="equipo" action="show" id="${fechaInstance?.equipoLocal?.id}">${fechaInstance?.equipoLocal?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${fechaInstance?.equipoVisitante}">
				<li class="fieldcontain">
					<span id="equipoVisitante-label" class="property-label"><g:message code="fecha.equipoVisitante.label" default="Equipo Visitante" /></span>
					
						<span class="property-value" aria-labelledby="equipoVisitante-label"><g:link controller="equipo" action="show" id="${fechaInstance?.equipoVisitante?.id}">${fechaInstance?.equipoVisitante?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<ol class="property-list fecha" style="float:left">
				<g:if test="${fechaInstance?.fechaDeJuego}">
				<li class="fieldcontain">
					<span id="fechaDeJuego-label" class="property-label"><g:message code="fecha.fechaDeJuego.label" default="Fecha De Juego" /></span>
					
						<span class="property-value" aria-labelledby="fechaDeJuego-label"><g:formatDate format="HH:mm dd/MM/yyyy" date="${fechaInstance?.fechaDeJuego}" /></span>
					
				</li>
				</g:if>
				<g:if test="${fechaInstance?.fechaReprogramacion}">
				<li class="fieldcontain">
					<span id="fechaReprogramacion-label" class="property-label"><g:message code="fecha.fechaReprogramacion.label" default="Fecha De Reprogramacion" /></span>
					
						<span class="property-value" aria-labelledby="fechaReprogramacion-label" id="fechaReprogramacion"><g:formatDate format="HH:mm dd/MM/yyyy" date="${fechaInstance?.fechaReprogramacion}" /></span>
					
				</li>
				</g:if>
				<g:if test="${fechaInstance?.fechaSubidaResultado}">
				<li class="fieldcontain">
					<span id="fechaSubidaResultado-label" class="property-label"><g:message code="fecha.fechaSubidaResultado.label" default="Fecha Subida Resultado" /></span>
					
						<span class="property-value" aria-labelledby="fechaSubidaResultado-label"><g:formatDate format="HH:mm dd/MM/yyyy" date="${fechaInstance?.fechaSubidaResultado}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${fechaInstance?.wo}">
				<li class="fieldcontain">
					<span id="wo-label" class="property-label"><g:message code="fechaInstance.wo.label" default="Wo" /></span>
					
						<span class="property-value" aria-labelledby="wo-label"><g:formatBoolean boolean="${fechaInstance?.wo}" /></span>
					
				</li>
				</g:if>
				<li class="fieldcontain">
					<span id="aprobadoPorRival-label" class="property-label"><g:message code="fecha.aprobadoPorRival.label" default="Aprobado Por Rival" /></span>
					
						<span class="property-value" aria-labelledby="aprobadoPorRival-label"><g:formatBoolean boolean="${fechaInstance?.aprobadoPorRival}" /></span>
					
				</li>
			</ol>
			<ol class="property-list fecha" style="float:left">
				<li class="fieldcontain">
					<span id="formacionIncorrectaLocal-label" class="property-label"><g:message code="fecha.formacionIncorrectaLocal.label" default="Formacion Incorrecta Local" /></span>
					
						<span class="property-value" aria-labelledby="formacionIncorrectaLocal-label"><g:formatBoolean boolean="${fechaInstance?.formacionIncorrectaLocal}" /></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="formacionIncorrectaVisitante-label" class="property-label"><g:message code="fecha.formacionIncorrectaVisitante.label" default="Formacion Incorrecta Visitante" /></span>
					
						<span class="property-value" aria-labelledby="formacionIncorrectaVisitante-label"><g:formatBoolean boolean="${fechaInstance?.formacionIncorrectaVisitante}" /></span>
					
				</li>
				<g:if test="${fechaInstance?.aprobadoPorAdmin}">			
				<li class="fieldcontain">
					<span id="aprobadoPorAdmin-label" class="property-label"><g:message code="fecha.aprobadoPorAdmin.label" default="Aprobado Por Admin" /></span>
					
						<span class="property-value" aria-labelledby="aprobadoPorAdmin-label"><g:formatBoolean boolean="${fechaInstance?.aprobadoPorAdmin}" /></span>
					
				</li>
				</g:if>
				
				<li class="fieldcontain">
					<span id="desaprobadoPorRival-label" class="property-label">Resultado desaprobado</span>
					
						<span class="property-value" aria-labelledby="desaprobadoPorRival-label"><g:formatBoolean boolean="${fechaInstance?.desaprobadoPorRival}" /></span>
					
				</li>
				
			</ol>
			<table>
				<thead>
					<th>Partido</th>
					<th>Local</th>
					<th>Visitante</th>
					<th>Resultado</th>
					<th>Abandono</th>
					<th>Ganador</th>
				</thead>
				<tbody>
					<tr class="even">
						<td>
							<label>Single 1</label>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single1?.jugadorLocal}</span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single1?.jugadorVisitante}</span>
						</td>
						<td>
							<g:if test="${ fechaInstance?.single1?.primerSet?.gamesGanador != null }">
								<span class="property-value" aria-labelledby="wo-label">
									${fechaInstance?.single1?.primerSet?.gamesGanador + '-' +
										 fechaInstance?.single1?.primerSet?.gamesPerdedor + ' ' +
										 fechaInstance?.single1?.segundoSet?.gamesGanador + '-' +
										 fechaInstance?.single1?.segundoSet?.gamesPerdedor}
								</span>
								<g:if test="${fechaInstance.single1?.tercerSet != null}">
									${' ' + fechaInstance?.single1?.tercerSet?.gamesGanador + '-' +
										 fechaInstance?.single1?.tercerSet?.gamesPerdedor }	
								</g:if>
							</g:if>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label"><g:formatBoolean boolean="${fechaInstance?.single1?.abandono}" /></span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single1?.equipoGanador}</span>
						</td>
					</tr>
					<tr class="odd">
						<td>
							<label>Single 2</label>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single2?.jugadorLocal}</span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single2?.jugadorVisitante}</span>
						</td>
						<td>
							<g:if test="${ fechaInstance?.single2?.primerSet?.gamesGanador != null }">
								<span class="property-value" aria-labelledby="wo-label">
									${fechaInstance?.single2?.primerSet?.gamesGanador + '-' +
										 fechaInstance?.single2?.primerSet?.gamesPerdedor + ' ' +
										 fechaInstance?.single2?.segundoSet?.gamesGanador + '-' +
										 fechaInstance?.single2?.segundoSet?.gamesPerdedor}
								</span>
								<g:if test="${fechaInstance.single2?.tercerSet != null}">
									${' ' + fechaInstance?.single2?.tercerSet?.gamesGanador + '-' +
										 fechaInstance?.single2?.tercerSet?.gamesPerdedor }	
								</g:if>
							</g:if>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label"><g:formatBoolean boolean="${fechaInstance?.single2?.abandono}" /></span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.single2?.equipoGanador}</span>
						</td>
					</tr>
					<tr class="even">
						<td>
							<label>Doble</label>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.doble?.parejaLocal?.doblista1}</span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.doble?.parejaVisitante?.doblista1}</span>
						</td>
						<td>
							<g:if test="${ fechaInstance?.doble?.primerSet?.gamesGanador != null }">
								<span class="property-value" aria-labelledby="wo-label">
									${fechaInstance?.doble?.primerSet?.gamesGanador + '-' +
										 fechaInstance?.doble?.primerSet?.gamesPerdedor + ' ' +
										 fechaInstance?.doble?.segundoSet?.gamesGanador + '-' +
										 fechaInstance?.doble?.segundoSet?.gamesPerdedor + ' '}
								<g:if test="${fechaInstance.doble?.tercerSet != null}">
									${' ' + fechaInstance?.doble?.tercerSet?.gamesGanador + '-' +
										 fechaInstance?.doble?.tercerSet?.gamesPerdedor }	
								</g:if>
							</g:if>
							</span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label"><g:formatBoolean boolean="${fechaInstance?.doble?.abandono}" /></span>							</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.doble?.equipoGanador}</span>
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.doble?.parejaLocal?.doblista2}</span>
						</td>
						<td>
							<span class="property-value" aria-labelledby="wo-label">${fechaInstance?.doble?.parejaVisitante?.doblista2}</span>
						</td>
						<td></td>
						<td></td>
					</tr>
				</tbody>
			</table>
				
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${fechaInstance?.id}" />
					<sec:ifAllGranted roles="ROLE_ADMIN">
						<g:link class="edit" action="aprobarComoAdmin" id="${fechaInstance?.id}">Aprobar como Admin</g:link>
						<g:link class="edit" action="edit" id="${fechaInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</sec:ifAllGranted>
					<sec:ifAllGranted roles="ROLE_CAPITAN_EQUIPO">
						<g:if test="${fechaInstance?.equipoGanador == null || 
										Jugador.get(sec.loggedInUserInfo(field:'id').toLong()).equipos.contains(fechaInstance?.equipoGanador)}">
							<g:link class="edit" action="createPartido" id="${fechaInstance?.id}">Editar resultados</g:link>
						</g:if>
						<g:if test="${Jugador.get(sec.loggedInUserInfo(field:'id').toLong()).equipos.contains(fechaInstance?.equipoPerdedor)}">
							<g:link class="edit" action="aprobarComoRival" id="${fechaInstance?.id}">Aprobar resultado</g:link>
							<g:link class="edit" action="desaprobar" id="${fechaInstance?.id}">Desaprobar resultado</g:link>
						</g:if>
						<g:link class="edit reprogramar" action="pedirReprogramacionFecha" id="${fechaInstance?.id}">Pedir reprogramacion</g:link>
					</sec:ifAllGranted>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
