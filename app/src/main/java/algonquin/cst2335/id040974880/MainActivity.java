package algonquin.cst2335.id040974880;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private static final String FILE_NAME = "week4.txt";

    public static final String EMAIL = "email";
    Button loginButton;
    EditText editEmail;
    TextView textView;
    Intent callIntent;
    Intent cameraIntent;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          // Log.d(TAG,"Message");
        Log.w("MainActivity", "In onCreate()-Loading Widgets");
        loginButton = findViewById(R.id.loginButton);
        editEmail = findViewById(R.id.editEmail);
        //load email from sharedpreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("Email", "");
        editEmail.setText(emailAddress);

        loginButton.setOnClickListener(clk -> {


            //save to disk
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Email", editEmail.getText().toString());
            editor.apply();

            callIntent = new Intent(MainActivity.this, SecondActivity.class);
            callIntent.putExtra("EmailAddress", editEmail.getText().toString());
            callIntent.putExtra("Age", 40);
            startActivityForResult(callIntent, 1234);


        });


        File file = new File("FILE_NAME");
        if (file.exists()) {
            ImageView imageView = findViewById(R.id.imageView);

            Bitmap theImage = BitmapFactory.decodeFile(FILE_NAME);
            imageView.setImageBitmap(theImage);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent fromNextPage = data;
        if (requestCode == 1234) {
            if (resultCode == 123) {
                String city = fromNextPage.getStringExtra("city");
                int age = fromNextPage.getIntExtra("Age", 0);
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "in onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "in onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "in onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "in onDestroy()");
    }


}