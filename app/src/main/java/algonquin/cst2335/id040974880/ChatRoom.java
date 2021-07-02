package algonquin.cst2335.id040974880;
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
    @Override
        protected void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chatlayout);

            EditText messageTyped = findViewById(R.id.messageEdit);
            Button send = findViewById(R.id.sendButton);
            Button receive = findViewById(R.id.receiveButton);



            chatList = findViewById(R.id.myrecycler);
            chatList.setAdapter(adt);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            chatList.setLayoutManager(layoutManager);



        send.setOnClickListener(clk ->{
            ChatMessage thisMessage = new ChatMessage(messageTyped.getText().toString(),1,new Date() );
            messages.add(thisMessage);
            messageTyped.setText("");

            //tell the list there is a new item the end of the arraylist
            adt.notifyItemInserted(messages.size()-1);
        });
        receive.setOnClickListener(clk ->{
            ChatMessage thisMessage = new ChatMessage(messageTyped.getText().toString(),
                    2, new Date() );
            messages.add(thisMessage);
            messageTyped.setText("");
            adt.notifyItemInserted(messages.size()-1);

        });

    }
        private class MyRowViews extends RecyclerView.ViewHolder{
            TextView messageText ;
            TextView timeText ;
            MyChatAdapter adt ;
            int position ;
        public MyRowViews( View itemView) {
            super(itemView);

            position = -1;

            itemView.setOnClickListener(clk ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" +messageText.getText());
                builder.setTitle("Question:");

                builder.setPositiveButton("Yes",(dialog,cl) ->{
                    position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(position);

                    messages.remove(position);
                    adt.notifyItemRemoved(position);
                    Snackbar.make(messageText, "You deleted message #" +position,
                            Snackbar.LENGTH_LONG).setAction("Undo",click ->{
                                messages.add(position,removedMessage);
                                adt.notifyItemInserted(position);
                    })
                            .show();
                })
                        .setNegativeButton("No",(dialog, cl)->{

                        })
                        .create().show();



            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
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
        Date timeSent;

        public ChatMessage(String message, int sendOrReceive, Date timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }
        public String getMessage() { return message; }

        public int getSendOrReceive() { return sendOrReceive; }

        public Date getTimeSent() { return timeSent; }

    }
}
