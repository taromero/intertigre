<%@ page import="intertigre.domain.Club" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'club.label', default: 'Club')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<script type="text/javascript">
			function checkHorariosFormat(){
				if($('#horariosPreferidosParaLocal').val().match(/\[([0-9]*, )*[0-9]*\]/) != null &&
						$('#horariosPreferidosParaLocal').val().match(/\[([0-9]*, )*[0-9]*\]/)[0] == $('#horariosPreferidosParaLocal').val()){
					return true
				}else{
					alert('Los horarios deben respetar el formato "[hora1, hora2, horan]", por ejemplo: [10, 16, 14], los corchetes son importantes')
					return false
				}
			}
		</script>
	</head>
	<body>
		<a href="#edit-club" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="edit-club" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${clubInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${clubInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" onSubmit="return checkHorariosFormat()">
				<g:hiddenField name="id" value="${clubInstance?.id}" />
				<g:hiddenField name="version" value="${clubInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
