package pl.mskruch.ping.test

import com.google.appengine.api.memcache.MemcacheServiceFactory
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
	private cache = MemcacheServiceFactory.getMemcacheService()

	@RequestMapping(method = PUT)
	@ResponseBody
	update(@RequestBody request)
	{
		this.cache.put('up', request.up)
		return ['up': this.cache.get('up')]
	}

	@RequestMapping(method = GET)
	@ResponseBody
	get()
	{
		def up = cache.get('up')
		if (!up) {
			throw new BadRequest('test is down')
		}
		return ['up': up]
	}
}
