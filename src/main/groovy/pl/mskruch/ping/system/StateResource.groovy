package pl.mskruch.ping.system

import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.security.Auth
import pl.mskruch.ping.user.Users

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@Log
@RequestMapping("/api/state")
class StateResource
{
	Users users
	Auth auth

	StateResource(Users users, Auth auth)
	{
		this.users = users
		this.auth = auth
	}

	@RequestMapping(method = GET)
	@ResponseBody
	state()
	{
		['enabled'  : users.isEnabled(),
		 'admin'    : auth.isAdmin(),
		 'logoutUrl': users.logoutURL('/')]
	}

}
