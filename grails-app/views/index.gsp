<%@ page import="intertigre.domain.Notificacion" %>
<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Intertigres</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}
            
			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
	<body>
			<h1 align="center">Intertigres</h1>

			<h2 style="margin-left: 2%">Notificaciones:</h2>
			
				<g:each in="${Notificacion.findAll(sort: 'fecha') { fecha >= new DateTime().minusDays(1).toDate() }}" var="notificacion">
					<table>
						<thead>
							<tr>
								<th>
									${ notificacion.titulo }
								</th>
								<th><p align="right"><g:formatDate date="${ notificacion.fecha }" format="dd-MMM-yy"/></p></th>
							</tr>
						</thead>
						<tbody>
							<tr><td>${ notificacion.mensaje }</td></tr>
						</tbody>	
					</table>
				</g:each>
	</body>
</html>
