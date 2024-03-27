package com.health.myapplication.ui.mon;

import java.util.List;


public class FuzzyStringMatcher {

    private static int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j - 1] + costOfSubstitution(s1.charAt(i - 1), s2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int... numbers) {
        int min = Integer.MAX_VALUE;
        for (int number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }

    public static double calculateSimilarity(String s1, String s2) {
        int distance = calculateLevenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());

        return 1.0 - (double) distance / maxLength;
    }

    public static String findMostSimilarString(List<String> strings, String input) {
        double maxSimilarity = 0.0;
        String mostSimilarString = "";

        for (String str : strings) {
            double similarity = calculateSimilarity(input, str);
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                mostSimilarString = str;
            }
        }

        return mostSimilarString;
    }
}
