package pl.mskruch.ping.check

import com.googlecode.objectify.Ref
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent

@Entity
class Outage
{
	@Id
	Long id;

	@Parent
	Ref<Check> checkRef;
//	Key<Check> checkKey;

	@Index
	Date started;
	Date finished;

	Date notified;
}
