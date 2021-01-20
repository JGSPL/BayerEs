package com.procialize.bayer2020.ui.document.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.document.adapter.DocumentGridAdapter;
import com.procialize.bayer2020.ui.document.adapter.DocumentListAdapter;
import com.procialize.bayer2020.ui.document.model.Document;
import com.procialize.bayer2020.ui.document.model.DocumentDetail;
import com.procialize.bayer2020.ui.document.viewmodel.DocumentViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class DocumentActivity extends AppCompatActivity {

    SwipeRefreshLayout documentrefresh;
    RecyclerView recycler_document;
    TextView txt_pullrefresh, tv_header;
    ImageView iv_back;
    ConstraintLayout constraint_main;
    SessionManager session;
    String api_token = "", event_id;
    ConnectionDetector cd;
    DocumentViewModel documentviewmodel;
    public static List<DocumentDetail> gsonevent = new ArrayList<>();
    DocumentGridAdapter documentAdapter;
    DocumentListAdapter documentAdapterlist;
    ImageView image_list, image_grid;
    int color, color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        new RefreashToken(DocumentActivity.this).callGetRefreashToken(DocumentActivity.this);

        documentrefresh = findViewById(R.id.documentrefresh);
        recycler_document = findViewById(R.id.recycler_document);
        txt_pullrefresh = findViewById(R.id.txt_pullrefresh);
        tv_header = findViewById(R.id.tv_header);
        iv_back = findViewById(R.id.iv_back);
        constraint_main = findViewById(R.id.constraint_main);
        image_grid = findViewById(R.id.image_grid);
        image_list = findViewById(R.id.image_list);

        CommonFunction.showBackgroundImage(DocumentActivity.this, constraint_main);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_1)));
        color = Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_1));
        color2 = Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_2));

        image_grid.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        image_list.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
        session = new SessionManager(getApplicationContext());


        api_token = SharedPreference.getPref(DocumentActivity.this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(DocumentActivity.this, EVENT_ID);

        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CommonFirebase.crashlytics("DocumentListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "DocumentListing", api_token);

        cd = ConnectionDetector.getInstance(this);
        documentviewmodel = ViewModelProviders.of(this).get(DocumentViewModel.class);



        if (cd.isConnectingToInternet()) {
            documentviewmodel.getDocumentList(api_token, event_id, "100", "1");
            documentviewmodel.getDocumentList().observe(this, new Observer<Document>() {
                @Override
                public void onChanged(Document event) {
                    gsonevent.clear();
                    RefreashToken refreashToken = new RefreashToken(DocumentActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(decrypteventdetail);
                    Gson gson = new Gson();
                    gsonevent = gson.fromJson(je, new TypeToken<ArrayList<DocumentDetail>>() {
                    }.getType());
                    setAdapter(gsonevent);
                }
            });
        } else {
            Utility.createShortSnackBar(constraint_main, "No Internet Connection");
        }

        documentrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                documentrefresh.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    documentviewmodel.getDocumentList(api_token, event_id, "100", "1");
                    documentviewmodel.getDocumentList().observe(DocumentActivity.this, new Observer<Document>() {
                        @Override
                        public void onChanged(Document event) {

                            gsonevent.clear();
                            RefreashToken refreashToken = new RefreashToken(DocumentActivity.this);
                            String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                            JsonParser jp = new JsonParser();
                            JsonElement je = jp.parse(decrypteventdetail);
                            Gson gson = new Gson();
                            gsonevent = gson.fromJson(je, new TypeToken<ArrayList<DocumentDetail>>() {
                            }.getType());
                            setAdapter(gsonevent);
                        }
                    });
                } else {
                   Utility.createShortSnackBar(constraint_main, "No Internet Connection");
                }
            }
        });

        image_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_grid.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                image_list.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
                setAdapter(gsonevent);
            }
        });

        image_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_list.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                image_grid.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
                setAdapterlist(gsonevent);
            }
        });
    }

    public void setAdapter(List<DocumentDetail> commentList) {
        documentAdapter = new DocumentGridAdapter(DocumentActivity.this, commentList);
        recycler_document.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_document.setAdapter(documentAdapter);
    }

    public void setAdapterlist(List<DocumentDetail> commentList) {

        documentAdapterlist = new DocumentListAdapter(DocumentActivity.this, commentList);
        recycler_document.setLayoutManager(new LinearLayoutManager(this));
        recycler_document.setAdapter(documentAdapterlist);
    }


}
