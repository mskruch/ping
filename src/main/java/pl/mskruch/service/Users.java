package pl.mskruch.service;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class Users
{
	private HttpServletRequest req;
	private UserService userService = UserServiceFactory.getUserService();

	public Users(HttpServletRequest req)
	{
		this.req = req;
	}

	public boolean isLoggedIn()
	{
		return req.getUserPrincipal() != null;
	}

	public String loginURL()
	{
		String thisUrl = req.getRequestURI();
		return userService.createLoginURL(thisUrl);
	}

	public String logoutURL()
	{
		String thisUrl = req.getRequestURI();
		return userService.createLogoutURL(thisUrl);
	}
}
