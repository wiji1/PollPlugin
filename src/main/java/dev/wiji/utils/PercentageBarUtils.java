package dev.wiji.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class PercentageBarUtils {

    private static final int BAR_LENGTH = 30;
    private static final char BAR_CHAR = '|';
    private static final char BRACKET_OPEN = '[';
    private static final char BRACKET_CLOSE = ']';

    public static Component createPercentageBar(double percentage) {
        percentage = Math.max(0.0, Math.min(100.0, percentage));

        TextColor barFillColor = getColorForPercentage(percentage);
        String percentageText = String.format("%.0f%%", percentage);

        int textLength = percentageText.length();
        int availableBarChars = BAR_LENGTH - textLength;

        int filledBarChars = (int) Math.round((percentage / 100.0) * availableBarChars);
        int emptyBarChars = availableBarChars - filledBarChars;

        int leftPaddingBarChars = availableBarChars / 2;
        int rightPaddingBarChars = availableBarChars - leftPaddingBarChars;

        TextComponent.Builder builder = Component.text();

        builder.append(Component.text(BRACKET_OPEN, NamedTextColor.GRAY));

        int leftBarFilled = Math.min(leftPaddingBarChars, filledBarChars);
        if (leftBarFilled > 0) {
            builder.append(Component.text(repeat(leftBarFilled), barFillColor));
        }

        int leftBarEmpty = leftPaddingBarChars - leftBarFilled;
        if (leftBarEmpty > 0) {
            builder.append(Component.text(repeat(leftBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(percentageText, NamedTextColor.GRAY));

        filledBarChars -= leftBarFilled;
        emptyBarChars -= leftBarEmpty;

        int rightBarFilled = Math.min(rightPaddingBarChars, filledBarChars);
        if (rightBarFilled > 0) {
            builder.append(Component.text(repeat(rightBarFilled), barFillColor));
        }

        int rightBarEmpty = rightPaddingBarChars - rightBarFilled;
        if (rightBarEmpty > 0) {
            builder.append(Component.text(repeat(rightBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(BRACKET_CLOSE, NamedTextColor.GRAY));

        return builder.build();
    }

    public static Component createCenteredPercentageBar(double percentage) {
        percentage = Math.max(0.0, Math.min(100.0, percentage));

        TextColor barColor = getColorForPercentage(percentage);
        String percentageText = String.format("%.0f%%", percentage);

        String fullPercentageTextSegment = BRACKET_OPEN + percentageText + BRACKET_CLOSE;
        int fullPercentageTextLength = fullPercentageTextSegment.length();

        int availableBarChars = BAR_LENGTH - fullPercentageTextLength;

        int filledBarChars = (int) Math.round((percentage / 100.0) * availableBarChars);
        int emptyBarChars = availableBarChars - filledBarChars;

        int leftPaddingBarChars = availableBarChars / 2;
        int rightPaddingBarChars = availableBarChars - leftPaddingBarChars;

        TextComponent.Builder builder = Component.text();

        builder.append(Component.text(BRACKET_OPEN, NamedTextColor.GRAY));

        int leftBarFilled = Math.min(leftPaddingBarChars, filledBarChars);
        if (leftBarFilled > 0) {
            builder.append(Component.text(repeat(leftBarFilled), barColor));
        }

        int leftBarEmpty = leftPaddingBarChars - leftBarFilled;
        if (leftBarEmpty > 0) {
            builder.append(Component.text(repeat(leftBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(fullPercentageTextSegment, NamedTextColor.GRAY));

        filledBarChars -= leftBarFilled;
        emptyBarChars -= leftBarEmpty;

        int rightBarFilled = Math.min(rightPaddingBarChars, filledBarChars);
        if (rightBarFilled > 0) {
            builder.append(Component.text(repeat(rightBarFilled), barColor));
        }

        int rightBarEmpty = rightPaddingBarChars - rightBarFilled;
        if (rightBarEmpty > 0) {
            builder.append(Component.text(repeat(rightBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(BRACKET_CLOSE, NamedTextColor.GRAY));

        return builder.build();
    }

    private static TextColor getColorForPercentage(double percentage) {
        if (percentage >= 80.0) {
            return NamedTextColor.GREEN;
        } else if (percentage >= 60.0) {
            return NamedTextColor.YELLOW;
        } else if (percentage >= 40.0) {
            return NamedTextColor.GOLD;
        } else if (percentage >= 20.0) {
            return NamedTextColor.RED;
        } else {
            return NamedTextColor.DARK_RED;
        }
    }

    public static TextColor getGradientColor(double percentage) {
        percentage = Math.max(0.0, Math.min(100.0, percentage));

        double ratio = percentage / 100.0;

        int red = (int) (255 * (1 - ratio));
        int green = (int) (255 * ratio);
        int blue = 0;

        return TextColor.color(red, green, blue);
    }

    public static Component createGradientPercentageBar(double percentage) {
        percentage = Math.max(0.0, Math.min(100.0, percentage));

        TextColor barFillColor = getGradientColor(percentage);
        String percentageText = String.format("%.0f%%", percentage);

        String fullPercentageTextSegment = BRACKET_OPEN + percentageText + BRACKET_CLOSE;
        int fullPercentageTextLength = fullPercentageTextSegment.length();

        int availableBarChars = BAR_LENGTH - fullPercentageTextLength;

        int filledBarChars = (int) Math.round((percentage / 100.0) * availableBarChars);
        int emptyBarChars = availableBarChars - filledBarChars;

        int leftPaddingBarChars = availableBarChars / 2;
        int rightPaddingBarChars = availableBarChars - leftPaddingBarChars;

        TextComponent.Builder builder = Component.text();

        builder.append(Component.text(BRACKET_OPEN, NamedTextColor.GRAY));

        int leftBarFilled = Math.min(leftPaddingBarChars, filledBarChars);
        if (leftBarFilled > 0) {
            builder.append(Component.text(repeat(leftBarFilled), barFillColor));
        }

        int leftBarEmpty = leftPaddingBarChars - leftBarFilled;
        if (leftBarEmpty > 0) {
            builder.append(Component.text(repeat(leftBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(fullPercentageTextSegment, NamedTextColor.GRAY));

        filledBarChars -= leftBarFilled;
        emptyBarChars -= leftBarEmpty;

        int rightBarFilled = Math.min(rightPaddingBarChars, filledBarChars);
        if (rightBarFilled > 0) {
            builder.append(Component.text(repeat(rightBarFilled), barFillColor));
        }

        int rightBarEmpty = rightPaddingBarChars - rightBarFilled;
        if (rightBarEmpty > 0) {
            builder.append(Component.text(repeat(rightBarEmpty), NamedTextColor.GRAY));
        }

        builder.append(Component.text(BRACKET_CLOSE, NamedTextColor.GRAY));

        return builder.build();
    }

    private static String repeat(int count) {
        if (count <= 0) return "";
        return new String(new char[count]).replace('\0', PercentageBarUtils.BAR_CHAR);
    }
}
