<%@ page import="intertigre.domain.Jugador" %>



<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'username', 'error')} ">
	<label for="username">
		<g:message code="jugador.username.label" default="Usuario (usar tu email)" />
		
	</label>
	<g:field type="email" name="username" id="username"value="${jugadorInstance?.username}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'password', 'error')} ">
	<label for="password">
		<g:message code="jugador.password.label" default="Password" />
		
	</label>
	<g:field type="password" name="password" name="password" id="password"/>
</div>

<div class="fieldcontain">
	<label for="password">
		Confirmar password
		
	</label>
	<g:field type="password" name="passwordConfirm" id="passwordConfirm"/>
</div>

<sec:ifAllGranted roles="ROLE_ADMIN">
	<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'role', 'error')} ">
		<label for="role">
			<g:message code="jugador.role.label" default="Role" />
			
		</label>
		<g:select name="role" from="${jugadorInstance.constraints.role.inList}" value="${jugadorInstance?.role}" valueMessagePrefix="jugador.role" 
		noSelection="['': 'Elegi el rol']"/>
	</div>
</sec:ifAllGranted>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'dni', 'error')} ">
	<label for="dni">
		<g:message code="jugador.dni.label" default="Dni" />
		
	</label>
	<g:textField name="dni" pattern="${jugadorInstance.constraints.dni.matches}" value="${jugadorInstance?.dni}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'nombre', 'error')} ">
	<label for="nombre">
		<g:message code="jugador.nombre.label" default="Nombre" />
		
	</label>
	<g:textField name="nombre" pattern="${jugadorInstance.constraints.nombre.matches}" value="${jugadorInstance?.nombre}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'apellido', 'error')} ">
	<label for="apellido">
		<g:message code="jugador.apellido.label" default="Apellido" />
		
	</label>
	<g:textField name="apellido" pattern="${jugadorInstance.constraints.apellido.matches}" value="${jugadorInstance?.apellido}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'sexo', 'error')} ">
	<label for="sexo">
		<g:message code="jugador.sexo.label" default="Sexo" />
		
	</label>
	<g:select name="sexo" from="${jugadorInstance.constraints.sexo.inList}" value="${jugadorInstance?.sexo}" valueMessagePrefix="jugador.sexo" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'nacimiento', 'error')} ">
	<label for="nacimiento">
		<g:message code="jugador.nacimiento.label" default="Nacimiento" />
		
	</label>
	<g:datePicker name="nacimiento" precision="day"  value="${jugadorInstance?.nacimiento}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'telefono', 'error')} ">
	<label for="telefono">
		<g:message code="jugador.telefono.label" default="Telefono" />
		
	</label>
	<g:field type="text" name="telefono" value="${fieldValue(bean: jugadorInstance, field: 'telefono')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: jugadorInstance, field: 'urlImagen', 'error')} ">
	<label for="urlImagen">
		URL Imagen
		
	</label>
	<g:field type="text" name="urlImagen" value="${fieldValue(bean: jugadorInstance, field: 'urlImagen')}"/>
	<p>Busque en alguna pagina su imagen, y consiga su URL (gralmente click der. -> URL imagen)</p>
</div>

