package com.procialize.bayer2020.ui.upskill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.agenda.model.Agenda;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.model.UpSkill;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpskillDetailsFirstActivity extends AppCompatActivity {

    TextView tv_title,tv_description;
            ImageView iv_banner;
    UpskillList upskillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_first);

        tv_title = findViewById(R.id.tv_title);
        iv_banner = findViewById(R.id.iv_banner);
        tv_description = findViewById(R.id.tv_description);

        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");

        tv_description.setText(upskillList.getDescription());
        tv_title.setText(upskillList.getName());

        Glide.with(this)
                .load(upskillList.getCover_img())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(iv_banner);
    }


}