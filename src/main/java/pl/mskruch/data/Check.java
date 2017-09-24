package pl.mskruch.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

@Entity
public class Check
{
	@Id
	Long id;
	@Index
	String ownerEmail;

	Date created;
	String url;

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

	// Date lastCheck;
	// Long secondsDown;

	public Long getId()
	{
		return id;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return "Check{" + "id=" + id + ", ownerEmail='" + ownerEmail + '\''
			+ ", created=" + created + ", url='" + url + '\'' + '}';
	}
}
