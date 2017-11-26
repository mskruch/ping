package pl.mskruch.ping.test

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.exception.BadRequest

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.PUT

@Controller
@RequestMapping("/test")
class TestResource
{
	def up

	@RequestMapping(method = PUT)
	@ResponseBody
	update(@RequestBody request)
	{
		up = request.up
		return ['up': up]
	}

	@RequestMapping(method = GET)
	@ResponseBody
	get()
	{
		if (!up) {
			throw new BadRequest('test is down')
		}
		return ['up': up]
	}
}
