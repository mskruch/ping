package pl.mskruch.ping.check

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import groovy.transform.ToString

import static pl.mskruch.common.FormatUtils.formatDurationSince

@Entity
@ToString
class Check
{
	@Id
	Long id;
	@Index
	String ownerEmail;
	@Index
	String name;

	Date created;
	String url;

	Date lastCheck;
	Status status
	Date statusSince;

	def notificationDelay

	Check()
	{
	}

	public Check(String ownerEmail, String url)
	{
		this.ownerEmail = ownerEmail;
		this.url = url;
		this.created = new Date();
	}
	// Boolean paused;

	public Long getId()
	{
		return id;
	}

	public String getUrl()
	{
		return url;
	}

	public boolean setStatus(Status status)
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

	public Date getLastCheck()
	{
		return lastCheck;
	}

	public Date getStatusSince()
	{
		return statusSince;
	}

	String getStatusSinceDuration()
	{
		formatDurationSince(this.statusSince)
	}

	String getLastCheckDuration()
	{
		formatDurationSince(this.lastCheck)
	}

	public String getOwnerEmail()
	{
		return ownerEmail;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
