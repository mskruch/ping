package pl.mskruch.ping.system

import pl.mskruch.ping.data.ConfigEntry

import static com.googlecode.objectify.ObjectifyService.ofy

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

	String get(String key)
	{
		def value = _get(key)
		if (!value) {
			throw new IllegalStateException("<$key not configured")
		}
		return value
	}

	private String _get(String key)
	{
		ConfigEntry fetched = ofy().load().type(ConfigEntry.class).filter("key", key).first().now()
		return fetched?.value
	}

	public int getInt(String key, int defaultValue)
	{
		String value = _get(key);
		if (value == null) {
			return defaultValue;
		}
		Integer number = toInteger(value);
		return number != null ? number.intValue() : defaultValue;
	}

	private Integer toInteger(String value)
	{
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
