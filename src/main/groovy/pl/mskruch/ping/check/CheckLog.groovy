package pl.mskruch.ping.check

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent

@Entity
class CheckLog
{
	@Id
	Long id

	@Parent
	Key<Check> checkKey

	Status status
	Long duration

	@Index
	Date created

	CheckLog(Key<Check> checkKey)
	{
		this.checkKey = checkKey
	}
}