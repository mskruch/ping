package pl.mskruch.ping.service

import pl.mskruch.exception.Unauthorized

import javax.servlet.http.HttpServletRequest

class Auth
{
	final private request

	Auth(HttpServletRequest request)
	{
		this.request = request;
	}

	String email(){
		def email = request?.getUserPrincipal()?.getName();
		if (email == null){
			throw new Unauthorized()
		}
		email
	}

	def isAdmin() {
		request.isUserInRole("admin")
	}
}
