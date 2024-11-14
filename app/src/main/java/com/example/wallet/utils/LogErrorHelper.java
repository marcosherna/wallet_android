package com.example.wallet.utils;

import android.util.Log;

public class LogErrorHelper {
    public static void print(Throwable throwable) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.println(Log.ERROR, methodName, "Error: " + throwable.getClass().getName() + " " + throwable.getMessage());
    }
}
