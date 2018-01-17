package com.emma.flashchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        //Set up the display name and get the Firebase reference
        setupDisplayName();
            //the database reference is used for reading and writing data to that location in cloud database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();



        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        //Two methods of sending message
        // 1. Send the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });
        // 2. Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    //Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);
        if (mDisplayName == null) {
            mDisplayName = "Anonymous";
        }
    }


    private void sendMessage() {
        //Grab the text the user typed in and push the message to Firebase
        Log.d("FlashChat", "The user sent something");
        String input = mInputText.getText().toString();
        if (!input.equals("")) {
            InstantMessage chat = new InstantMessage(input, mDisplayName);
            //finally store the InstantMessage object into cloud database
                //the child is the location where the data is saved
            mDatabaseReference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }

    // Override the onStart() lifecycle method. Setup the adapter here.
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        // let view know which adapter it should talk to
        mChatListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter. (Defined in ChatListAdapter.java)
        mAdapter.cleanup();

    }

}
