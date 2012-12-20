package intertigre.functional.pages

import geb.Page;

class FechasListPage extends Page{
	static url = 'fecha/list/'
	static at = { waitFor { title == "Fecha List" } }
	
	static content = {}
}