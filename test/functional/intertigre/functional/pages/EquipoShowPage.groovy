package intertigre.functional.pages

import geb.Page

class EquipoShowPage extends Page{
	static url = 'equipo/edit/'
	static at = { waitFor { title == "Show Equipo" } }
}
