package pl.mskruch.common

class DateUtils
{
	static long secondsBetween(Date first, Date second)
	{
		(first.getTime() - second.getTime()) / 1000
	}
}
