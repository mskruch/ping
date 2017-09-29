package pl.mskruch.ping.data

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Parent
import pl.mskruch.data.Check
import pl.mskruch.data.Status

@Entity
class CheckStatus
{
	@Id
	Long id;

	@Parent
	Check check;

	Status status;

	@Index
	Date since;
	Date updated;
	Boolean notified;
}
