
<%@ page import="intertigre.domain.Fecha" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'fecha.label', default: 'Fecha')}" />
		<title>Reprogramar Fechas</title>
	</head>
	<body>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#checkearTodos').click(function(){
					if($('#checkearTodos').prop('checked')) {
						$('input:checkbox').prop('checked', true);
					} else {
						$('input:checkbox').prop('checked', false);
					}
					
				})
			})
        </script>
		<a href="#list-fecha" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-fecha" class="content scaffold-list" role="main">
			<h1>Reprogramar Fechas</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:form method="post" >

				<table>
					<thead>
						<tr>
						
							<th> Equipo Local </th>
						
							<th> Equipo Visitante </th>
							
							<th> Fecha de juego </th>

							<th> Reprogramar? <input type="checkbox" id="checkearTodos"></th>
						
						</tr>
					</thead>
					<tbody>
					<g:each in="${fechas}" status="i" var="fechaInstance">
						<tr class="fecha ${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td><g:link controller="equipo" action="show" id="${fechaInstance.equipoLocal.id}">${fieldValue(bean: fechaInstance, field: "equipoLocal")}</g:link></td>
						
							<td><g:link controller="equipo" action="show" id="${fechaInstance.equipoVisitante.id}">${fieldValue(bean: fechaInstance, field: "equipoVisitante")}</g:link></td>
							
							<td><g:link action="show" id="${fechaInstance.id}">${fieldValue(bean: fechaInstance, field: "fechaDeJuego")}</g:link></td>

							<td><g:checkBox name="ids[]" id="id${fechaInstance.id}" value="${fechaInstance.id}" checked="unchecked"/></td>
						
						</tr>
					</g:each>
					</tbody>
				</table>
					<fieldset class="buttons">
					<g:actionSubmit class="save" action="reprogramarFechasMasivamente" id="reprogramar" value="Reprogramar seleccionadas" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
