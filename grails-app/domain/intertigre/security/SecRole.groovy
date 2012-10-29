package intertigre.security

class SecRole {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
	
	def boolean equals(secRole){
		if (this.is(secRole)) return true
		
		if (!secRole || getClass() != secRole.class) return false
				
		return this.authority == secRole.authority
	}
	
	def String toString(){
		return authority
	}
}
