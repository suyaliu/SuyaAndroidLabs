package algonquin.cst2335.id040974880;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity{
    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt = new MyChatAdapter();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HmHmss", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());
    SQLiteDatabase db;

    @Override
        protected void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chatlayout);

            EditText messageTyped = findViewById(R.id.messageEdit);
            Button send = findViewById(R.id.sendButton);
            Button receive = findViewById(R.id.receiveButton);

        MyOpenHelper opener = new MyOpenHelper(this);
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


            chatList = findViewById(R.id.myrecycler);
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            chatList.setLayoutManager(layoutManager);



        send.setOnClickListener(clk ->{


            ChatMessage cm = new ChatMessage(messageTyped.getText().toString(),1,currentDateandTime);
           // insert into database
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,cm.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message, newRow);
            cm.setId(newId);
            messages.add(cm);

            messageTyped.setText("");
            //tell the list there is a new item the end of the arraylist
            adt.notifyItemInserted(messages.size()-1);
        });
        receive.setOnClickListener(clk ->{
            ChatMessage cm= new ChatMessage(messageTyped.getText().toString(),
                    2, currentDateandTime );
            // insert into database
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive,cm.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());

            //now insert
            long newId = db.insert(MyOpenHelper.TABLE_NAME,MyOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            messageTyped.setText("");
            adt.notifyItemInserted(messages.size()-1);

        });

    }
        private class MyRowViews extends RecyclerView.ViewHolder{
            TextView messageText ;
            TextView timeText ;
            MyChatAdapter adt ;
            int position=-1 ;
        public MyRowViews( View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" +messageText.getText());
                builder.setTitle("Question:");

                builder.setPositiveButton("Yes",(dialog,cl) ->{
                    position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(position);

                    //deleter from database
                    messages.remove(position);
                    adt.notifyItemRemoved(position);
                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});

                    Snackbar.make(messageText, "You deleted message #" +position,
                            Snackbar.LENGTH_LONG).setAction("Undo",click ->{
                                messages.add(position,removedMessage);
                                adt.notifyItemInserted(position);

                                db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                "','" + removedMessage.getMessage() +
                                "','" + removedMessage.getSendOrReceive() +
                                "','" + removedMessage.getTimeSent() + "');");

                    })
                            .show();
                })
                        .setNegativeButton("No",(dialog, cl)->{

                        })
                        .create().show();



            });

        }
        public void setPosition(int p){position = p;}
    }

        private  class MyChatAdapter extends  RecyclerView.Adapter<MyRowViews> {

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

    private class ChatMessage{
        String message;
        int sendOrReceive;
        String timeSent;
        long id;
        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;

        }
        public ChatMessage(String message, int sendOrReceive, String timeSent,long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            setId(id);
        }
        public String getMessage() { return message; }

        public int getSendOrReceive() { return sendOrReceive; }

        public String getTimeSent() { return timeSent; }
        public void setId(Long l){ id = l; }

        public long getId(){ return id; }
    }
}
