
<%@ page import="intertigre.domain.Fixture" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fixture.label', default: 'Fixture')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-fixture" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-fixture" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="fixture.categoria.label" default="Categoria" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${fixtureInstanceList}" status="i" var="fixtureInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${fixtureInstance.id}">${fieldValue(bean: fixtureInstance, field: "categoria")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${fixtureInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
