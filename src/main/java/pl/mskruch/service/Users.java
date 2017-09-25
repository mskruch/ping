package pl.mskruch.service;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import pl.mskruch.data.Check;
import pl.mskruch.data.User;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

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

	public boolean isEnabled()
	{
		if (req.getUserPrincipal() == null || req.getUserPrincipal().getName() == null) {
			return false;
		}
		String name = req.getUserPrincipal().getName();
		User fetched = ofy().load().type(User.class).filter("email", name)
			.first().now();

		if (fetched == null) {
			User user = new User(name);
			ofy().save().entity(user).now(); // async without the now()
			return req.isUserInRole("admin");
		}

		return req.isUserInRole("admin") || fetched.isEnabled();
	}

	public List<User> all()
	{
		return ofy().load().type(User.class).list();
	}

    public void switchEnabled(String id) {
	    if (id == null){
	        return;
        }
        User user = ofy().load().type(User.class).id(Long.valueOf(id)).now();
	    if (user.isEnabled()){
	        user.disable();
        } else {
	        user.enable();
        }
        ofy().save().entity(user).now();
    }
}
