
<%@ page import="intertigre.domain.Club" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-club" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-club" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list club">
			
				<g:if test="${clubInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="club.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${clubInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clubInstance?.direccion}">
				<li class="fieldcontain">
					<span id="direccion-label" class="property-label"><g:message code="club.direccion.label" default="Direccion" /></span>
					
						<span class="property-value" aria-labelledby="direccion-label"><g:fieldValue bean="${clubInstance}" field="direccion"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clubInstance?.telefono}">
				<li class="fieldcontain">
					<span id="telefono-label" class="property-label"><g:message code="club.telefono.label" default="Telefono" /></span>
					
						<span class="property-value" aria-labelledby="telefono-label"><g:fieldValue bean="${clubInstance}" field="telefono"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clubInstance?.email}">
				<li class="fieldcontain">
					<span id="email-label" class="property-label"><g:message code="club.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${clubInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clubInstance?.localidad}">
				<li class="fieldcontain">
					<span id="localidad-label" class="property-label"><g:message code="club.localidad.label" default="Localidad" /></span>
					
						<span class="property-value" aria-labelledby="localidad-label"><g:fieldValue bean="${clubInstance}" field="localidad"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${clubInstance?.triosDeCanchasDisponibles}">
				<li class="fieldcontain">
					<span id="triosDeCanchasDisponibles-label" class="property-label"><g:message code="club.triosDeCanchasDisponibles.label" default="Trios De Canchas Disponibles" /></span>
					
						<span class="property-value" aria-labelledby="triosDeCanchasDisponibles-label"><g:fieldValue bean="${clubInstance}" field="triosDeCanchasDisponibles"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${clubInstance?.triosDeCanchasDisponibles}">
				<li class="fieldcontain">
					<span id="horariosPreferidosParaLocal-label" class="property-label">Horarios disponibles de canchas</span>
					
						<span class="property-value" aria-labelledby="horariosPreferidosParaLocal-label"><g:fieldValue bean="${clubInstance}" field="horariosPreferidosParaLocal"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${clubInstance?.id}" />
					<g:link class="edit" action="edit" id="${clubInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
