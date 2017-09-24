package com.example.rashasaadeh.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView weatherText;
    Button weatherButton;
    EditText cityName;



    public class DownloadTask extends AsyncTask<String, Void, String>{ //Async Task allows us to run a background thread, pass string, return string

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection(); //sets up url connection
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char) data;

                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) { //method called when do in background is completed
            //this method can interact with the UI

            super.onPostExecute(result);
            //extracting string into json data
            try {
                JSONObject jsonObject = new JSONObject(result); //may fail if result string is empty
                JSONObject weatherDetails = jsonObject.getJSONObject("current");
                String weatherInfo = jsonObject.getString("current"); //extract the weather part of the json object
                Log.i("Website content", weatherInfo); //Website content: [{"id":300,"main":"Drizzle","description":"light intensity drizzle","icon":"09d"}

                String temp_c = weatherDetails.getString("temp_c");
                String temp_f = weatherDetails.getString("temp_f");

                weatherText.setText("Celsius: " + temp_c + "\n" + "Fahrenheit: "  + temp_f);
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherText = (TextView) findViewById(R.id.weatherText);
        cityName = (EditText) findViewById(R.id.cityName);

    }

    public void onClick(View view){

        String city =  cityName.getText().toString();

        DownloadTask task= new DownloadTask();
        task.execute("http://api.apixu.com/v1/current.json?key=91839f454ecd4233bf5121820171507&q=" + city);

    }
}
