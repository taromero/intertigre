<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'single.label', default: 'Single')}" />
		<title>Crear Partidos</title>
		<style type="text/css">
			.sett{
				width: 20.5%
			}
		</style>
	</head>
	<body>
		<a href="#create-single" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-single" class="content scaffold-create" role="main">
			<h1>Crear Partidos</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${fecha}">
			<ul class="errors" role="alert">
				<g:eachError bean="${fecha}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="savePartidos" id="${fecha?.id}">
				<input type="hidden" name="fechaId" value="${fechaId}"/>
				<table>
					<thead>
						<th>Partido</th>
						<th>Local</th>
						<th>Visitante</th>
						<th>Resultado</th>
						<th>Abandono</th>
						<th>Ganador</th>
					</thead>
					<tbody>
						<tr>
							<td>
								<label>Single 1</label>
							</td>
							<td>
								<g:select name="single1.jugadorLocal.id" from="${jugadoresLocales}" optionKey="id" 
										value="${fecha?.single1?.jugadorLocal?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:select name="single1.jugadorVisitante.id" from="${jugadoresVisitantes}" optionKey="id" 
										value="${fecha?.single1?.jugadorVisitante?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:field type="number" min='0' max='7' name="single1.primerSet.gamesGanador" value="${fecha?.single1?.primerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single1.primerSet.gamesPerdedor" value="${fecha?.single1?.primerSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single1.segundoSet.gamesGanador" value="${fecha?.single1?.segundoSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single1.segundoSet.gamesPerdedor" value="${fecha?.single1?.segundoSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single1.tercerSet.gamesGanador" value="${fecha?.single1?.tercerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single1.tercerSet.gamesPerdedor" value="${fecha?.single1?.tercerSet?.gamesPerdedor}" class="sett" style='width:35%'/>
							</td>
							<td>
								<g:checkBox name="abandonoSingle1" value="${fecha?.single1?.abandono }"/>
							</td>
							<td>
								<g:select name="single1.equipoGanador.id" from="${equipos}" optionKey="id" 
										value="${fecha?.single1?.equipoGanador?.id}" class="many-to-one" noSelection="['null': '']"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Single 2</label>
							</td>
							<td>
								<g:select name="single2.jugadorLocal.id" from="${jugadoresLocales}" optionKey="id" 
										value="${fecha?.single2?.jugadorLocal?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:select name="single2.jugadorVisitante.id" from="${jugadoresVisitantes}" optionKey="id" 
										value="${fecha?.single2?.jugadorVisitante?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:field type="number" min='0' max='7' name="single2.primerSet.gamesGanador" value="${fecha?.single2?.primerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single2.primerSet.gamesPerdedor" value="${fecha?.single2?.primerSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single2.segundoSet.gamesGanador" value="${fecha?.single2?.segundoSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single2.segundoSet.gamesPerdedor" value="${fecha?.single2?.segundoSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single2.tercerSet.gamesGanador" value="${fecha?.single2?.tercerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="single2.tercerSet.gamesPerdedor" value="${fecha?.single2?.primerSet?.gamesGanador}" class="sett" style='width:35%'/>
							</td>
							<td>
								<g:checkBox name="abandonoSingle2" value="${fecha?.single2?.abandono }"/>
							</td>
							<td>
								<g:select name="single2.equipoGanador.id" from="${equipos}" optionKey="id" 
										value="${fecha?.single2?.equipoGanador?.id}" class="many-to-one" noSelection="['null': '']"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Doble</label>
							</td>
							<td>
								<g:select name="doble.parejaLocal.doblista1.id" from="${jugadoresLocales}" optionKey="id" 
										value="${fecha?.doble?.parejaLocal?.doblista1?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:select name="doble.parejaVisitante.doblista1.id" from="${jugadoresVisitantes}" optionKey="id" 
										value="${fecha?.doble?.parejaVisitante?.doblista1?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:field type="number" min='0' max='7' name="doble.primerSet.gamesGanador" value="${fecha?.doble?.primerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="doble.primerSet.gamesPerdedor" value="${fecha?.doble?.primerSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="doble.segundoSet.gamesGanador" value="${fecha?.doble?.segundoSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="doble.segundoSet.gamesPerdedor" value="${fecha?.doble?.segundoSet?.gamesPerdedor}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="doble.tercerSet.gamesGanador" value="${fecha?.doble?.tercerSet?.gamesGanador}" class="sett" style='width:35%'/>
								<g:field type="number" min='0' max='7' name="doble.tercerSet.gamesPerdedor" value="${fecha?.doble?.tercerSet?.gamesPerdedor}" class="sett" style='width:35%'/>
							</td>
							<td>
								<g:checkBox name="abandonoDoble" value="${fecha?.doble?.abandono }"/>
							</td>
							<td>
								<g:select name="doble.equipoGanador.id" from="${equipos}" optionKey="id" 
										value="${fecha?.doble?.equipoGanador?.id}" class="many-to-one" noSelection="['null': '']"/>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<g:select name="doble.parejaLocal.doblista2.id" from="${jugadoresLocales}" optionKey="id" 
										value="${fecha?.doble?.parejaLocal?.doblista1?.id}" noSelection="['null': '']"/>
							</td>
							<td>
								<g:select name="doble.parejaVisitante.doblista2.id" from="${jugadoresVisitantes}" optionKey="id" 
										value="${fecha?.doble?.parejaVisitante?.doblista1?.id}" noSelection="['null': '']"/>
							</td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>