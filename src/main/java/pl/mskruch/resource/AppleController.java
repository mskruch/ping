package pl.mskruch.resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apples")
public class AppleController
{

	String message;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String getMovie(@PathVariable String name, ModelMap model)
	{
		return message + " " + name;

	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}