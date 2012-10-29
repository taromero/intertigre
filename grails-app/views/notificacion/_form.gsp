<%@ page import="intertigre.domain.Notificacion" %>



<div class="fieldcontain ${hasErrors(bean: notificacionInstance, field: 'mensaje', 'error')} ">
	<label for="mensaje">
		<g:message code="notificacion.mensaje.label" default="Mensaje" />
		
	</label>
	<g:textArea name="mensaje" cols="40" rows="5" maxlength="500" value="${notificacionInstance?.mensaje}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: notificacionInstance, field: 'fecha', 'error')} ">
	<label for="fecha">
		<g:message code="notificacion.fecha.label" default="Fecha" />
		
	</label>
	<g:datePicker name="fecha" precision="day"  value="${notificacionInstance?.fecha}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: notificacionInstance, field: 'titulo', 'error')} ">
	<label for="titulo">
		<g:message code="notificacion.titulo.label" default="Titulo" />
		
	</label>
	<g:textField name="titulo" value="${notificacionInstance?.titulo}"/>
</div>

