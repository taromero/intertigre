
<%@ page import="intertigre.domain.Categoria" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'categoria.label', default: 'Categoria')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-categoria" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-categoria" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list categoria">
			
				<g:if test="${categoriaInstance?.edadLimiteInferior}">
				<li class="fieldcontain">
					<span id="edadLimiteInferior-label" class="property-label"><g:message code="categoria.edadLimiteInferior.label" default="Edad Limite Inferior" /></span>
					
						<span class="property-value" aria-labelledby="edadLimiteInferior-label"><g:fieldValue bean="${categoriaInstance}" field="edadLimiteInferior"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${categoriaInstance?.edadLimiteSuperior}">
				<li class="fieldcontain">
					<span id="edadLimiteSuperior-label" class="property-label"><g:message code="categoria.edadLimiteSuperior.label" default="Edad Limite Superior" /></span>
					
						<span class="property-value" aria-labelledby="edadLimiteSuperior-label"><g:fieldValue bean="${categoriaInstance}" field="edadLimiteSuperior"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${categoriaInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="categoria.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${categoriaInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${categoriaInstance?.sexo}">
				<li class="fieldcontain">
					<span id="sexo-label" class="property-label"><g:message code="categoria.sexo.label" default="Sexo" /></span>
					
						<span class="property-value" aria-labelledby="sexo-label"><g:fieldValue bean="${categoriaInstance}" field="sexo"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${categoriaInstance?.id}" />
					<g:link class="edit" action="edit" id="${categoriaInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
