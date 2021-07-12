package algonquin.cst2335.id040974880;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ChatRoom extends AppCompatActivity {
    boolean isTablet = false;
    MessageListFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        isTablet = findViewById(R.id.detailsRoom) !=null;

        //need a fragment object:
        chatFragment= new MessageListFragment();

        FragmentManager fMgr = getSupportFragmentManager(); // only one object
        FragmentTransaction tx = fMgr.beginTransaction();

        tx.add(R.id.fragmentRoom,chatFragment);

        tx.commit();


    }


    public void userClickMessage(MessageListFragment.ChatMessage chatMessage, int positon) {
      //  MessageListFragment mdFragment = new MessageListFragment();
        MessageDetailsFragment mFragement = new MessageDetailsFragment(chatMessage,positon);


        if(isTablet)
        {

        getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom,mFragement).commit();
        }else //on a phone
        {
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom,mFragement).commit();
        }
    }

    public void notifyMessageDeleted(MessageListFragment.ChatMessage chosenMessage, int chosenPosition) {
        chatFragment.notifyMessageDeleted(chosenMessage,chosenPosition);

    }
}
