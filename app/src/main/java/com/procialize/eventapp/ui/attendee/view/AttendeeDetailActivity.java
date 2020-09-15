package com.procialize.eventapp.ui.attendee.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.procialize.eventapp.R;

public class AttendeeDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);


    }

    public void getIntentData(){
        Intent intent=getIntent();

    }
}