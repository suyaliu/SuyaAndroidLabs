package algonquin.cst2335.id040974880;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    Button forecatButton;
    TextView tv;
    ImageView iv ;
    Bitmap image = null;
            EditText cityText;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv = findViewById(R.id.textView);
        forecatButton = findViewById(R.id.forecastbutton);
        cityText = findViewById(R.id.editText);

        forecatButton.setOnClickListener(clk -> {
            //connect to serve

            iv = findViewById(R.id.icon);
            //Here's how to create a second thread:
            // Executor is actually an Interface that has only 1 function: execute(Runnable r);
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(() -> {
                URL url = null;
                try {
                    //
                    String serverURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityText.getText().toString(), "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
// creates a URL object and you pass in the URL of the server you want to connect to as a String
                    url = new URL(serverURL);
 //connects to the server
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 //waits for a response from the server, The incoming data is represented as an InputStream
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//convert the inputStream from the server into a Java String object
                    //creates a BufferedReader object
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()// converts it to the lines of text using the ".lines()" call
                            //then calls ".collect( )" to combine the different lines of text into one long string.
                            // Copy this code into click handler after you have connected to the server:
                            .collect(Collectors.joining("\n"));

                    //convert String to Json object:
                    JSONObject theDocument = new JSONObject( text );

                    //get { }Jasonobject
                    JSONObject coord = theDocument.getJSONObject("coord");
                    int vis=  theDocument.getInt("visibility");

//                    double lat = coord.getDouble("lat");
//                    double lon = coord.getDouble("lon");
                    //[ ] meaning it's a JSONArray
                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject obj0= weatherArray.getJSONObject(0);
                    String description = obj0.getString("description");
                    String iconName = obj0.getString("icon");


                    JSONObject main = theDocument.getJSONObject("main");
                    double currenTemp = main.getDouble("temp");
                    double min = main.getDouble("temp_min");
                    double max = main.getDouble("temp_max");
                    int humidity = main.getInt("humidity");
                    File file = new File(getFilesDir(),iconName +".png");
                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir()+"/"+iconName+".png");

                    }
                    else{


                    //     String name = theDocument.getString("name");

                    //Here you are making a second HTTP request to the server and
                    // the BitmapFactory.decodeStream() is reading the data from the server.
                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        // set the ImageView's image to the Bitmap that you downloaded:
//                        ImageView iv = findViewById(R.id.icon);
//                        iv.setImageBitmap(image);
                        image.compress(Bitmap.CompressFormat.PNG,100,openFileOutput(iconName+".png", Activity.MODE_PRIVATE));
                    }
                    }
                    //save the icon to the device so that next time
                    // you don't have to download if you've already downloaded i
                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }
//only call setText(), setVisibility(), setImageBitmap() from the main GUI thread.
// Otherwise you can get into some complicated timing bugs when you call functions across threads.
// To make sure that you are calling these functions on the main GUI thread, you need to call this function
                    runOnUiThread(()->{
                        TextView tv = findViewById(R.id.temp);
                        tv.setText("The current temperature is " +currenTemp);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.MinTemp);
                        tv.setText("The min temperature is " + min);
                        tv.setVisibility(View.VISIBLE);


                        tv = findViewById(R.id.MaxTemp);
                        tv.setText("The max temperature is " + max);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.Humidity);
                        tv.setText("The Humidity is " + humidity);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.Description);
                        tv.setText("The Description is " + description);
                        tv.setVisibility(View.VISIBLE);
                        iv = findViewById(R.id.icon);

                        iv.setVisibility(View.VISIBLE);
                        iv.setImageBitmap(image);
                    });

                    

                } catch (IOException | JSONException e) {
                    Log.e("connection error:", e.getMessage());
                }
            });


        });


    }

}