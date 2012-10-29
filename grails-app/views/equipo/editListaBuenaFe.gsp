<%@ page import="intertigre.domain.Equipo"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<title>Lista de Buena Fe</title>
</head>
<body>
	<style>
		.connectedSortable { float: left; list-style-type: none; margin: 0; padding: 0; width: 50%; }
		.connectedSortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 1.4em; height: 18px; }
		.connectedSortable li span { position: absolute; margin-left: -1.3em; }
	</style>
	<script>
		$(function() {
			$( "#jugadoresClub, #jugadoresEquipo" ).sortable({
				connectWith: ".connectedSortable"
			}).disableSelection();
			$('.save').click(function(){
				$('#listaEditada').val($('#jugadoresEquipo').sortable('toArray'));
			})
			$('#busquedaJugador').keyup(function(){
				var textoBusqueda = $('#busquedaJugador').val().toLowerCase()
				$('.jugadoresClub').each(function(index){
					var texto = $(this).html().split(' ')
					var contiene = false
					for(var i = 0; i < texto.length; i++){
						var palabra = texto[i].toLowerCase()
						if(palabra.indexOf(textoBusqueda) != -1){
							contiene = true
						}
					}
					if(!contiene){
						$(this).hide()
					}else{
						$(this).show()
					}
				})
			})
		});
	</script>
	<a href="#create-equipo" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
		</ul>
	</div>
	<div id="edit-listaBuenaFe" class="content scaffold-edit" role="main">
		<h1>Editar Lista De Buena Fe</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${equipoInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${equipoInstance}" var="error">
					<li
						<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
							error="${error}" /></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form method="post">
			<g:hiddenField name="id" value="${equipoInstance?.id}" />
			<g:hiddenField name="version" value="${equipoInstance?.version}" />
			<g:hiddenField name="listaEditada" value="" />
			<label>Filtrar: </label><input type="text" id="busquedaJugador">
			<fieldset class="form">
				
				<ul id="jugadoresClub" class="connectedSortable" style="float: left;height: 300px;overflow:auto">
					<g:each in="${jugadoresClub}" var="j">
						<li id="${j.dni }" title="${j.dni }" class="ui-state-default jugadores jugadoresClub">${j.nombre + ' ' + j.apellido }</li>
					</g:each>
				</ul>

				<ul id="jugadoresEquipo" class="connectedSortable" style="float: left;height: 300px;overflow:auto">
					<g:each in="${equipoInstance.itemsListaBuenaFe*.jugador}" var="j">
						<li id="${j.dni }" title="${j.dni }" class="ui-state-highlight jugadores">${j.nombre + ' ' + j.apellido }</li>
					</g:each>
				</ul>

			</fieldset>
			<fieldset class="buttons">
				<g:actionSubmit class="save" action="update"
					value="${message(code: 'default.button.update.label', default: 'Update')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>