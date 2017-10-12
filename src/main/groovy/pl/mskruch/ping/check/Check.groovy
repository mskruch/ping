package pl.mskruch.ping.check;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import groovy.transform.ToString;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

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
	Status status;
	Date statusSince;

	Long notificationDelayInMilliseconds;

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

	public String getStatusSinceDuration()
	{
		return formatDurationSince(this.statusSince);

	}

	public String getLastCheckDuration()
	{
		return formatDurationSince(this.lastCheck);
	}

	private String formatDurationSince(Date time)
	{
		if (time == null){
			return null;
		}
		PrettyTime pt = new PrettyTime(Locale.ENGLISH);
		return pt.format(time);
	}

	public Status getStatus()
	{
		return status;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public Long getNotificationDelayInMilliseconds() {
		return notificationDelayInMilliseconds;
	}

	public void setNotificationDelayInMilliseconds(Long notificationDelayInMilliseconds) {
		this.notificationDelayInMilliseconds = notificationDelayInMilliseconds;
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
