<%@ page import="intertigre.domain.Club" %>



<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'nombre', 'error')} ">
	<label for="nombre">
		<g:message code="club.nombre.label" default="Nombre" />
		
	</label>
	<g:textField name="nombre" value="${clubInstance?.nombre}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'direccion', 'error')} ">
	<label for="direccion">
		<g:message code="club.direccion.label" default="Direccion" />
		
	</label>
	<g:textField name="direccion" value="${clubInstance?.direccion}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'telefono', 'error')} ">
	<label for="telefono">
		<g:message code="club.telefono.label" default="Telefono" />
		
	</label>
	<g:textField name="telefono" value="${clubInstance?.telefono}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'email', 'error')} ">
	<label for="email">
		<g:message code="club.email.label" default="Email" />
		
	</label>
	<g:textField name="email" value="${clubInstance?.email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'localidad', 'error')} ">
	<label for="localidad">
		<g:message code="club.localidad.label" default="Localidad" />
		
	</label>
	<g:textField name="localidad" value="${clubInstance?.localidad}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'triosDeCanchasDisponibles', 'error')} ">
	<label for="triosDeCanchasDisponibles">
		<g:message code="club.triosDeCanchasDisponibles.label" default="Trios De Canchas Disponibles" />
		
	</label>
	<g:field type="number" name="triosDeCanchasDisponibles" value="${fieldValue(bean: clubInstance, field: 'triosDeCanchasDisponibles')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: clubInstance, field: 'horariosPreferidosParaLocal', 'error')} ">
	<label for="horariosPreferidosParaLocal">
		Horarios disponibles de canchas
		
	</label>
	<g:field type="text" id="horariosPreferidosParaLocal" name="horariosPreferidosParaLocal"/>
</div>

