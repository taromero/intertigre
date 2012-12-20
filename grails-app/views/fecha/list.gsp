
<%@ page import="intertigre.domain.Fecha" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fecha.label', default: 'Fecha')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-fecha" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div>
			<g:form action="list" >
				<g:if test="${equipos?.size() > 1}">
					<g:select id="equipo" name="equipo.id" from="${equipos}" optionKey="id" value="${idEquipoSeleccionado}" class="many-to-one" noSelection="['null': 'TODOS']"/>
					<fieldset class="buttons">
						<g:submitButton name="list" class="list" value="Buscar Fechas" />
					</fieldset>
				</g:if>
				<g:else>
					<title> ${ equipos?.get(0) } </title>
				</g:else>
			</g:form>
		</div>
		<div id="list-fecha" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th> Equipo Local </th>
					
						<th> Equipo Visitante </th>
						
						<th> Fecha de juego </th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${fechaInstanceList}" status="i" var="fechaInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'} fechaItem">
					
						<td><g:link controller="equipo" action="show" id="${fechaInstance.equipoLocal.id}">${fieldValue(bean: fechaInstance, field: "equipoLocal")}</g:link></td>
					
						<td><g:link controller="equipo" action="show" id="${fechaInstance.equipoVisitante.id}">${fieldValue(bean: fechaInstance, field: "equipoVisitante")}</g:link></td>
						
						<td><g:link action="show" id="fechaDeJuego${fechaInstance.id}">${fieldValue(bean: fechaInstance, field: "fechaDeJuego")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${fechaInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
