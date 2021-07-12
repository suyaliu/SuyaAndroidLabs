package algonquin.cst2335.id040974880;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment{
    RecyclerView chatList;

    MyChatAdapter adt ;
    ArrayList<ChatMessage> messages = new ArrayList<>();


    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HmHmss", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());
    Button send;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chatLayout = inflater.inflate(R.layout.chatlayout,container,false);

        MyOpenHelper opener = new MyOpenHelper(getContext());
        // open a database
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceieve = results.getInt(sendCol);
            messages.add(new ChatMessage(message, sendOrReceieve, time, id));
        }
        chatList = chatLayout.findViewById(R.id.myrecycler);
        EditText messageTyped = chatLayout.findViewById(R.id.messageEdit);
        send =chatLayout.findViewById(R.id.sendButton);
        Button receive = chatLayout.findViewById(R.id.receiveButton);

        //adapter
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        chatList.setLayoutManager(layoutManager);

        send.setOnClickListener(clk ->{
          ChatMessage thisMessage = new ChatMessage(messageTyped.getText().toString(),1,sdf.format(new Date()) );


            // insert into database
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);


            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            messageTyped.setText("");
        });
        receive.setOnClickListener(clk ->{
            ChatMessage thisMessage = new ChatMessage(messageTyped.getText().toString(),2,sdf.format(new Date()) );



            // insert into database
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            messageTyped.setText("");
        });

        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete the message:" +chosenMessage.getMessage());
        builder.setTitle("Question:");

        builder.setPositiveButton("Yes",(dialog,cl) ->{
           // position = getAbsoluteAdapterPosition();
            ChatMessage removedMessage = messages.get(chosenPosition);

            messages.remove(chosenPosition);
            adt.notifyItemRemoved(chosenPosition);
            Snackbar.make(send, "You deleted message #" +chosenPosition,
                    Snackbar.LENGTH_LONG).setAction("Undo",click ->{
                messages.add(chosenPosition,removedMessage);
                adt.notifyItemInserted(chosenPosition);
            }).show();

        })
//                .setNegativeButton("No",(dialog, cl)->{
//
//                })
                .create().show();
    }

    public class MyRowViews extends RecyclerView.ViewHolder{
    TextView messageText ;
    TextView timeText ;
   // MyChatAdapter adt ;
    int position ;
    public MyRowViews( View itemView) {
        super(itemView);

        position = -1;

        itemView.setOnClickListener(clk ->{

            ChatRoom parentActivity = (ChatRoom)getContext();
            int positon = getAbsoluteAdapterPosition();
            parentActivity.userClickMessage(messages.get(positon),positon);

        });
        messageText = itemView.findViewById(R.id.message);
        timeText = itemView.findViewById(R.id.time);
    }
    public void setPosition(int p){position = p;}
}

public   class MyChatAdapter extends  RecyclerView.Adapter<MyRowViews> {

    @Override
    public MyRowViews onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();


        int layoutID;
        if(viewType == 1)//send
            layoutID = R.layout.sent_message;
        else//Receive
            layoutID = R.layout.receive_message;

        View loadedRow = inflater.inflate(layoutID,parent,false);
        MyRowViews initRow = new MyRowViews(loadedRow);
        return initRow;
    }

    @Override
    public void onBindViewHolder( MyRowViews holder, int position) {

        MyRowViews thisRowLayout = (MyRowViews)holder;
        thisRowLayout.messageText.setText(messages.get(position).getMessage());
        thisRowLayout.timeText.setText(currentDateandTime);
        thisRowLayout.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage thisRow = messages.get(position);
        return thisRow.sendOrReceive;
    }
}

class ChatMessage{
    String message;
    int sendOrReceive;
    String timeSent;
long id;

    public ChatMessage(String message, int sendOrReceive, String timeSent) {
        this.message = message;
        this.sendOrReceive = sendOrReceive;
        this.timeSent = timeSent;

    }

    public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
        this.message = message;
        this.sendOrReceive = sendOrReceive;
        this.timeSent = timeSent;
setId(id);
    }
    public String getMessage() { return message; }

    public int getSendOrReceive() { return sendOrReceive; }

    public String getTimeSent() { return timeSent; }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

}