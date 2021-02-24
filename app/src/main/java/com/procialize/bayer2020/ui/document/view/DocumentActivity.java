package com.procialize.bayer2020.ui.document.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.document.adapter.DocumentGridAdapter;
import com.procialize.bayer2020.ui.document.adapter.DocumentListAdapter;
import com.procialize.bayer2020.ui.document.model.Document;
import com.procialize.bayer2020.ui.document.model.DocumentDetail;
import com.procialize.bayer2020.ui.document.viewmodel.DocumentViewModel;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.DOCUMENT_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;

public class DocumentActivity extends AppCompatActivity implements DocumentListAdapter.DocumentListAdapterListner {

    SwipeRefreshLayout documentrefresh;
    RecyclerView recycler_document;
    TextView txt_pullrefresh, tv_header;
    ImageView iv_back,headerlogoIv;
    ConstraintLayout constraint_main;
    SessionManager session;
    String api_token = "", event_id;
    ConnectionDetector cd;
    DocumentViewModel documentviewmodel;
    public static List<DocumentDetail> gsonevent = new ArrayList<>();
    DocumentGridAdapter documentAdapter;
    DocumentListAdapter documentAdapterlist;

    //ImageView image_list, image_grid;
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
      /*  image_grid = findViewById(R.id.image_grid);
        image_list = findViewById(R.id.image_list);
*/
        //CommonFunction.showBackgroundImage(DocumentActivity.this, constraint_main);
        /*tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_1)));
        color = Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_1));
        color2 = Color.parseColor(SharedPreference.getPref(DocumentActivity.this, EVENT_COLOR_2));

        image_grid.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        image_list.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);*/
        session = new SessionManager(getApplicationContext());


        api_token = SharedPreference.getPref(DocumentActivity.this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(DocumentActivity.this, EVENT_ID);

        /*iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);*/
       /* iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);

        String eventLogo = SharedPreference.getPref(this, EVENT_LOGO);
        String eventListMediaPath = SharedPreference.getPref(this, EVENT_LIST_MEDIA_PATH);
        Glide.with(this)
                .load(eventListMediaPath + eventLogo)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(headerlogoIv);

        CommonFirebase.crashlytics("DocumentListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "DocumentListing", api_token);

        cd = ConnectionDetector.getInstance(this);
        documentviewmodel = ViewModelProviders.of(this).get(DocumentViewModel.class);

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);

            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DocumentActivity.this, NotificationActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------



        if (cd.isConnectingToInternet()) {
            documentviewmodel.getDocumentList(api_token, event_id, "100", "1");
            documentviewmodel.getDocumentList().observe(this, new Observer<Document>() {
                @Override
                public void onChanged(Document event) {
                    gsonevent.clear();
                    RefreashToken refreashToken = new RefreashToken(DocumentActivity.this);
                    if(event!=null) {
                        String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                        if(event.getDocument_path()!=null) {
                            String docPath = event.getDocument_path();

                            HashMap<String, String> map = new HashMap<>();
                            map.put(DOCUMENT_MEDIA_PATH, docPath);
                            SharedPreference.putPref(DocumentActivity.this, map);
                        }


                        JsonParser jp = new JsonParser();
                        JsonElement je = jp.parse(decrypteventdetail);
                        Gson gson = new Gson();
                        gsonevent = gson.fromJson(je, new TypeToken<ArrayList<DocumentDetail>>() {
                        }.getType());
                        setAdapterlist(gsonevent);
                    }
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
                            String docPath = event.getDocument_path();
                            HashMap<String, String> map = new HashMap<>();
                            map.put(DOCUMENT_MEDIA_PATH, docPath);
                            SharedPreference.putPref(DocumentActivity.this, map);

                            JsonParser jp = new JsonParser();
                            JsonElement je = jp.parse(decrypteventdetail);
                            Gson gson = new Gson();
                            gsonevent = gson.fromJson(je, new TypeToken<ArrayList<DocumentDetail>>() {
                            }.getType());
                            setAdapterlist(gsonevent);
                        }
                    });
                } else {
                   Utility.createShortSnackBar(constraint_main, "No Internet Connection");
                }
            }
        });

       /* image_grid.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

/*
    public void setAdapter(List<DocumentDetail> commentList) {
        documentAdapter = new DocumentGridAdapter(DocumentActivity.this, commentList);
        recycler_document.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_document.setAdapter(documentAdapter);
    }
*/

    public void setAdapterlist(List<DocumentDetail> commentList) {

        documentAdapterlist = new DocumentListAdapter(DocumentActivity.this, commentList,DocumentActivity.this);
        recycler_document.setLayoutManager(new LinearLayoutManager(this));
        recycler_document.setAdapter(documentAdapterlist);
    }


    @Override
    public void onMoreSelected(DocumentDetail event, int position) {
        String path = SharedPreference.getPref(DocumentActivity.this, DOCUMENT_MEDIA_PATH);
        new DownloadFile().execute(path + event.getDocument_file_name());

        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                event.getDoc_id(),
                "downloads_document_download",
                "downloads",
                "8");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------

    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(DocumentActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "BayerEs/Documents/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("error", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();

//            sharePdf(folder + fileName, PdfViewerActivity.this);
        }
    }
}
