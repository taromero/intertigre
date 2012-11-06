package intertigre.security

import grails.plugins.springsecurity.SpringSecurityService;

class SecUser {

	transient springSecurityService, authority

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
		username email:true, blank: false, unique: true, nullable: false
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<SecRole> getAuthorities() {
//		SecUserSecRole.findAllBySecUser(this).collect { it.secRole } as Set
		//Lo hago de esta forma porque no puedo lograr testear la forma de arriba
		SecUserSecRole.findAll().find { it.secUser == this }*.secRole as Set
	}

	SecRole getAuthority(){
		return getAuthorities().toArray()[0]
	}
	
	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		if(springSecurityService){
			password = springSecurityService.encodePassword(password)
		}
	}
}
