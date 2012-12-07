package intertigre.functional.pages

import geb.Page;

class LogoutPage extends Page{
	static url = 'logout/index'
	static at = { waitFor { title == "Intertigres" } }
	
}
