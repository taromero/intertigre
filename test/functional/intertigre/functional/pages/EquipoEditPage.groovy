package intertigre.functional.pages

import geb.Page

class EquipoEditPage extends Page{
	static url = 'equipo/edit/'
	static at = { waitFor { title == "Show Equipo" } }
	
	static content = {
		//Si el selector de jquery encuentra mas de un elemento para la coleccion, aparentemente los
		// va encontrando de arriba hacia abajo, por lo que esta lista va a tener ese orden
		itemsListaBuenaFeField { $('.itemListaBuenaFe') }
	}
	
}
