package pl.mskruch.ping.home

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import pl.mskruch.ping.service.Auth
import pl.mskruch.ping.service.Checks
import pl.mskruch.ping.service.Users

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class HomeController
{
	Users users
	Checks checks
	Auth auth

	HomeController(Users users, Checks checks, Auth auth)
	{
		this.users = users
		this.checks = checks
		this.auth = auth
	}

	@RequestMapping(value = "/", method = GET)
	show(Model model)
	{
		if (users.isEnabled()) {
			model.addAttribute("checks", checks.list())
		}

		model.addAttribute("logoutUrl", users.logoutURL())
		model.addAttribute("admin", auth.isAdmin())

		'main'
	}
}
