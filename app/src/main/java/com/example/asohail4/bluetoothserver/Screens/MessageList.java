package com.example.asohail4.bluetoothserver.Screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asohail4.bluetoothserver.Adapters.MessageListAdapter;
import com.example.asohail4.bluetoothserver.MainActivity;
import com.example.asohail4.bluetoothserver.Models.Message;
import com.example.asohail4.bluetoothserver.R;

import java.util.ArrayList;
import java.util.List;

public class MessageList extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> messageList;
    private ImageButton btn;
    private EditText editText;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        messageList = new ArrayList<>();
        initRecyclerView();
        addWelcomeMessage();
        btn = findViewById(R.id.btnSend);
        editText = findViewById(R.id.txtChatInput);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }
    private void addWelcomeMessage() {
        String welcomeString = getString(R.string.bot_hello);
        Message message = new Message(welcomeString, MessageListAdapter.VIEWTYPE_BOT);
        mMessageAdapter.addItem(message);
    }

    private void initRecyclerView(){
        // use a linear layout manager
        mMessageRecycler = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(mLayoutManager);

        // specify an adapter
        mMessageAdapter = new MessageListAdapter(messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
    }


    private void sendMessage(){
        String text = editText.getText().toString();
        if(text != null && text != "" && text.length() > 0){
            mMessageAdapter.addItem(new Message(text, MessageListAdapter.VIEWTYPE_USER));
            MainActivity.sendMessage(text);
            mMessageRecycler.scrollToPosition(messageList.size() - 1);
            editText.setText("");
        }

    }


}