package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private TextView descr_spb;
    private TextView temp_spb;
    private TextView fl_spb;
    private TextView wind_spb;
    private TextView time_spb;
    private ImageView cloud_spb;
    private ImageView rain_spb;
    private ImageView sun_spb;

    private TextView descr_tsk;
    private TextView temp_tsk;
    private TextView fl_tsk;
    private TextView wind_tsk;
    private TextView time_tsk;
    private ImageView cloud_tsk;
    private ImageView rain_tsk;
    private ImageView sun_tsk;

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
        Button main_btn_spb = findViewById(R.id.spb_btn);

        temp_spb = findViewById(R.id.temp_spb);
        temp_tsk = findViewById(R.id.temp_tsk);

        fl_spb = findViewById(R.id.fl_spb);
        fl_tsk = findViewById(R.id.fl_tsk);

        wind_spb = findViewById(R.id.wind_spb);
        wind_tsk = findViewById(R.id.wind_tsk);

        descr_tsk = findViewById(R.id.descr_tsk);
        descr_spb = findViewById(R.id.descr_spb);

        time_tsk = findViewById(R.id.time_tsk);
        time_spb = findViewById(R.id.time_spb);

        cloud_tsk = findViewById(R.id.cloud_tsk);
        rain_tsk = findViewById(R.id.rain_tsk);
        sun_tsk = findViewById(R.id.sun_tsk);

        cloud_spb = findViewById(R.id.cloud_spb);
        rain_spb = findViewById(R.id.rain_spb);
        sun_spb = findViewById(R.id.sun_spb);

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=56.501041&lon=84.992455&appid=be59c27f4f9fe68c807fdd8034c9e97b&units=metric&lang=en";
        String url_spb = "https://api.openweathermap.org/data/2.5/weather?lat=59.937500&lon=30.308611&appid=be59c27f4f9fe68c807fdd8034c9e97b&units=metric&lang=en";


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetURLData getURLData = new GetURLData();
                getURLData.execute(url);
            }
        });

        main_btn_spb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            descr_tsk.setText("...");
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

                StringBuilder buffer = new StringBuilder();
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
                time_tsk.setText(df.format(Calendar.getInstance().getTime()));
                JSONObject jsonObject = new JSONObject(result);
                temp_tsk.setText((int)jsonObject.getJSONObject("main").getDouble("temp")+"°C");
                fl_tsk.setText((int)jsonObject.getJSONObject("main").getDouble("feels_like")+"°C");
                wind_tsk.setText((int)jsonObject.getJSONObject("wind").getDouble("speed")+" m/s");
                descr_tsk.setText((CharSequence) jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));

                if(descr_tsk.getText().equals("clear sky")){
                    sun_tsk.setVisibility(View.VISIBLE);
                }else if(descr_tsk.getText().equals("few clouds")
                        ||descr_tsk.getText().equals("scattered clouds")
                        ||descr_tsk.getText().equals("broken clouds")){
                    cloud_tsk.setVisibility(View.VISIBLE);
                }else{
                    rain_tsk.setVisibility(View.VISIBLE);
                }
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
           @SuppressLint({"SetTextI18n"})
           @Override
           protected void onPostExecute(String result){
            super.onPostExecute(result);

               try {
                   time_spb.setText(df.format(Calendar.getInstance().getTime()));
                   JSONObject jsonObject = new JSONObject(result);
                   temp_spb.setText(((int) jsonObject.getJSONObject("main").getDouble("temp"))+"°C");
                   fl_spb.setText((int)jsonObject.getJSONObject("main").getDouble("feels_like")+"°C");
                   wind_spb.setText((int)jsonObject.getJSONObject("wind").getDouble("speed")+" m/s");
                   descr_spb.setText((CharSequence) jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));

                   if(descr_spb.getText().equals("clear sky")){
                       sun_spb.setVisibility(View.VISIBLE);
                   }else if(descr_spb.getText().equals("few clouds")
                           ||descr_spb.getText().equals("scattered clouds")
                           ||descr_spb.getText().equals("broken clouds")){
                       cloud_spb.setVisibility(View.VISIBLE);
                   }else{
                       rain_spb.setVisibility(View.VISIBLE);
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
        }
    }
