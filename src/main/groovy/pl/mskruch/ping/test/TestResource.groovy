package pl.mskruch.ping.test

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.exception.NotFound

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@RequestMapping("/test")
class TestResource
{
	boolean value

	@RequestMapping(value = "/change", method = GET)
	@ResponseBody
	change()
	{
		value = !value
		return value
	}

	@RequestMapping(value = "/get", method = GET)
	@ResponseBody
	get()
	{
		if (!value) {
			throw new NotFound('test',0L)
		}
		return 'totoro'
	}
}
