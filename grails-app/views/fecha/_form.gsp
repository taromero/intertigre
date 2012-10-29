<%@ page import="intertigre.domain.Fecha" %>
<%@ page import="intertigre.domain.Jugador" %>


<sec:ifAllGranted roles="ROLE_ADMIN">
	<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'aprobadoPorAdmin', 'error')} ">
		<label for="aprobadoPorAdmin">
			<g:message code="fecha.aprobadoPorAdmin.label" default="Aprobado Por Admin" />
			
		</label>
		<g:checkBox name="aprobadoPorAdmin" value="${fechaInstance?.aprobadoPorAdmin}" />
	</div>
</sec:ifAllGranted>
<sec:ifAllGranted roles="ROLE_CAPITAN_EQUIPO">
		<g:if test="${Jugador.get(sec.loggedInUserInfo(field:'id').toLong()).equipos.contains(fechaInstance?.equipoPerdedor)}">
			<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'aprobadoPorRival', 'error')} ">
				<label for="aprobadoPorRival">
					<g:message code="fecha.aprobadoPorRival.label" default="Aprobado Por Rival" />
					
				</label>
				<g:checkBox name="aprobadoPorRival" value="${fechaInstance?.aprobadoPorRival}" />
			</div>
		</g:if>
</sec:ifAllGranted>

<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'categoria', 'error')} ">
	<label for="categoria">
		<g:message code="fecha.categoria.label" default="Categoria" />
		
	</label>
	<g:select id="categoria" name="categoria.id" from="${intertigre.domain.Categoria.list()}" optionKey="id" value="${fechaInstance?.categoria?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'equipoLocal', 'error')} ">
	<label for="equipoLocal">
		<g:message code="fecha.equipoLocal.label" default="Equipo Local" />
		
	</label>
	<g:select id="equipoLocal" name="equipoLocal.id" from="${intertigre.domain.Equipo.list()}" optionKey="id" value="${fechaInstance?.equipoLocal?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'equipoVisitante', 'error')} ">
	<label for="equipoVisitante">
		<g:message code="fecha.equipoVisitante.label" default="Equipo Visitante" />
		
	</label>
	<g:select id="equipoVisitante" name="equipoVisitante.id" from="${intertigre.domain.Equipo.list()}" optionKey="id" value="${fechaInstance?.equipoVisitante?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'fechaDeJuego', 'error')} ">
	<label for="fechaDeJuego">
		<g:message code="fecha.fechaDeJuego.label" default="Fecha De Juego" />
		
	</label>
	<g:datePicker name="fechaDeJuego" precision="day"  value="${fechaInstance?.fechaDeJuego}" default="none" noSelection="['': '']" default="${new Date()}" relativeYears="[0..0]"/>
</div>

<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'wo', 'error')} ">
	<label for="wo">
		<g:message code="fechaInstance.wo.label" default="Wo" />
		
	</label>
	<g:checkBox name="wo" value="${fechaInstance?.wo}" />
</div>

<g:if test="${fechaInstance.id != null}">
	<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'single1', 'error')} ">
		<label for="single1">
			<g:message code="fecha.single1.label" default="Single1" />
			
		</label>
		<g:if test="${fechaInstance?.single1 != null}">
		    <g:link controller="single" action="show" id="${fechaInstance.single1.id}">${fechaInstance.single1.encodeAsHTML()}</g:link>
		</g:if>
		<g:else>
			<g:link controller="single" action="create" params="['fechaId': fechaInstance?.id, 'singleNro': 1]">${message(code: 'default.add.label', args: [message(code: 'single.label', default: 'Single')])}</g:link>
		</g:else>
	</div>
</g:if>

<g:if test="${fechaInstance.id != null}">
	<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'single2', 'error')} ">
		<label for="single2">
			<g:message code="fecha.single2.label" default="Single2" />
			
		</label>
		<g:if test="${fechaInstance?.single2 != null}">
		    <g:link controller="single" action="show" id="${fechaInstance.single2.id}">${fechaInstance.single2.encodeAsHTML()}</g:link>
		</g:if>
		<g:else>
			<g:link controller="single" action="create" params="['fechaId': fechaInstance?.id, 'singleNro': 2]">${message(code: 'default.add.label', args: [message(code: 'single.label', default: 'Single')])}</g:link>
		</g:else>
	</div>
</g:if>

<g:if test="${fechaInstance.id != null}">
	<div class="fieldcontain ${hasErrors(bean: fechaInstance, field: 'doble', 'error')} ">
		<label for="doble">
			<g:message code="fecha.doble.label" default="Doble" />
			
		</label>
		<g:if test="${fechaInstance?.single2 != null}">
		    <g:link controller="doble" action="show" id="${fechaInstance.doble.id}">${fechaInstance.doble.encodeAsHTML()}</g:link>
		</g:if>
		<g:else>
			<g:link controller="doble" action="create" params="['fechaId': fechaInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'doble.label', default: 'Doble')])}</g:link>
		</g:else>
	</div>
</g:if>