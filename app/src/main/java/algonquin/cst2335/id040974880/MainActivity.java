package algonquin.cst2335.id040974880;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author suya Liu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /** This is holds the Button at the bottom of the screen*/
    private Button btn;
    /** This is holds the text at the centre of the screen*/
   private TextView tv;
    /** This is holds the text can be edited*/
   private EditText  et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         tv = findViewById(R.id.textView);
         btn = findViewById(R.id.button);
         et = findViewById(R.id.editText);

         btn.setOnClickListener( clk ->{
             String password = et.getText().toString();
            if(checkPasswordComplexity( password)) {
                tv.setText("Your password is complex enough");
            }
            else{
                tv.setText("You shall not pass");
            }
         });

    }

    /** This function is to check password meet requirement
     *
     * @param pw The string object that we are checking
     * @return returns true if meet requirement
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        char ch;
        for (int i = 0; i < pw.length(); i++) {
            ch = pw.charAt(i);

            if (!foundUpperCase){
                foundUpperCase = Character.isUpperCase(ch);
            }
            if(!foundLowerCase){
                foundLowerCase = Character.isLowerCase(ch);
            }
            if(!foundNumber){
                foundNumber = Character.isDigit(ch);
            }
           if(!foundSpecial){
               foundSpecial = isSpecialCharacter(ch);
           }

        }

            if (!foundUpperCase) {
                Toast.makeText(getApplicationContext(),
                        "Your password does not have an upper case letter", Toast.LENGTH_LONG).show();
                return false;
            } else if (!foundLowerCase) {
                Toast.makeText(getApplicationContext(),
                        "Your password does not have an a lower case letter", Toast.LENGTH_LONG).show();
                return false;
            } else if (!foundNumber) {
                Toast.makeText(getApplicationContext(),
                        "Your password does not have an number", Toast.LENGTH_LONG).show();
                return false;
            } else if (!foundSpecial) {
                Toast.makeText(getApplicationContext(),
                        "Your password does not have a special symbol", Toast.LENGTH_LONG).show();
                return false;
            } else
                return true;

    }

    /**This function to check if the password have special character
     *
     * @param c  The string that holding we are checking
     * @return Retruns true if have special character
     */
    boolean isSpecialCharacter(char c){
        switch(c)
        {
            case '#':
            case '?':
            case '*':
                return true;
            default:
                return false;

        }
    }


}