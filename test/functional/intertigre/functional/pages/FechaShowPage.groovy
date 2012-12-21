package intertigre.functional.pages

import geb.Page

class FechaShowPage extends Page{
	static url = 'fecha/show/'
	static at = { waitFor { title == "Show Fecha" } }
	
	static content = {
		reprogramarButton { $('.reprogramar') }
		fechaReprogramacionField { $('#fechaReprogramacion') }
	}
}
