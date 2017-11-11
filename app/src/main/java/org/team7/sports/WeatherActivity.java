package org.team7.sports;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team7.sports.Util.WeatherUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView forecastTextView;
    private TextView humidityTextView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        forecastTextView = (TextView) findViewById(R.id.forecastTextView);
        humidityTextView = (TextView) findViewById(R.id.humidityTextView);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        toolbar = (Toolbar) findViewById(R.id.create_game_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        try {
            refreshWeather();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button refreshBtn = (Button) findViewById(R.id.weather_refresh_btn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    refreshWeather();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // TODO Add auto refresh
    // TODO Add tool bar
    public void refreshWeather() throws IOException, JSONException {
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
        JSONArray myArray;
        myArray = myResponse.getJSONArray("items");

        JSONObject items = new JSONObject(myArray.get(0).toString());
        JSONObject general = new JSONObject(items.get("general").toString());


        JSONObject temperature = new JSONObject(general.get("temperature").toString());

        JSONObject relative_humidity = new JSONObject(general.get("relative_humidity").toString());
        temperatureTextView.setText(temperature.get("low").toString() + " ~ " + temperature.get("high").toString() + "°C");
        forecastTextView.setText(general.get("forecast").toString());
        humidityTextView.setText(relative_humidity.get("low").toString() + " ~ " + relative_humidity.get("high").toString() + "%");
        String forecast = general.get("forecast").toString();
        int iconNumber = WeatherUtil.getIcon(forecast);
        int id = getResources().getIdentifier("w" + iconNumber, "drawable", getPackageName());
        weatherIconImageView.setImageResource(id);
    }

}
