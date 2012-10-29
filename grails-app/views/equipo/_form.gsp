<%@ page import="intertigre.domain.Equipo" %>

<div class="fieldcontain ${hasErrors(bean: equipoInstance, field: 'categoria', 'error')} required">
	<label for="categoria">
		<g:message code="equipo.categoria.label" default="Categoria" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="categoria" name="categoria.id" from="${intertigre.domain.Categoria.list()}" optionKey="id" required="" value="${equipoInstance?.categoria?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipoInstance, field: 'jerarquia', 'error')} ">
	<label for="jerarquia">
		<g:message code="equipo.jerarquia.label" default="Jerarquia" />
		
	</label>
	<g:textField name="jerarquia" maxlength="1" pattern="${equipoInstance.constraints.jerarquia.matches}" value="${equipoInstance?.jerarquia}"/>
</div>


