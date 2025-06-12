package dev.wiji.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeUtils {

	private static final long SECOND_MILLIS = 1000;
	private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
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
		} else if (absDiff < MONTH_MILLIS) {
			value = absDiff / DAY_MILLIS;
			timeUnit = value == 1 ? "day" : "days";
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

	public static String formatDuration(long durationMillis) {
		long remainingMillis = Math.abs(durationMillis);
		if (remainingMillis < SECOND_MILLIS) {
			return "Less than a second";
		}

		List<String> parts = new ArrayList<>();

		long years = remainingMillis / YEAR_MILLIS;
		if (years > 0) {
			parts.add(years + " " + (years == 1 ? "year" : "years"));
			remainingMillis %= YEAR_MILLIS;
		}

		long months = remainingMillis / MONTH_MILLIS;
		if (months > 0) {
			parts.add(months + " " + (months == 1 ? "month" : "months"));
			remainingMillis %= MONTH_MILLIS;
		}

		long days = remainingMillis / DAY_MILLIS;
		if (days > 0) {
			parts.add(days + " " + (days == 1 ? "day" : "days"));
			remainingMillis %= DAY_MILLIS;
		}

		long hours = remainingMillis / HOUR_MILLIS;
		if (hours > 0) {
			parts.add(hours + " " + (hours == 1 ? "hour" : "hours"));
			remainingMillis %= HOUR_MILLIS;
		}

		long minutes = remainingMillis / MINUTE_MILLIS;
		if (minutes > 0) {
			parts.add(minutes + " " + (minutes == 1 ? "minute" : "minutes"));
			remainingMillis %= MINUTE_MILLIS;
		}

		long seconds = remainingMillis / SECOND_MILLIS;
		if (seconds > 0) {
			parts.add(seconds + " " + (seconds == 1 ? "second" : "seconds"));
		}

		return parts.stream().limit(2).collect(Collectors.joining(", "));
	}

	public static String getDuration(long timestampMillis) {
		return getDuration(timestampMillis, System.currentTimeMillis());
	}

	public static String getDuration(long timestampMillis, long currentTimeMillis) {
		long diff = Math.abs(timestampMillis - currentTimeMillis);
		return formatDuration(diff);
	}

	public static long durationToMillis(long value, String timeUnit) {
		timeUnit = timeUnit.toLowerCase();
		switch (timeUnit) {
			case "second": case "seconds": return value * SECOND_MILLIS;
			case "minute": case "minutes": return value * MINUTE_MILLIS;
			case "hour":   case "hours":   return value * HOUR_MILLIS;
			case "day":    case "days":    return value * DAY_MILLIS;
			case "month":  case "months":  return value * MONTH_MILLIS;
			case "year":   case "years":   return value * YEAR_MILLIS;
			default: throw new IllegalArgumentException("Unknown time unit: " + timeUnit);
		}
	}

	public static long durationStringToMillis(String duration) {
		if (duration == null || duration.trim().isEmpty()) {
			throw new IllegalArgumentException("Duration string cannot be null or empty.");
		}
		return parseAbbreviatedDuration(duration);
	}

	private static long parseAbbreviatedDuration(String duration) {
		duration = duration.replaceAll("\\s+", "");

		long totalMillis = 0;
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)([a-zA-Z]+)");
		java.util.regex.Matcher matcher = pattern.matcher(duration);

		boolean foundMatch = false;
		while (matcher.find()) {
			foundMatch = true;
			long value = Long.parseLong(matcher.group(1));
			String unit = matcher.group(2).toLowerCase();

			switch (unit) {
				case "s": case "sec": case "second": case "seconds":
					totalMillis += value * SECOND_MILLIS;
					break;
				case "m": case "min": case "minute": case "minutes":
					totalMillis += value * MINUTE_MILLIS;
					break;
				case "h": case "hr": case "hour": case "hours":
					totalMillis += value * HOUR_MILLIS;
					break;
				case "d": case "day": case "days":
					totalMillis += value * DAY_MILLIS;
					break;
				case "mo": case "month": case "months":
					totalMillis += value * MONTH_MILLIS;
					break;
				case "y": case "yr": case "year": case "years":
					totalMillis += value * YEAR_MILLIS;
					break;
				default:
					throw new IllegalArgumentException("Unknown time unit abbreviation: " + unit);
			}
		}

		if (!foundMatch) {
			throw new IllegalArgumentException("Invalid duration format. No valid time units found in: " + duration);
		}

		return totalMillis;
	}
}