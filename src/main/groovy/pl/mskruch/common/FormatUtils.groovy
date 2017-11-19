package pl.mskruch.common

import org.ocpsoft.prettytime.PrettyTime

class FormatUtils
{
	static String formatDurationSince(Date time)
	{
		time ? new PrettyTime(Locale.ENGLISH).format(time) : null
	}
}
