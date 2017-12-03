package pl.mskruch.ping.outage

import com.fasterxml.jackson.annotation.JsonIgnore
import com.googlecode.objectify.Ref
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent
import groovy.transform.ToString
import pl.mskruch.ping.check.Check

@Entity
@ToString
class Outage
{
	@Id
	Long id

	@JsonIgnore
	@Parent
	Ref<Check> checkRef

	@Index
	Date started
	@Index
	Date finished

	Date notified

	Outage()
	{
	}

	Outage(Ref<Check> checkRef, Date started)
	{
		this.checkRef = checkRef
		this.started = started
	}
}
