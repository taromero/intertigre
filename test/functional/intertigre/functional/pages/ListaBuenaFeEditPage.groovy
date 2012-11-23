package intertigre.functional.pages

import geb.Page;

class ListaBuenaFeEditPage extends Page{
	static url = 'equipo/editListaBuenaFe/'
	static at = { waitFor { title == "Lista de Buena Fe" } }
	
	static content = { 
		jugadoresDelClub { $('#jugadoresClub') }
		jugadoresDelEquipo { $('#jugadoresEquipo') }
		submitButton { $('.save') }
	}
}
