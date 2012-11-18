package intertigre.functional.pages

import geb.Page;

class HomePage extends Page{
	static url = ''
	static at = { waitFor { title == "Intertigres" } }
	
	static content = { }
}
