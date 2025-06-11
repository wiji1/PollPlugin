package dev.wiji.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

	private static final long SECOND_MILLIS = 1000;
	private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
	private static final long WEEK_MILLIS = 7 * DAY_MILLIS;
	private static final long MONTH_MILLIS = 30 * DAY_MILLIS;
	private static final long YEAR_MILLIS = 365 * DAY_MILLIS;

	public static String getRelativeTime(long timestampMillis) {
		long currentTime = System.currentTimeMillis();
		return getRelativeTime(timestampMillis, currentTime);
	}

	public static String getRelativeTime(long timestampMillis, long currentTimeMillis) {
		long diff = timestampMillis - currentTimeMillis;
		long absDiff = Math.abs(diff);
		boolean isFuture = diff > 0;

		if (absDiff < MINUTE_MILLIS) {
			return "Just now";
		}

		String timeUnit;
		long value;

		if (absDiff < HOUR_MILLIS) {
			value = absDiff / MINUTE_MILLIS;
			timeUnit = value == 1 ? "minute" : "minutes";
		} else if (absDiff < DAY_MILLIS) {
			value = absDiff / HOUR_MILLIS;
			timeUnit = value == 1 ? "hour" : "hours";
		} else if (absDiff < WEEK_MILLIS) {
			value = absDiff / DAY_MILLIS;
			timeUnit = value == 1 ? "day" : "days";
		} else if (absDiff < MONTH_MILLIS) {
			value = absDiff / WEEK_MILLIS;
			timeUnit = value == 1 ? "week" : "weeks";
		} else if (absDiff < YEAR_MILLIS) {
			value = absDiff / MONTH_MILLIS;
			timeUnit = value == 1 ? "month" : "months";
		} else {
			value = absDiff / YEAR_MILLIS;
			timeUnit = value == 1 ? "year" : "years";
		}

		if (isFuture) {
			return "In " + value + " " + timeUnit;
		} else {
			return value + " " + timeUnit + " ago";
		}
	}

	public static String getRelativeTimeWithPrecision(long timestampMillis) {
		Instant now = Instant.now();
		Instant target = Instant.ofEpochMilli(timestampMillis);

		boolean isFuture = target.isAfter(now);
		Instant earlier = isFuture ? now : target;
		Instant later = isFuture ? target : now;

		long seconds = ChronoUnit.SECONDS.between(earlier, later);
		long minutes = ChronoUnit.MINUTES.between(earlier, later);
		long hours = ChronoUnit.HOURS.between(earlier, later);
		long days = ChronoUnit.DAYS.between(earlier, later);

		String result;

		if (seconds < 60) {
			result = "Just now";
		} else if (minutes < 60) {
			result = minutes + " " + (minutes == 1 ? "minute" : "minutes");
		} else if (hours < 24) {
			result = hours + " " + (hours == 1 ? "hour" : "hours");
		} else if (days < 7) {
			result = days + " " + (days == 1 ? "day" : "days");
		} else if (days < 30) {
			long weeks = days / 7;
			result = weeks + " " + (weeks == 1 ? "week" : "weeks");
		} else if (days < 365) {
			long months = days / 30;
			result = months + " " + (months == 1 ? "month" : "months");
		} else {
			long years = days / 365;
			result = years + " " + (years == 1 ? "year" : "years");
		}

		if (result.equals("Just now")) {
			return result;
		}

		return isFuture ? "In " + result : result + " ago";
	}
}