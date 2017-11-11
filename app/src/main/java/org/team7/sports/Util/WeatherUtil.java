package org.team7.sports.Util;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Proudly created by zhangxinye on 7/11/17.
 */


public class WeatherUtil {
    public static String getWeatherForecast() throws JSONException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Date now = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh-mm-ss");

        String API_KEY = "IInoSpVCZEfhT9CQ4Cmt22GiGZKfObGm";
        URL sgGov = new URL("https://api.data.gov.sg/v1/environment/24-hour-weather-forecast?" + ft.format(now));
        URLConnection gov = sgGov.openConnection();
        gov.setRequestProperty("api-key", API_KEY);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        gov.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray myArray = new JSONArray();
        myArray = myResponse.getJSONArray("items");

        JSONObject items = new JSONObject(myArray.get(0).toString());
        JSONObject general = new JSONObject(items.get("general").toString());
        return (general.get("forecast")).toString();
    }
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

    public static String getRecommendation(String s) {
        switch (s) {
            case "Mist":
                return "suitable for sports";
            case "Cloudy":
                return "suitable for sports";

            case "Drizzle":
                return "not suitable for sports";

            case "Fair(Day)":
                return "suitable for sports";

            case "Fog":
                return "not suitable for sports";
            case "Fair(Night)":
                return "suitable for sports";
            case "Fair & Warm":
                return "not suitable for sports";
            case "Heavy Thundery Showers with Gusty Winds":
                return "not suitable for sports";
            case "Heavy Rain":
                return "not suitable for sports";
            case "Heavy Showers":
                return "not suitable for sports";
            case "Hazy":
                return "not suitable for sports";
            case "Slightly Hazy":
                return "not suitable for sports";
            case "Light Rain":
                return "not suitable for sports";
            case "Light Showers":
                return "not suitable for sports";
            case "Overcast":
                return "not suitable for sports";
            case "Partly Cloudy(Day)":
                return "suitable for sports";
            case "Partly Cloudy(Night)":
                return "suitable for sports";
            case "Passing Showers":
                return "not suitable for sports";
            case "Moderate Rain":
                return "not suitable for sports";
            case "Showers":
                return "not suitable for sports";
            case "Strong Winds, Rain":
                return "not suitable for sports";
            case "Strong Winds, Showers":
                return "not suitable for sports";
            case "Sunny":
                return "suitable for sports";
            case "Strong Winds":
                return "not suitable for sports";
            case "Thundery Showers":
                return "not suitable for sports";
            case "Windy,Cloudy":
                return "not suitable for sports";
            case "Windy":
                return "not suitable for sports";
            case "Windy, Fair":
                return "suitable for sports";
            case "Windy, Rain":
                return "not suitable for sports";
            case "Windy, Showers":
                return "not suitable for sports";
            default:
                return "unknown";
        }
    }
}

