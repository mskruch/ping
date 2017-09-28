package pl.mskruch.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppleController
{

	String message;

	@RequestMapping(value = "/apples/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String apple(@PathVariable String name, ModelMap model)
	{
		return message + " " + name;

	}

	@RequestMapping(value = "/map", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> map()
	{
		Map<String, String> map = new HashMap<>();
		map.put("x", "y");
		return map;

	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}