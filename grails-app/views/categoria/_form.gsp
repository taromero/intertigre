<%@ page import="intertigre.domain.Categoria" %>



<div class="fieldcontain ${hasErrors(bean: categoriaInstance, field: 'edadLimiteInferior', 'error')} ">
	<label for="edadLimiteInferior">
		<g:message code="categoria.edadLimiteInferior.label" default="Edad Limite Inferior" />
		
	</label>
	<g:field type="number" name="edadLimiteInferior" value="${fieldValue(bean: categoriaInstance, field: 'edadLimiteInferior')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: categoriaInstance, field: 'edadLimiteSuperior', 'error')} ">
	<label for="edadLimiteSuperior">
		<g:message code="categoria.edadLimiteSuperior.label" default="Edad Limite Superior" />
		
	</label>
	<g:field type="number" name="edadLimiteSuperior" value="${fieldValue(bean: categoriaInstance, field: 'edadLimiteSuperior')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: categoriaInstance, field: 'nombre', 'error')} ">
	<label for="nombre">
		<g:message code="categoria.nombre.label" default="Nombre" />
		
	</label>
	<g:textField name="nombre" value="${categoriaInstance?.nombre}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: categoriaInstance, field: 'sexo', 'error')} ">
	<label for="sexo">
		<g:message code="categoria.sexo.label" default="Sexo" />
		
	</label>
	<g:textField name="sexo" value="${categoriaInstance?.sexo}"/>
</div>

