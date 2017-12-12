package pl.mskruch.ping.system

import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.check.ChecksConfig
import pl.mskruch.ping.security.Auth
import pl.mskruch.ping.user.Users

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@Log
@RequestMapping(["/api/state", "api/session"])
@TupleConstructor
class SessionResource
{
	Users users
	Auth auth
	ChecksConfig checksConfig

	@RequestMapping(method = GET)
	@ResponseBody
	session()
	{
		['enabled'    : users.isEnabled(),
		 'admin'      : auth.isAdmin(),
		 'checksLimit': checksConfig.getLimit(),
		 'logoutUrl'  : auth.logoutURL('/')]
	}

}
