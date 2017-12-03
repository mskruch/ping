package pl.mskruch.exception

import groovy.transform.InheritConstructors

@InheritConstructors
class InvalidURL extends RuntimeException
{
	InvalidURL(String url)
	{
		super("invalid url: $url")
	}
}
