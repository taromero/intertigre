
<%@ page import="intertigre.domain.Fixture" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fixture.label', default: 'Fixture')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-fixture" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-fixture" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list fixture">
			
				<g:if test="${fixtureInstance?.fechas}">
				<li class="fieldcontain">
					<span id="fechas-label" class="property-label"><g:message code="fixture.fechas.label" default="Fechas" /></span>
					
						<g:each in="${fixtureInstance.fechas}" var="f">
						<span class="property-value" aria-labelledby="fechas-label"><g:link controller="fecha" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${fixtureInstance?.grupos}">
				<li class="fieldcontain">
					<span id="grupos-label" class="property-label"><g:message code="fixture.grupos.label" default="Grupos" /></span>
					
						<g:each in="${fixtureInstance.grupos}" var="g">
						<span class="property-value" aria-labelledby="grupos-label"><g:link controller="grupo" action="show" id="${g.id}">${g?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${fixtureInstance?.categoria}">
				<li class="fieldcontain">
					<span id="categoria-label" class="property-label"><g:message code="fixture.categoria.label" default="Categoria" /></span>
					
						<span class="property-value" aria-labelledby="categoria-label"><g:link controller="categoria" action="show" id="${fixtureInstance?.categoria?.id}">${fixtureInstance?.categoria?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${fixtureInstance?.id}" />
					<g:link class="edit" action="edit" id="${fixtureInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
