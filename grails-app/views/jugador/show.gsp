
<%@ page import="intertigre.domain.Jugador" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'jugador.label', default: 'Jugador')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-jugador" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="show-jugador" class="content scaffold-show" role="main" style="float:left; width:50%">
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list jugador">
			
				<g:if test="${jugadorInstance?.role}">
				<li class="fieldcontain">
					<span id="role-label" class="property-label"><g:message code="jugador.role.label" default="Role" /></span>
					
						<span class="property-value" aria-labelledby="role-label"><g:fieldValue bean="${jugadorInstance}" field="role"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.dni}">
				<li class="fieldcontain">
					<span id="dni-label" class="property-label"><g:message code="jugador.dni.label" default="Dni" /></span>
					
						<span class="property-value" aria-labelledby="dni-label"><g:fieldValue bean="${jugadorInstance}" field="dni"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="jugador.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${jugadorInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.apellido}">
				<li class="fieldcontain">
					<span id="apellido-label" class="property-label"><g:message code="jugador.apellido.label" default="Apellido" /></span>
					
						<span class="property-value" aria-labelledby="apellido-label"><g:fieldValue bean="${jugadorInstance}" field="apellido"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.sexo}">
				<li class="fieldcontain">
					<span id="sexo-label" class="property-label"><g:message code="jugador.sexo.label" default="Sexo" /></span>
					
						<span class="property-value" aria-labelledby="sexo-label"><g:fieldValue bean="${jugadorInstance}" field="sexo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.email}">
				<li class="fieldcontain">
					<span id="email-label" class="property-label"><g:message code="jugador.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${jugadorInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.club}">
				<li class="fieldcontain">
					<span id="club-label" class="property-label"><g:message code="jugador.club.label" default="Club" /></span>
					
						<span class="property-value" aria-labelledby="club-label"><g:link controller="club" action="show" id="${jugadorInstance?.club?.id}">${jugadorInstance?.club?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.nacimiento}">
				<li class="fieldcontain">
					<span id="nacimiento-label" class="property-label"><g:message code="jugador.nacimiento.label" default="Nacimiento" /></span>

						<span class="property-value" aria-labelledby="nacimiento-label"><g:formatDate format="dd/MM/yyyy" date="${jugadorInstance?.nacimiento}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${jugadorInstance?.telefono}">
				<li class="fieldcontain">
					<span id="telefono-label" class="property-label"><g:message code="jugador.telefono.label" default="Telefono" /></span>
					
						<span class="property-value" aria-labelledby="telefono-label"><g:fieldValue bean="${jugadorInstance}" field="telefono"/></span>
					
				</li>
				</g:if>
			
			</ol>
		</div>
		<div style="float:left; width:50%" ><img src="${jugadorInstance.urlImagen }" style="max-width:100%; max-height:100%;"/></div>
		<div>
			<g:form>
				<fieldset class="buttons footer">
					<g:hiddenField name="id" value="${jugadorInstance?.id}" />
					<g:if test="${Jugador.get(sec.loggedInUserInfo(field:'id').toLong()) == jugadorInstance}">
						<g:link class="edit" action="edit" id="${jugadorInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					</g:if>
					<g:else>
						<sec:ifAllGranted roles="ROLE_ADMIN">
							<g:link class="edit" action="edit" id="${jugadorInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						</sec:ifAllGranted>
					</g:else>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
