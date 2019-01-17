/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - Utils.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.content.res.Resources;


public class Utils {
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
