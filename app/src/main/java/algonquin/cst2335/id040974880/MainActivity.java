package algonquin.cst2335.id040974880;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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

/**
 * @author suya Liu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    Button forecatButton;
    TextView tv;
    EditText cityText;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv = findViewById(R.id.textView);
        forecatButton = findViewById(R.id.button);
        cityText = findViewById(R.id.editText);

        forecatButton.setOnClickListener(clk -> {
            //connect to serve


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

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        });


    }

}