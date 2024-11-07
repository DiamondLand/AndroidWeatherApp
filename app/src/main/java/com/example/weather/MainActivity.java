package com.example.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private TextView weatherInfo;
    private static final String API_KEY = "c269502293174e07abe130103242207";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json?key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        weatherInfo = findViewById(R.id.weatherInfo);
        Button getWeatherButton = findViewById(R.id.getWeatherButton);

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherInfo.setText("Ожидайте...");

                String city = cityInput.getText().toString();
                getWeather(city);
            }
        });
    }

    private void getWeather(String city) {
        String url = BASE_URL + API_KEY + "&q=" + city + "&aqi=no&lang=ru";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject current = response.getJSONObject("current");
                            String temp = current.getString("temp_c");
                            String condition = current.getJSONObject("condition").getString("text");
                            String humidity = current.getString("humidity");
                            String windSpeed = current.getString("wind_kph");

                            // Форматируем вывод информации о погоде
                            weatherInfo.setText("Температура: " + temp + "°C\n" +
                                    "Состояние: " + condition + "\n" +
                                    "Влажность: " + humidity + "%\n" +
                                    "Скорость ветра: " + windSpeed + " км/ч");
                        } catch (JSONException e) {
                            weatherInfo.setText("Ошибка получения данных");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherInfo.setText("Ошибка сети");
            }
        });

        queue.add(jsonObjectRequest);
    }
}
