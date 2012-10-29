
<%@ page import="intertigre.domain.Equipo" %>
<%@ page import="intertigre.domain.Club" %>
<%@ page import="intertigre.domain.Categoria" %>
a<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'equipo.label', default: 'Equipo')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-equipo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div>
			<g:form action="list" >
				<g:select id="idClub" name="club.id" from="${Club.list(sort: 'nombre')}" optionKey="id" value="${idClubSeleccionado}" class="many-to-one" noSelection="['false': 'Filtro Club']"/>
				<g:select id="idCategoria" name="categoria.id" from="${Categoria.list()}" optionKey="id" value="${idCategoriaSeleccionada}" class="many-to-one" noSelection="['false': 'Filtro Categoria']"/>
				<g:select id="jerarquia" name="jerarquia" from="${'A'..'Z'}" value="${jerarquiaSeleccionada}" class="many-to-one" noSelection="['false': 'Filtro Jerarquia']"/>
				<fieldset class="buttons">
					<g:submitButton name="list" class="list" value="Buscar Equipos" />
				</fieldset>
			</g:form>
		</div>
		<div id="list-equipo" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="equipo.club.label" default="Club" /></th>
					
						<th><g:message code="equipo.categoria.label" default="Categoria" /></th>
					
						<g:sortableColumn property="jerarquia" title="${message(code: 'equipo.jerarquia.label', default: 'Jerarquia')}" />
					
						<th><g:message code="equipo.capitan.label" default="Capitan" /></th>
					
						<g:sortableColumn property="estaBienFormado" title="${message(code: 'equipo.estaBienFormado.label', default: 'Esta Bien Formado')}" />
					
						<g:sortableColumn property="estaConfirmado" title="${message(code: 'equipo.estaConfirmado.label', default: 'Esta Confirmado')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${equipoInstanceList}" status="i" var="equipoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${equipoInstance.id}">${fieldValue(bean: equipoInstance, field: "club")}</g:link></td>
					
						<td>${fieldValue(bean: equipoInstance, field: "categoria")}</td>
					
						<td>${fieldValue(bean: equipoInstance, field: "jerarquia")}</td>
					
						<td>${fieldValue(bean: equipoInstance, field: "capitan")}</td>
					
						<td><g:formatBoolean boolean="${equipoInstance.estaBienFormado}" /></td>
					
						<td><g:formatBoolean boolean="${equipoInstance.estaConfirmado}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${equipoInstanceTotal}" params='${["club.id": idClubSeleccionado, "categoria.id": idCategoriaSeleccionada, "jerarquia": jerarquiaSeleccionada ]}'/>
			</div>
		</div>
	</body>
</html>
