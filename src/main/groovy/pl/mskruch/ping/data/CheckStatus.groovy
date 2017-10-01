package pl.mskruch.ping.data

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent

@Entity
class CheckStatus
{
	@Id
	Long id;

	@Parent
	Key<Check> check;

	Status status;

	@Index
	Date since;
	Date checked;
	Date notified;
}
