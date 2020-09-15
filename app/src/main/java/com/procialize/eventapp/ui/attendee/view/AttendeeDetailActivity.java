package com.procialize.eventapp.ui.attendee.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.procialize.eventapp.R;

public class AttendeeDetailActivity extends AppCompatActivity {
    String fname, lname, company, city, designation, prof_pic, attendee_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);

        getIntentData();
    }

    public void getIntentData() {
        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        company = intent.getStringExtra("company");
        city = intent.getStringExtra("city");
        designation = intent.getStringExtra("designation");
        prof_pic = intent.getStringExtra("prof_pic");
        attendee_type = intent.getStringExtra("attendee_type");
    }
}