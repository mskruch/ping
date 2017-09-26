package pl.mskruch.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ConfigEntry
{
	@Id
	Long id;
	@Index
	String key;
	String value;

	public ConfigEntry(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	ConfigEntry()
	{
	}

	public Long getId()
	{
		return id;
	}

	public String getValue()
	{
		return value;
	}

	public String getKey()
	{
		return key;
	}
}
