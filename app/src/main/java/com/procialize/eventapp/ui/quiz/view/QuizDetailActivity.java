package com.procialize.eventapp.ui.quiz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.procialize.eventapp.R;

public class QuizDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                count = 1;
//                pagerAdapter.selectopt = 0;
//                try {
//                    if (timercountdown != null) {
//                        timercountdown.cancel();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

               /* Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
                startActivity(intent);
                finish();*/
                onBackPressed();

            }
        });


    }
}