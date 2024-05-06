package com.online.learning.utils;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatUtils {

    private static final DecimalFormat df = new DecimalFormat("###,###,###");

    private static final DecimalFormat dfScore = new DecimalFormat("#.#");

    public static String formatNumber(int value) {
        try {
            String result = df.format(value);
            return result.startsWith(".") ? "0" + result : result;
        } catch (Exception ex) {
            return "";
        }
    }

    public static int formatNumber(String value) {
        try {
            String target = value.replaceAll(",", "").trim();
            return Integer.parseInt(target);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double formatScore(double value) {
        try {
            return Double.parseDouble(dfScore.format(value));
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String toSlug(String input) {
        String lowercaseString = input.toLowerCase();
        String slug = lowercaseString.replaceAll("\\s+", "-");

        return slug.replaceAll("[^a-z0-9-]", "");
    }

    public static String toWhiteSpace(String input) {
        return input.replace("-", " ");
    }

    public static String toEncodePrice(String input) {
        return input.replace(",", "");
    }

}
