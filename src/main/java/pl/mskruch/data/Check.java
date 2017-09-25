package pl.mskruch.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

@Entity
public class Check
{
	@Id
	Long id;
	@Index
	String ownerEmail;

	Date created;
	String url;

	Date lastCheck;
	Status status;
	Date statusSince;

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

	@Override
	public String toString()
	{
		return "Check{" + "id=" + id + ", ownerEmail='" + ownerEmail + '\''
			+ ", created=" + created + ", url='" + url + '\'' + '}';
	}
}
