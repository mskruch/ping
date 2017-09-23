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
	// Boolean active;

	// Date lastCheck;
	// Long secondsDown;
}
