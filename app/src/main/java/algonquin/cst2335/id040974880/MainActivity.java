package algonquin.cst2335.id040974880;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    imageView = findViewById(R.id.flagview);
    sw = findViewById(R.id.spin_switch);
    sw.setOnCheckedChangeListener((btn, ischecked)->{
        if(ischecked){
            RotateAnimation rotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotate.setDuration(5000);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setInterpolator(new LinearInterpolator());
            imageView.startAnimation(rotate);
        }
        else{
            imageView.clearAnimation();
        }

    });

//        TextView mytext = findViewById(R.id.textView);
//        Button btn =findViewById(R.id.mybutton);
//        EditText myedit = findViewById(R.id.myedittext);
//        String editString = myedit.getText().toString();
//
//         btn.setOnClickListener( vw  -> mytext.setText("Your edit text has "+editString));
//
//        CheckBox mycb = findViewById(R.id.chechbox1);
//        mycb.setOnCheckedChangeListener((cbox, isChecked)->{
//            Toast.makeText(getApplicationContext(),"You clicked on the Checkbox and it is now: " + isChecked,
//                    Toast.LENGTH_LONG).show();
//        });
//
//
//        Switch myswitch = findViewById(R.id.myswitch);
//        myswitch.setOnCheckedChangeListener((cbox, isChecked)->{
//            Toast.makeText(getApplicationContext(),
//                    "You clicked on the Switch and it is now: " + isChecked,
//                    Toast.LENGTH_SHORT).show();
//        });
//        RadioButton myradiobt = findViewById(R.id.radioButton);
//
//        myradiobt.setOnCheckedChangeListener((cbox, isChecked)->{
//            Toast.makeText(getApplicationContext(),
//                    "You clicked on the Radio Button and it is now: " + isChecked,
//                    Toast.LENGTH_SHORT).show();
//        });
//
//        ImageView myimage = findViewById(R.id.logo_algonquin);
//
//        ImageButton imgbtn = findViewById( R.id.myimagebutton );
//        imgbtn.setOnClickListener(vm->{
//            Toast.makeText(getApplicationContext(),
//                    "The width = " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight(),
//                    Toast.LENGTH_SHORT).show();
//        });




    }


}
