package pl.mskruch.ping.check

import com.fasterxml.jackson.annotation.JsonFormat
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import groovy.transform.ToString

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import static pl.mskruch.common.FormatUtils.TIME_FORMAT
import static pl.mskruch.common.FormatUtils.formatDurationSince

@Entity
@ToString
class Check implements Serializable
{
	@Id
	Long id;
	@Index
	String ownerEmail;
	@Index
	String name;

	@JsonFormat(shape = STRING, pattern = TIME_FORMAT, timezone = "GMT")
	Date created;
	String url;

	@JsonFormat(shape = STRING, pattern = TIME_FORMAT, timezone = "GMT")
	Date lastCheck;
	Status status

	@JsonFormat(shape = STRING, pattern = TIME_FORMAT, timezone = "GMT")
	Date statusSince

	Long getNotificationDelay()
	{
		return notificationDelay ?: 0
	}
	Long notificationDelay

	Check()
	{
	}

	Check(ownerEmail, url, name)
	{
		this.ownerEmail = ownerEmail
		this.name = name
		this.url = url
		this.created = new Date()
	}

	boolean setStatus(Status status)
	{
		Date now = new Date();
		this.lastCheck = now;
		if (this.status != status) {
			this.status = status;
			this.statusSince = now;
			return true;
		}
		return false;
	}

	@Deprecated
	String getStatusSinceDuration()
	{
		formatDurationSince(this.statusSince)
	}

	@Deprecated
	String getLastCheckDuration()
	{
		formatDurationSince(this.lastCheck)
	}
}
