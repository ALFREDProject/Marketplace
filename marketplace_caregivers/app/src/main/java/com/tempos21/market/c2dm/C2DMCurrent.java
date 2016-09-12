package com.tempos21.market.c2dm;

public class C2DMCurrent {
    private static String userId = null;
    private static int action = -1;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        C2DMCurrent.userId = userId;
    }

    public static int getAction() {
        return action;
    }

    public static void setAction(int action) {
        C2DMCurrent.action = action;
    }

    public static void clearC2DM() {
        action = -1;
        userId = null;
    }

    public static boolean inC2DM() {
        return action > -1;
    }
}
