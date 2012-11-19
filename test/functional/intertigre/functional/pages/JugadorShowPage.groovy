package intertigre.functional.pages

import geb.Page;

class JugadorShowPage  extends Page{
	static url = 'jugador/show/'
	static at = { waitFor { title == 'Show Jugador' } }
	
	static content = { 
		dniField { $('span', "aria-labelledby": 'dni-label') }
	}
}
