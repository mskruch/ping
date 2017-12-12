package pl.mskruch.ping.system;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index
import groovy.transform.Canonical
import groovy.transform.TupleConstructor;

@Entity
@Canonical
@TupleConstructor(excludes = "id")
class ConfigEntry
{
	@Id
	Long id

	@Index
	String key
	String value

	Boolean auto
}
