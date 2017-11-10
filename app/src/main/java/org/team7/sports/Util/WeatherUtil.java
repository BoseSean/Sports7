package org.team7.sports.Util;

/**
 * Proudly created by zhangxinye on 7/11/17.
 */


public class WeatherUtil {
    public static int getIcon(String forecast) {
        switch (forecast) {
            case "Mist":
                return 34;
            case "Cloudy":
                return 28;

            case "Drizzle":
                return 9;

            case "Fair(Day)":
                return 30;

            case "Fog":
                return 20;
            case "Fair(Night)":
                return 29;
            case "Fair & Warm":
                return 32;
            case "Heavy Thundery Showers with Gusty Winds":
                return 35;
            case "Heavy Rain":
                return 12;
            case "Heavy Showers":
                return 11;
            case "Hazy":
                return 21;
            case "Slightly Hazy":
                return 21;
            case "Light Rain":
                return 9;
            case "Light Showers":
                return 9;
            case "Overcast":
                return 26;
            case "Partly Cloudy(Day)":
                return 30;
            case "Partly Cloudy(Night)":
                return 29;
            case "Passing Showers":
                return 9;
            case "Moderate Rain":
                return 11;
            case "Showers":
                return 11;
            case "Strong Winds, Rain":
                return 11;
            case "Strong Winds, Showers":
                return 11;
            case "Sunny":
                return 36;
            case "Strong Winds":
                return 24;
            case "Thundery Showers":
                return 0;
            case "Windy,Cloudy":
                return 24;
            case "Windy":

                return 24;
            case "Windy, Fair":
                return 24;
            case "Windy, Rain":
                return 11;
            case "Windy, Showers":
                return 11;
            default:
                return 44;


        }

    }
}

