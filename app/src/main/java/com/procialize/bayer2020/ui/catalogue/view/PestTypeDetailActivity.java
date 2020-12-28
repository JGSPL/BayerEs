package com.procialize.bayer2020.ui.catalogue.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.livepoll.adapter.LivePollAdapter;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll_option;
import com.procialize.bayer2020.ui.livepoll.viewmodel.LivePollViewModel;

import java.util.List;

public class PestTypeDetailActivity extends AppCompatActivity {

    private APIService eventApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_polllist);
    }
}
