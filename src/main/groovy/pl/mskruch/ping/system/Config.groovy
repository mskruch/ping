package pl.mskruch.ping.system

import com.googlecode.objectify.Key
import groovy.util.logging.Log

import static com.googlecode.objectify.ObjectifyService.ofy
import static pl.mskruch.common.FormatUtils.toIntegerOrNull

@Log
class Config
{
	List<ConfigEntry> list()
	{
		return ofy().load().type(ConfigEntry.class).list();
	}

	Long update(String key, String value)
	{
		ConfigEntry entry = find(key)
		if (entry && entry.value == value) {
			log.info "property $key is already set to $value"
			return entry.id
		}

		if (entry) {
			entry.value = value
			entry.auto = false
		} else {
			entry = new ConfigEntry(key, value, false)
		}
		save(entry)

		new AdminMail().notify("$key property updated", "$key: $value")

		return entry.id
	}

	private Key<ConfigEntry> save(ConfigEntry entity)
	{
		ofy().save().entity(entity).now()
	}

	String get(String key)
	{
		def value = getOrRegister(key)
		if (!value) {
			throw new IllegalStateException("$key property not configured")
		}
		return value
	}

	private ConfigEntry find(String key)
	{
		ofy().load().type(ConfigEntry.class).filter("key", key).first().now()
	}

	int getAsInt(String key, int defaultValue)
	{
		def value = getOrRegister(key, defaultValue.toString())
		def asInteger = value ? toIntegerOrNull(value) : null
		return asInteger ?: defaultValue
	}

	private String getOrRegister(String key, String defaultValue = null)
	{
		ConfigEntry entry = find(key)
		def notAutoEntryPresent = entry && !entry.auto
		if (notAutoEntryPresent) {
			return entry.value
		}

		log.fine "config entry fetched from datastore: $entry"

		if (!entry) {
			entry = new ConfigEntry(key, defaultValue, true)
			save(entry)
		} else if (entry.value != defaultValue) {
			entry.value = defaultValue
			save(entry)
		}
		return null
	}

	def delete(id)
	{
		ConfigEntry entry = ofy().load().type(ConfigEntry.class).id(id).now()
		if (entry) {
			ofy().delete().entity(entry)
			new AdminMail().notify("$key property deleted", "$entry.key: $entry.value")
			return entry
		}
	}
}