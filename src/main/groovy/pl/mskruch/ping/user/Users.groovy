package pl.mskruch.ping.user

import pl.mskruch.exception.NotFound

import javax.servlet.http.HttpServletRequest

import static com.googlecode.objectify.ObjectifyService.ofy

class Users
{
	private HttpServletRequest req

	/** for servlet */
	@Deprecated
	Users(HttpServletRequest req)
	{
		this.req = req
	}

	boolean isEnabled()
	{
		if (req.getUserPrincipal() == null || req.getUserPrincipal().getName() == null) {
			return false;
		}
		String name = req.getUserPrincipal().getName();
		User user = find(name);

		if (user == null) {
			user = new User(name)
			user.enabled = req.isUserInRole("admin")
			ofy().save().entity(user).now()
		}

		return user.isEnabled();
	}

	User find(String email)
	{
		return ofy().load().type(User.class).filter("email", email)
				.first().now();
	}

	List<User> all()
	{
		return ofy().load().type(User.class).list();
	}

	void switchEnabled(Long id)
	{
		User user = get(id)
		if (user.isEnabled()) {
			user.disable()
		} else {
			user.enable()
		}
		ofy().save().entity(user).now()
	}

	User get(Long id)
	{
		User user = ofy().load().type(User.class).id(id).now()
		if (user == null) {
			throw new NotFound('user', id);
		}
		return user;
	}

	void delete(Long id)
	{
		try {
			User user = get(id);
			ofy().delete().entity(user);
		} catch (NotFound e) {
			/* already deleted - fine */
		}
	}
}
