<%@ page import="intertigre.domain.Grupo" %>



<div class="fieldcontain ${hasErrors(bean: grupoInstance, field: 'equipos', 'error')} ">
	<label for="equipos">
		<g:message code="grupo.equipos.label" default="Equipos" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${grupoInstance?.equipos?}" var="e">
    <li><g:link controller="equipo" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="equipo" action="create" params="['grupo.id': grupoInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'equipo.label', default: 'Equipo')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: grupoInstance, field: 'fechas', 'error')} ">
	<label for="fechas">
		<g:message code="grupo.fechas.label" default="Fechas" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${grupoInstance?.fechas?}" var="f">
    <li><g:link controller="fecha" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="fecha" action="create" params="['grupo.id': grupoInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'fecha.label', default: 'Fecha')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: grupoInstance, field: 'fixture', 'error')} ">
	<label for="fixture">
		<g:message code="grupo.fixture.label" default="Fixture" />
		
	</label>
	<g:select id="fixture" name="fixture.id" from="${intertigre.domain.Fixture.list()}" optionKey="id" value="${grupoInstance?.fixture?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: grupoInstance, field: 'nombre', 'error')} ">
	<label for="nombre">
		<g:message code="grupo.nombre.label" default="Nombre" />
		
	</label>
	<g:textField name="nombre" value="${grupoInstance?.nombre}"/>
</div>

