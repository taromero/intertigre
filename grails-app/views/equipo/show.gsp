
<%@ page import="intertigre.domain.Equipo" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'equipo.label', default: 'Equipo')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<script type="text/javascript">
			function cambiarCapitanEquipo(){
				$.ajax({
					url:"${request.contextPath}/equipo/cambiarCapitanEquipoAjax",
					dataType: 'json',
					data: {
						idNuevoCapitan: $('#nuevoCapitan').val(),
						idEquipo: ${equipoInstance?.id}
					},
					success: function(nuevoCapitan) {
						$('#cambiarCapitanModal').dialog('destroy');
						$('#capitanSpan').html('<a href="${request.contextPath}/jugador/show' + nuevoCapitan.id + '">' + nuevoCapitan.nombre + ' ' + nuevoCapitan.apellido + '</a>')
<%--						alert(nuevoCapitan.nombre + ' ' + nuevoCapitan.apellido);--%>
					},
					error: function(request, status, error) {
						alert(error);
					}
				});
			}

			function cambiarCapitan(){
				$('#cambiarCapitanModal').dialog({
					height: 140,
					modal: true,
					title: 'Cambiar capitan del equipo'
				});
			}
		</script>
		<a href="#show-equipo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-equipo" class="content scaffold-show" role="main">
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<div style="float:left;width:50%">
			<ol class="property-list equipo">
			
				<g:if test="${equipoInstance?.club}">
				<li class="fieldcontain">
					<span id="club-label" class="property-label"><g:message code="equipo.club.label" default="Club" /></span>
					
						<span class="property-value" aria-labelledby="club-label"><g:link controller="club" action="show" id="${equipoInstance?.club?.id}">${equipoInstance?.club?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${equipoInstance?.categoria}">
				<li class="fieldcontain">
					<span id="categoria-label" class="property-label"><g:message code="equipo.categoria.label" default="Categoria" /></span>
					
						<span class="property-value" aria-labelledby="categoria-label"><g:link controller="categoria" action="show" id="${equipoInstance?.categoria?.id}">${equipoInstance?.categoria?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
				
				<g:if test="${equipoInstance?.grupo}">
				<li class="fieldcontain">
					<span id="grupo-label" class="property-label">Grupo</span>
					
						<span class="property-value" aria-labelledby="grupo-label"><g:link controller="grupo" action="show" id="${equipoInstance?.grupo?.id}">${equipoInstance?.grupo?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${equipoInstance?.jerarquia}">
				<li class="fieldcontain">
					<span id="jerarquia-label" class="property-label"><g:message code="equipo.jerarquia.label" default="Jerarquia" /></span>
					
						<span class="property-value" aria-labelledby="jerarquia-label"><g:fieldValue bean="${equipoInstance}" field="jerarquia"/></span>
					
				</li>
				</g:if>
			
				<li class="fieldcontain">
					<span id="capitan-label" class="property-label"><g:message code="equipo.capitan.label" default="Capitan" /></span>
					<g:if test="${equipoInstance?.capitan}">
						
							<span class="property-value" aria-labelledby="capitan-label" id="capitanSpan"><g:link controller="jugador" action="show" id="${equipoInstance?.capitan?.id}">${equipoInstance?.capitan?.encodeAsHTML()}</g:link></span>
					</g:if>
					<button onclick="cambiarCapitan()">Cambiar capitan</button>
				</li>
				
				<li class="fieldcontain">
					<span id="estaBienFormado-label" class="property-label"><g:message code="equipo.estaBienFormado.label" default="Esta Bien Formado" /></span>
					
						<span class="property-value" aria-labelledby="estaBienFormado-label"><g:formatBoolean boolean="${equipoInstance?.estaBienFormado}" /></span>
					
				</li>
				
				<li class="fieldcontain">
					<span id="estaConfirmado-label" class="property-label"><g:message code="equipo.estaConfirmado.label" default="Esta Confirmado" /></span>
					
						<span class="property-value" aria-labelledby="estaConfirmado-label"><g:formatBoolean boolean="${equipoInstance?.estaConfirmado}" /></span>
					
				</li>
				<g:if test="${equipoInstance?.grupo}">
				<li class="fieldcontain">
					<span id="grupo-label" class="property-label"><g:message code="equipo.grupo.label" default="Grupo" /></span>
					
						<span class="property-value" aria-labelledby="grupo-label"><g:link controller="grupo" action="show" id="${equipoInstance?.grupo?.id}">${equipoInstance?.grupo?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			</ol>
			</div>
			<div style="float:left;width:50%">
				<ol class="property-list equipo">
				<li class="fieldcontain">
					<span id="itemsListaBuenaFe-label" class="property-label">Lista de Buena Fe</span>
					<span class="property-value" aria-labelledby="grupo-label"><g:link action="editListaBuenaFe" id="${equipoInstance?.id}">Editar</g:link></span>
						<table id="listaBuenaFe">
							<thead>
								<th>Nombre</th>
							</thead>
							<tbody>
								<g:each in="${equipoInstance.itemsListaBuenaFe*.jugador}" status="i" var="jugador">
									<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
										<td class="itemListaBuenaFe">
											<g:link controller="jugador" action="show" id="${jugador.id}" name="${jugador.id}">
												${jugador.nombre + ' ' + jugador.apellido }
											</g:link>
										</td>
									</tr>
								</g:each>
							</tbody>
						</table>
					
				</li>
				
			</div>
				<g:if test="${equipoInstance?.fechas}">
					<span id="fechas-label" class="property-label" style="text-align:left"><g:message code="equipo.fechas.label" default="Fechas" /></span>
						<table>
							<thead>
								<th>Equipo Local</th>
								<th>Equipo Visitante</th>
								<th>Fecha</th>
								<th></th>
							</thead>
							<tbody>
								<g:each in="${equipoInstance.fechas}" status="i" var="f">
									<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
										<td>${f.equipoLocal.encodeAsHTML() }</td>
										<td>${f.equipoVisitante.encodeAsHTML() }</td>
										<td><g:formatDate format="HH:mm dd/MM/yyyy" date="${f.fechaDeJuego}"/></td>
										<td>
											<span class="property-value" aria-labelledby="fechas-label">
												<g:link controller="fecha" action="show" id="${f.id}">Ver</g:link>
											</span>
										</td>
									</tr>
								</g:each>
							</tbody>
						</table>
				</g:if>
			<g:form>
				<fieldset class="buttons footer">
					<g:hiddenField name="id" value="${equipoInstance?.id}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
		<div id="cambiarCapitanModal" style="display:none">
			<g:select id="nuevoCapitan" name="Capitan" from="${equipoInstance?.jugadores}" optionKey="id"/>
			<button onclick="cambiarCapitanEquipo()">Elegir</button>
		</div>
	</body>
</html>
