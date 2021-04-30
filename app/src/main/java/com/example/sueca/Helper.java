package com.example.sueca;

import android.content.Context;
import android.os.Bundle;

public class Helper {
    private static Helper mInstance;
    int width;
    int height;
    private Helper() {

    }

    public static synchronized Helper getInstance() {
        if (mInstance == null) {
            mInstance = new Helper();
        }
        return mInstance;
    }
}
