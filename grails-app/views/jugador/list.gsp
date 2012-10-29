
<%@ page import="intertigre.domain.Jugador" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'jugador.label', default: 'Jugador')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-jugador" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-jugador" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="username" title="${message(code: 'jugador.username.label', default: 'Username')}" />
					
						<g:sortableColumn property="dni" title="${message(code: 'jugador.dni.label', default: 'Dni')}" />
					
						<g:sortableColumn property="nombre" title="${message(code: 'jugador.nombre.label', default: 'Nombre')}" />
					
						<g:sortableColumn property="apellido" title="${message(code: 'jugador.apellido.label', default: 'Apellido')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${jugadorInstanceList}" status="i" var="jugadorInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${jugadorInstance.id}">${fieldValue(bean: jugadorInstance, field: "username")}</g:link></td>
					
						<td>${fieldValue(bean: jugadorInstance, field: "dni")}</td>
					
						<td>${fieldValue(bean: jugadorInstance, field: "nombre")}</td>
					
						<td>${fieldValue(bean: jugadorInstance, field: "apellido")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${jugadorInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
