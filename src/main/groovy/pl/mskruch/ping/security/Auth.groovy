package pl.mskruch.ping.security

import com.google.appengine.api.users.UserService
import com.google.appengine.api.users.UserServiceFactory
import pl.mskruch.exception.Unauthorized

import javax.servlet.http.HttpServletRequest

class Auth
{
	final private request
	final private UserService userService = UserServiceFactory.getUserService();

	Auth(HttpServletRequest request)
	{
		this.request = request
	}

	String email()
	{
		def email = request?.getUserPrincipal()?.getName();
		if (email == null) {
			throw new Unauthorized()
		}
		email
	}

	def isAdmin()
	{
		request.isUserInRole("admin")
	}

	def logoutURL(String backUrl)
	{
		userService.createLogoutURL(backUrl)
	}
}
