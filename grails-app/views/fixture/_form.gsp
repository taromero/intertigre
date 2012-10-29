<%@ page import="intertigre.domain.Fixture" %>



<div class="fieldcontain ${hasErrors(bean: fixtureInstance, field: 'categoria', 'error')} ">
	<label for="categoria">
		<g:message code="fixture.categoria.label" default="Categoria" />
		
	</label>
	<g:select id="categoria" name="categoria.id" from="${intertigre.domain.Categoria.list()}" optionKey="id" value="${fixtureInstance?.categoria?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

