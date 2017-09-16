package com.binaa.android.binaa.server;

import java.util.Locale;

public class Constants {

    private static final String apiUrl = "http://restart-technology.com/binaa/public/api/";


    public static String getApiUrl() {
        return apiUrl;
    }


    public static boolean isArabic() {
        return Locale.getDefault().getISO3Language().toLowerCase().contains("ar");
    }

    public static boolean isEnglish() {
        return Locale.getDefault().getISO3Language().toLowerCase().contains("en");
    }


}
