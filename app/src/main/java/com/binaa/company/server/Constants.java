package com.binaa.company.server;

import java.util.Locale;

public class Constants {

    private static final String apiUrl = "http://binaacompany.com/api/";

    public static final String ENGLISH = "en";
    public static final String ARABIC = "ar";


    public static String getApiUrl() {
        return apiUrl;
    }


    public static boolean isArabic() {
        return Locale.getDefault().getISO3Language().toLowerCase().contains("ar");
    }


}
