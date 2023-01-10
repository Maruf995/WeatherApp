package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.DateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private Button main_btn;
    private TextView result_info;
    TextView date_mounth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user = findViewById(R.id.user);
        result_info = findViewById(R.id.result_info);
        main_btn = findViewById(R.id.main_btn);


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_input, Toast.LENGTH_LONG).show();
                else {
                    String city = user.getText().toString();
                    String key = "2533d80175e4e2fc0f432d20475ce357";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
                    new GetUrlData().execute(url);

                    date_mounth = findViewById(R.id.date_mounth);
                    Date currenTime = Calendar.getInstance().getTime();
                    String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currenTime);

                    String[] splitDate = formattedDate.split(",");

                    date_mounth.setText(splitDate[0]);


                }
            }

        });


    }

    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            result_info.setText("");

        }


        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("" + jsonObject.getJSONObject("main").getDouble("temp")+"°");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}