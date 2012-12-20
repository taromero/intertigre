package intertigre.functional.pages

import geb.Page;

class ReprogramarFechasPage extends Page{
	static url = 'fecha/fechasAReprogramar/'
	static at = { waitFor { title == "Reprogramar Fechas" } }
	
	static content = {
		fechasField { $('.fecha') }
		checkboxes { $('input[type=checkbox]') } //$('input:checkbox') anda en el navegador pero no con el driver
		marcarAlgunasComoResueltasButton { $('#marcarAlgunasComoResueltas') }
		marcarTodasComoResueltasButton { $('#marcarTodasComoResueltas') }
	}
}
