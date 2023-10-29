package com.wanted.teamr.snsfeedintegration.util;

import java.util.Random;

public class ApprovalCodeGenerator {

    private static final int LENGTH = 6;
    private static final String CANDIDATES;

    static {
        StringBuilder builder = new StringBuilder(10 + 26 + 26);
        for (int i = 0; i <= 9; ++i) {
            builder.append(i);
        }
        for (char c = 'a'; c <= 'z'; ++c) {
            builder.append(c);
        }
        for (char c = 'A'; c <= 'Z'; ++c) {
            builder.append(c);
        }
        CANDIDATES = builder.toString();
    }

    public static String generate() {
        char[] approvalCode = new char[LENGTH];
        Random random = new Random();
        for (int i = 0; i < LENGTH; ++i) {
            int candidateIdx = random.nextInt(0, CANDIDATES.length());
            approvalCode[i] = CANDIDATES.charAt(candidateIdx);
        }
        return new String(approvalCode);
    }

}
