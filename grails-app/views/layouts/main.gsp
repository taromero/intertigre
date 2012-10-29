<%@ page import="intertigre.domain.Jugador" %>

<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
		<r:require modules="bootstrap"/>
		<g:javascript library="jquery"/>
		<g:javascript library="jquery-ui"/>
		<g:layoutHead/>
        <r:layoutResources />
        <script type="text/javascript">
			function verCalendario(){
				$('#calendarioModal').dialog({
					height: 550,
					width: 850,
					modal: true,
					title: 'Calendario'
				});
			}
        </script>
	</head>
	<body style="margin-left:15.7%; margin-top:3.9%"> <%-- parche para bootstrap --%>
		<script>
			$(function(){
				$('.nav li').mouseover(function(){
					$('.active').not('.home').removeClass('active')
					$(this).addClass('active')
				})
				$('#navbar').mouseleave(function(){
					$('.active').not('.home').removeClass('active')
				})
			})
		</script>
		<div id="navbar" class="navbar navbar-fixed-top">
		  <div class="navbar-inner">
		    <div class="container">
		    <a class="brand" href="#">
			  InterTigres
			</a>
		      <ul class="nav">
				  <li class="active home">
				    <a class="btn-large" href="${createLink(uri: '/')}">Home</a>
				  </li>
				  <li class="dropdown">
					  <a data-toggle="dropdown" class="btn-large">Equipos<b class="caret"></b></a>
					  <ul class="dropdown-menu">
					    <li><g:link class="btn-large" controller="equipo" action="misEquipos">Mis Equipos</g:link></li>
					  	<li><g:link class="btn-large" controller="equipo" action="list">Buscar equipos</g:link></li>
					    <li><g:link class="btn-large" controller="equipo" action="listEquiposClub">Equipo Club</g:link></li>
					    <li class="divider"></li>
					  	<li class="nav-header">Administrar</li>
					  	<li><g:link class="btn-large" controller="equipo" action="create">Anotar nuevo equipo</g:link></li>
				      </ul>
				  </li>
				  <li class="dropdown">
					  <a data-toggle="dropdown" class="btn-large">Jugadores<b class="caret"></b></a>
					  <ul class="dropdown-menu">
					  	<li><g:link class="btn-large" controller="jugador" action="show">Mis datos</g:link></li>
					  	<li><g:link class="btn-large" controller="jugador" action="list">Buscar jugador</g:link></li>
					  	<li class="divider"></li>
					  	<li class="nav-header">Administrar</li>
					  	<li><g:link class="btn-large" controller="jugador" action="create">Inscribir jugador</g:link></li>
				  	  </ul>
				  </li>
				  <li class="dropdown">
					  <a data-toggle="dropdown" class="btn-large">Fechas<b class="caret"></b></a>
					  <ul class="dropdown-menu">
					  	<li><g:link class="btn-large" controller="fecha" action="list">Mis Fechas</g:link></li>
					  	<li><g:link class="btn-large" controller="fecha" action="list">Buscar Fechas</g:link></li>
				  	  </ul>
				  </li>
				  <sec:ifAllGranted roles="ROLE_ADMIN">
					  <li class="dropdown">
						  <a data-toggle="dropdown" class="btn-large">Administracion<b class="caret"></b></a>
						  <ul class="dropdown-menu">
						  	<li><g:link class="btn-large" controller="notificacion" action="create">Crear notificacion</g:link></li>
						  	<li><g:link class="btn-large" controller="fixture" action="create">Crear fixture</g:link></li>
					  	  </ul>
					  </li>
				  </sec:ifAllGranted>
				  <li><a onclick="verCalendario()" href="#" class="btn-large">Calendario</a></li>
				  <li><g:link class="btn-large" controller="login" action="index">Login</g:link></li>
				  <li><g:link class="btn-large" controller="logout" action="index">Logout</g:link></li>
				</ul>
				<sec:ifAllGranted roles="ROLE_JUGADOR">
					${Jugador.get(sec.loggedInUserInfo(field:'id').toLong())?.nombreCompleto + ' - ' +
					  Jugador.get(sec.loggedInUserInfo(field:'id').toLong())?.role}
				</sec:ifAllGranted>
		    </div>
		  </div>
		</div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>
        <r:layoutResources />
        <div id="calendarioModal" style="display:none">
        	<iframe src="https://www.google.com/calendar/embed?src=qoa5l1qn8se960bdqupajipdi8%40group.calendar.google.com&ctz=America/Argentina/Buenos_Aires" 
        				style="border: 0" width="800" height="500" frameborder="0" scrolling="no"></iframe>
        </div>
	</body>
</html>