package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private TextView temp_spb;
    private TextView fl_spb;
    private TextView wind_spb;

    private TextView temp_tsk;
    private TextView fl_tsk;
    private TextView wind_tsk;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//switch night mod off
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.view_flipper);
        /*viewFlipper.setFlipInterval(2000); //auto flip
        viewFlipper.startFlipping();*/



        Button main_btn = findViewById(R.id.tsk_btn);

        temp_spb = findViewById(R.id.temp_spb);
        temp_tsk = findViewById(R.id.temp_tsk);
        fl_spb = findViewById(R.id.fl_spb);
        fl_tsk = findViewById(R.id.fl_tsk);
        wind_spb = findViewById(R.id.wind_spb);
        wind_tsk = findViewById(R.id.wind_tsk);



        String url = "https://api.openweathermap.org/data/2.5/weather?lat=56.501041&lon=84.992455&appid=be59c27f4f9fe68c807fdd8034c9e97b&units=metric&lang=ru";
        String url_spb = "https://api.openweathermap.org/data/2.5/weather?lat=59.937500&lon=30.308611&appid=be59c27f4f9fe68c807fdd8034c9e97b&units=metric&lang=ru";


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetURLData getURLData = new GetURLData();
                getURLData.execute(url);
                GetURLData2 getURLData2 = new GetURLData2();
                getURLData2.execute(url_spb);
            }
        });
    }
    public void previousView(View v) {
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.showPrevious();
    }

    public void nextView(View v) {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        viewFlipper.showNext();
    }


    private class GetURLData extends AsyncTask<String,String,String>{//будет работать асинхронно

        protected void onPreExecute(){
            super.onPreExecute();
            temp_tsk.setText("...");
            wind_tsk.setText("...");
            fl_tsk.setText("...");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);// url-соединение, 0 - потому что первый элемент массива strings это url оказывается
                connection = (HttpURLConnection) url.openConnection(); //http-соединение
                connection.connect();

                InputStream stream = connection.getInputStream();//считываем полученный поток
                reader = new BufferedReader(new InputStreamReader(stream));//для считывания в формате строки

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line= reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null){
                    connection.disconnect();
                }
                try{
                    if(reader!=null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                temp_tsk.setText(jsonObject.getJSONObject("main").getDouble("temp")+"°C");
                fl_tsk.setText(jsonObject.getJSONObject("main").getDouble("feels_like")+"°C");
                wind_tsk.setText(jsonObject.getJSONObject("wind").getDouble("speed")+" m/s");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetURLData2 extends AsyncTask<String,String,String>{//будет работать асинхронно

        protected void onPreExecute(){
            super.onPreExecute();
            temp_spb.setText("...");
            wind_spb.setText("...");
            fl_spb.setText("...");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);// url-соединение, 0 - потому что первый элемент массива strings это url оказывается
                connection = (HttpURLConnection) url.openConnection(); //http-соединение
                connection.connect();

                InputStream stream = connection.getInputStream();//считываем полученный поток
                reader = new BufferedReader(new InputStreamReader(stream));//для считывания в формате строки

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line= reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null){
                    connection.disconnect();
                }
                try{
                if(reader!=null) {
                    reader.close();
                }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            return null;
            }
           @SuppressLint("SetTextI18n")
           @Override
           protected void onPostExecute(String result){
            super.onPostExecute(result);

               try {
                   JSONObject jsonObject = new JSONObject(result);
                   temp_spb.setText(jsonObject.getJSONObject("main").getDouble("temp")+"°C");
                   fl_spb.setText(jsonObject.getJSONObject("main").getDouble("feels_like")+"°C");
                   wind_spb.setText(jsonObject.getJSONObject("wind").getDouble("speed")+" m/s");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
        }
    }
