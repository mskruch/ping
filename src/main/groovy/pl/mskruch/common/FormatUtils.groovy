package pl.mskruch.common

import org.ocpsoft.prettytime.PrettyTime

class FormatUtils
{
	public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

	static String formatDurationSince(Date time)
	{
		time ? new PrettyTime(Locale.ENGLISH).format(time) : null
	}
}
