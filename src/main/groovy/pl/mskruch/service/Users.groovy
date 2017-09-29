package pl.mskruch.service;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import pl.mskruch.data.User;
import pl.mskruch.exception.NotFound;

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
		User fetched = find(name);

		if (fetched == null) {
			User user = new User(name);
			ofy().save().entity(user).now(); // async without the now()
			return req.isUserInRole("admin");
		}

		return req.isUserInRole("admin") || fetched.isEnabled();
	}

	public User find(String email) {
		return ofy().load().type(User.class).filter("email", email)
			.first().now();
	}

	public List<User> all()
	{
		return ofy().load().type(User.class).list();
	}

    public void switchEnabled(String id) {
	    if (id == null){
	        return;
        }
        User user = get(id);
	    if (user.isEnabled()){
	        user.disable();
        } else {
	        user.enable();
        }
        ofy().save().entity(user).now();
    }

	public User get(String id) {
		return get(Long.valueOf(id));
	}

	public User get(Long id) {
		User user = ofy().load().type(User.class).id(id).now();
		if (user == null){
			throw new NotFound('user', id);
		}
		return user;
	}

	public void delete(Long id) {
		User user = get(id);
		ofy().delete().entity(user);
	}
}
