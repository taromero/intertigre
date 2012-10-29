
<%@ page import="intertigre.domain.Club" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-club" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-club" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="nombre" title="${message(code: 'club.nombre.label', default: 'Nombre')}" />
					
						<g:sortableColumn property="direccion" title="${message(code: 'club.direccion.label', default: 'Direccion')}" />
					
						<g:sortableColumn property="telefono" title="${message(code: 'club.telefono.label', default: 'Telefono')}" />
					
						<g:sortableColumn property="email" title="${message(code: 'club.email.label', default: 'Email')}" />
					
						<g:sortableColumn property="localidad" title="${message(code: 'club.localidad.label', default: 'Localidad')}" />
					
						<g:sortableColumn property="triosDeCanchasDisponibles" title="${message(code: 'club.triosDeCanchasDisponibles.label', default: 'Trios De Canchas Disponibles')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${clubInstanceList}" status="i" var="clubInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${clubInstance.id}">${fieldValue(bean: clubInstance, field: "nombre")}</g:link></td>
					
						<td>${fieldValue(bean: clubInstance, field: "direccion")}</td>
					
						<td>${fieldValue(bean: clubInstance, field: "telefono")}</td>
					
						<td>${fieldValue(bean: clubInstance, field: "email")}</td>
					
						<td>${fieldValue(bean: clubInstance, field: "localidad")}</td>
					
						<td>${fieldValue(bean: clubInstance, field: "triosDeCanchasDisponibles")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${clubInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
