package com.procialize.eventapp.ui.newsFeedComment.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedComment.adapter.GifEmojiAdapter;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, GifEmojiAdapter.GifEmojiAdapterListner {

    private static final String API_KEY = "TVG20YJW1MXR";
    ImageView iv_gif,iv_back_gif;
    EditText et_comment,et_search_gif;
    FrameLayout fl_gif_container;
    LinearLayout ll_comment_container;
    CommentViewModel commentViewModel;
    String anon_id;
    RecyclerView rv_gif;
    ProgressBar pb_emoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        iv_gif = findViewById(R.id.iv_gif);
        rv_gif = findViewById(R.id.rv_gif);
        pb_emoji = findViewById(R.id.pb_emoji);
        iv_gif.setOnClickListener(this);
        et_comment = findViewById(R.id.et_comment);
        iv_back_gif = findViewById(R.id.iv_back_gif);
        et_search_gif = findViewById(R.id.et_search_gif);
        fl_gif_container = findViewById(R.id.fl_gif_container);
        ll_comment_container = findViewById(R.id.ll_comment_container);

        et_search_gif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    commentViewModel.searchGif(s.toString(), API_KEY, anon_id);
                    commentViewModel.searchGifList().observe(CommentActivity.this, new Observer<GifResponse>() {
                        @Override
                        public void onChanged(GifResponse listMutableLiveData) {
                            List<GifResult> listResult = listMutableLiveData.getResults();
                            setupGifAdapter(listResult);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gif:
                ll_comment_container.setVisibility(View.GONE);
                if (fl_gif_container.getVisibility() == View.GONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        et_comment.setTextColor(Color.parseColor("#0000"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ll_comment_container.setVisibility(View.GONE);
                    fl_gif_container.setVisibility(View.VISIBLE);

                    //Get Gif Id
                    commentViewModel.GetId(API_KEY);
                    commentViewModel.getAnonId().observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String result) {
                            anon_id = result;
                        }
                    });

                    //Get GIF images
                    commentViewModel.GetGif(API_KEY,anon_id);
                    commentViewModel.getGifList().observe(this, new Observer<GifResponse>() {
                        @Override
                        public void onChanged(GifResponse listMutableLiveData) {
                            List<GifResult> listResult = listMutableLiveData.getResults();
                            setupGifAdapter(listResult);
                        }
                    });
                } else {
                    ll_comment_container.setVisibility(View.VISIBLE);
                    fl_gif_container.setVisibility(View.GONE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void setupGifAdapter(List<GifResult> listResult)
    {
        pb_emoji.setVisibility(View.GONE);
        GifEmojiAdapter gifEmojiAdapter = new GifEmojiAdapter(CommentActivity.this, listResult, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_gif.setLayoutManager(horizontalLayoutManagaer);
        rv_gif.setAdapter(gifEmojiAdapter);
        gifEmojiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGifSelected(GifResult result) {
        ll_comment_container.setVisibility(View.VISIBLE);
        fl_gif_container.setVisibility(View.GONE);
    }
}