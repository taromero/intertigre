package intertigre.functional.pages

import geb.Page;

class JugadorEditPage extends Page{
	static url = 'jugador/edit/'
	static at = { waitFor { title == 'Edit Jugador' } }
	
	static content = { }
}
