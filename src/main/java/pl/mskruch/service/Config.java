package pl.mskruch.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import pl.mskruch.data.ConfigEntry;

public class Config
{
	public List<ConfigEntry> all()
	{
		return ofy().load().type(ConfigEntry.class).list();
	}

	public Long update(String key, String value)
	{
		ConfigEntry fetched = ofy().load().type(ConfigEntry.class)
			.filter("key", key).first().now();
		if (fetched != null) {
			ofy().delete().type(ConfigEntry.class).id(fetched.getId());
		}

		ConfigEntry entity = new ConfigEntry(key, value);
		ofy().save().entity(entity).now();

		new AdminMail().notify("Ping config changed: " + key,
			key + ": " + value);
		return entity.getId();
	}

	public String get(String key)
	{
		ConfigEntry fetched = ofy().load().type(ConfigEntry.class)
			.filter("key", key).first().now();
		return fetched != null ? fetched.getValue() : null;
	}
}
