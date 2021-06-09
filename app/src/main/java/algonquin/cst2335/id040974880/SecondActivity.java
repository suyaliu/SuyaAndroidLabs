package algonquin.cst2335.id040974880;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    private static String TAG = "SecondActivity";
    EditText inputPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView topOfScreen = findViewById(R.id.textView);
        //load phone from sharedpreferences
        SharedPreferences prefs = getSharedPreferences("MyPhone", Context.MODE_PRIVATE);
        int phoneNumber = prefs.getInt("PhoneNumber", 0);
        inputPhoneNumber.setText(Integer.toString( phoneNumber ));


        inputPhoneNumber = findViewById(R.id.editTextPhone);
        Intent fromPrevious = getIntent();
        String emailAdress = fromPrevious.getStringExtra("EmailAddress");
        //int age = fromPrevious.getIntExtra("Age",0);

        topOfScreen.setText("You typed " + emailAdress);
        Button btn1 = findViewById(R.id.callButton);
        btn1.setOnClickListener(clk -> {

            //save phone to disk
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("PhoneNumber", Integer.parseInt(inputPhoneNumber.getText().toString()));
            editor.apply();
            String callNumber = inputPhoneNumber.getText().toString();

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" +callNumber));
            startActivity(call);
        });

        Button btn_camera = findViewById(R.id.pictureButton);
        btn_camera.setOnClickListener(clk -> {

            Intent cameraPic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraPic, 3456);

//         Intent databack = new Intent();
//         databack.putExtra("Age",35);
//         databack.putExtra("city","Ottawa");
//         setResult(123,databack);
//         finish();

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profileImage = findViewById(R.id.imageView);
        if (requestCode == 3456) {

            if (resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                profileImage.setImageBitmap(thumbnail);

                FileOutputStream fout = null;
                try {
                    fout = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fout);
                    fout.flush();
                    fout.close();
                    //  Toast.makeText(this,"saved to "+getFilesDir()+"/"+FILE_NAME,Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "in onPause()");
    }
}