package algonquin.cst2335.id040974880;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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
    TextView tv,currentView,maxView,minView,humdityView,descriptionView;
    ImageView iv ;
    Bitmap image = null;
    String description = null;
    String cityName;
    String iconName = null;
    String current = null;
    String minTep = null;
    String maxTep = null;
    String humidity = null;

    float oldSize = 14;

    ImageView iconView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
         currentView = findViewById(R.id.temp);
         minView = findViewById(R.id.MinTemp);
         maxView = findViewById(R.id.MaxTemp);
         humdityView = findViewById(R.id.Humidity);
         descriptionView = findViewById(R.id.Description);
         iv = findViewById(R.id.imageView);
        switch (item.getItemId()){

            case R.id.hide_views:
                currentView.setVisibility(View.INVISIBLE);
                maxView.setVisibility(View.INVISIBLE);
                minView.setVisibility(View.INVISIBLE);
                humdityView.setVisibility(View.INVISIBLE);
                descriptionView.setVisibility(View.INVISIBLE);
                iconView.setVisibility(View.INVISIBLE);
              //  iv.setVisibility(View.INVISIBLE);
                cityText.setText("");//clear the city name
                break;
            case R.id.id_increase:
                  oldSize++;
                  currentView.setTextSize(oldSize);
                  maxView.setTextSize(oldSize);
                  minView.setTextSize(oldSize);
                humdityView.setTextSize(oldSize);
                  descriptionView.setTextSize(oldSize);
                  cityText.setTextSize(oldSize);
                break;

            case R.id.id_decrease:
                oldSize = Float.max(oldSize-1,5);
                currentView.setTextSize(oldSize);
                maxView.setTextSize(oldSize);
                minView.setTextSize(oldSize);
                humdityView.setTextSize(oldSize);
                descriptionView.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;
            case 5://re-run a previous search:
                 cityName = item.getTitle().toString();
                runForecast(cityName);
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void runForecast(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actibity_actions,menu);

        return true;
    }

    EditText cityText;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        tv = findViewById(R.id.textView);
        forecatButton = findViewById(R.id.forecastbutton);
        cityText = findViewById(R.id.editText);
        //navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item)->{
            onOptionsItemSelected(item);// call the function for the other Toolbar
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });


        forecatButton.setOnClickListener(clk -> {
            String cityName = cityText.getText().toString();
            // add items to overflow menu
            myToolbar.getMenu().add(1,5,10,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting forecast")
                    .setMessage("we are calling people in "+cityName+" to ollk outside their windows and tell us what's the weahter like over there")
                    .setView(new ProgressBar(MainActivity.this))
                    .show();

            //connect to serve

            iv = findViewById(R.id.icon);
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(() -> {
                URL url = null;
                try {
                    String serverURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityText.getText().toString(), "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";

                    url = new URL(serverURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8");

                  //  int eventType = xpp.getEventType();
                    while (xpp.next()!= XmlPullParser.END_DOCUMENT) {
                        switch (xpp.getEventType()){
                            case XmlPullParser.START_TAG:
                                if(xpp.getName().equalsIgnoreCase("temperature")){
                                    current = xpp.getAttributeValue(null, "value");
                                    //this gets the current temperature
                                    minTep = xpp.getAttributeValue(null, "min");
                                    //this gets the min temperature
                                    maxTep = xpp.getAttributeValue(null, "max");
                                    //this gets the max temperature
                                }else if(xpp.getName().equalsIgnoreCase("weather")){
                                    description = xpp.getAttributeValue(null, "value");
                                    //this gets the weather description
                                    iconName = xpp.getAttributeValue(null, "icon");
                                    //this gets the icon name
                                } else if (xpp.getName().equalsIgnoreCase("humidity")) {
                                    humidity = xpp.getAttributeValue(null,"value");
                                }

                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                break;
                            case  XmlPullParser.START_DOCUMENT:
                                break;
                            case  XmlPullParser.END_DOCUMENT:
                                break;
                        }

                    }
                    File file = new File(getFilesDir(), iconName + ".png");
                    if (file.exists()) {
                        image = BitmapFactory.decodeFile(getFilesDir()+"/"+iconName+".png");
                    }else{
                        URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                        HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(connection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName+".png", Activity.MODE_PRIVATE));
                        }}

                    runOnUiThread(()->{
                        currentView = findViewById(R.id.temp);
                        currentView.setText("The current temperature is " +current);
                        currentView.setVisibility(View.VISIBLE);

                        minView = findViewById(R.id.MinTemp);
                        minView.setText("The min temperature is " + minTep);
                        minView.setVisibility(View.VISIBLE);


                        maxView = findViewById(R.id.MaxTemp);
                        maxView.setText("The max temperature is " + maxTep);
                        maxView.setVisibility(View.VISIBLE);

                        humdityView = findViewById(R.id.Humidity);
                        humdityView.setText("The Humidity is " + humidity);
                        humdityView.setVisibility(View.VISIBLE);

                        descriptionView = findViewById(R.id.Description);
                        descriptionView.setText("The Description is " + description);
                        descriptionView.setVisibility(View.VISIBLE);
                        //iv.setImageBitmap(image);


                        iconView = findViewById(R.id.icon);
                        iconView.setImageBitmap(image);
                        iconView.setVisibility(View.VISIBLE);

                        dialog.hide();

                    });

                } catch (IOException | XmlPullParserException e ) {
                    Log.e("connection error:", e.getMessage());
                }
            });


        });


    }

}
//    String text = (new BufferedReader(
//            new InputStreamReader(in, StandardCharsets.UTF_8)))
//            .lines()
//            .collect(Collectors.joining("\n"));


//convert String to Json object:
//                    JSONObject theDocument = new JSONObject( text );
//
//                    //get Jasonobject
//                    JSONObject coord = theDocument.getJSONObject("coord");
//                    int vis=  theDocument.getInt("visibility");
//
////                    double lat = coord.getDouble("lat");
////                    double lon = coord.getDouble("lon");
//                    JSONArray weatherArray = theDocument.getJSONArray("weather");
//                    JSONObject obj0= weatherArray.getJSONObject(0);
//                    String description = obj0.getString("description");
//                    String iconName = obj0.getString("icon");
//
//
//                    JSONObject main = theDocument.getJSONObject("main");
//                    double currenTemp = main.getDouble("temp");
//                    double min = main.getDouble("temp_min");
//                    double max = main.getDouble("temp_max");
//                    int humidity = main.getInt("humidity");

                    //     String name = theDocument.getString("name");

//                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
//                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
//                    connection.connect();
//                    int responseCode = connection.getResponseCode();
//                    if (responseCode == 200) {
//                        image = BitmapFactory.decodeStream(connection.getInputStream());
//
//
//                    }
//
//                    FileOutputStream fOut = null;
//                    try {
//                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
//                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//                        fOut.flush();
//                        fOut.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//
//                    }
//                    runOnUiThread(()->{
//                        TextView tv = findViewById(R.id.temp);
//                        tv.setText("The current temperature is " +currenTemp);
//                        tv.setVisibility(View.VISIBLE);
//
//                        tv = findViewById(R.id.MinTemp);
//                        tv.setText("The min temperature is " + min);
//                        tv.setVisibility(View.VISIBLE);
//
//
//                        tv = findViewById(R.id.MaxTemp);
//                        tv.setText("The max temperature is " + max);
//                        tv.setVisibility(View.VISIBLE);
//
//                        tv = findViewById(R.id.Humidity);
//                        tv.setText("The Humidity is " + humidity);
//                        tv.setVisibility(View.VISIBLE);
//
//                        tv = findViewById(R.id.Description);
//                        tv.setText("The Description is " + description);
//                        tv.setVisibility(View.VISIBLE);
//                        iv.setImageBitmap(image);
//                    });

                    
