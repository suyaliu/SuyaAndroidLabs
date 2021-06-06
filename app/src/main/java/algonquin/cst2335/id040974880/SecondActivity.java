package algonquin.cst2335.id040974880;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView topOfScreen = findViewById(R.id.textView);

        Intent fromPrevious=getIntent();
        String emailAdress = fromPrevious.getStringExtra("EmailAddress");
        //int age = fromPrevious.getIntExtra("Age",0);

        topOfScreen.setText("You typed " +emailAdress );
        Button btn1 = findViewById(R.id.callButton);
        btn1.setOnClickListener(clk ->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" +"613 302 0588"));
            startActivity(call);
        });

     Button btn_camera = findViewById(R.id.pictureButton);
     btn_camera.setOnClickListener(clk ->{

        Intent cameraPic = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(cameraPic,3456);

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
        if(requestCode == 3456){

            if(resultCode ==RESULT_OK)
            {
                Bitmap thumbnail = data.getParcelableExtra("data");
                profileImage.setImageBitmap(thumbnail);
            }
        }
    }

}