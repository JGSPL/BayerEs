package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestRecommendedProducts;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductDetail;
import com.procialize.bayer2020.ui.catalogue.model.Product_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_item;
import com.procialize.bayer2020.ui.catalogue.model.product_dosage_detail;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;
import com.procialize.bayer2020.ui.loyalityleap.view.PurchaseHistoryActivity;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;
import com.procialize.bayer2020.ui.storelocator.view.StoreLocatorActivity;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class RecommendedproductListDetailActivity extends AppCompatActivity {

    private APIService eventApi;
    private FragmentTabHost mTabHostCel;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    String token, eventid, imageurl, productId = "1", Imageurl;
    List<product_subpoint_detail> product_subpoint_detailList = new ArrayList<>();
    List<Product_document_detail> Product_document_detailList = new ArrayList<>();
    List<product_dosage_detail> product_dosage_detailList = new ArrayList<>();

    LinearLayout linMain;
    ImageView imgCover;
    TextView productTitle;
    CataloguePestRecommendedProducts product_item;
    LinearLayout linCalc,linShare, linBuyNow;
    Button imgCalc,btnbuy,btnShare;
    private Uri dynamicLink = null;
    private static final String TAG = "DynamicLinks";
    private ConnectionDetector cd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist_detail);

        setUpToolbar();
        cd = ConnectionDetector.getInstance(this);
        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mTabHostCel = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHostCel.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        product_item = (CataloguePestRecommendedProducts) getIntent().getSerializableExtra("Product");
        Imageurl = getIntent().getStringExtra("Imageurl");;
        productId = product_item.getId();

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linMain = findViewById(R.id.linMain);
        imgCover = findViewById(R.id.imgCover);
        productTitle= findViewById(R.id.productTitle);
        linCalc = findViewById(R.id.linCalc);
        linShare = findViewById(R.id.linShare);
        linBuyNow = findViewById(R.id.linBuyNow);
        imgCalc = findViewById(R.id.imgCalc);
        btnbuy = findViewById(R.id.btnbuy);
        btnShare = findViewById(R.id.btnShare);
        progressBar = findViewById(R.id.progressBar);

//-----------------------------For Notification count-----------------------------
        /*try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);

            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RecommendedproductListDetailActivity.this, NotificationActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------

        productTitle.setText(product_item.getProduct_name());
        Glide.with(RecommendedproductListDetailActivity.this)
                .load(Imageurl+product_item.getProduct_image())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).into(imgCover);
       /* mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab1")
                        .setIndicator(createTabView(this, "Details")),
                ProductFragment.class, null);*/
       /* mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab2")
                        .setIndicator(createTabView(this, "Downloads")),
                RecommendedproductListDetailActivity.class, null);*/
        //PestFragment.class, null);
        getLink();

        linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLink();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLink();
            }
        });

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendedproductListDetailActivity.this, StoreLocatorActivity.class)
                );
            }
        });
        linBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecommendedproductListDetailActivity.this, StoreLocatorActivity.class)
                );
            }
        });

        if (cd.isConnectingToInternet()) {
            getDataFromApi(token, eventid);
        }else {
            Utility.createShortSnackBar(linMain, "No internet connection");
        }


    }

    public void getDataFromApi(String token, String eventid) {

        eventApi = ApiUtils.getAPIService();

        eventApi.ProductDetails(token, "1", productId)
                .enqueue(new Callback<FetchProductDetail>() {
                    @Override
                    public void onResponse(Call<FetchProductDetail> call, Response<FetchProductDetail> response) {
                        if (response.isSuccessful()) {

                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(RecommendedproductListDetailActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            Product_detail eventLists = gson.fromJson(data, new TypeToken<Product_detail>() {
                            }.getType());
                            // List<Product_detail> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Product_detail>>() {}.getType());

                            //Fetch Livepoll list
                            if (eventLists != null) {
                                product_subpoint_detailList = eventLists.getProduct_subpoints_detail();
                                Product_document_detailList = eventLists.getProductDocumentList();
                                product_dosage_detailList = eventLists.getProduct_dosage_detailList();

                                String DocumentPath = eventLists.getProduct_documentpath();
                                Bundle bb = new Bundle();
                                //bb.putSerializable("ProductType", (Serializable) product_item);
                                bb.putString("DocumentPath", DocumentPath);
                                bb.putSerializable("productDescription", product_item.getProduct_long_description());

                                bb.putSerializable("productSubPoint", (Serializable) product_subpoint_detailList);
                                mTabHostCel.addTab(
                                        mTabHostCel.newTabSpec("Tab1")
                                                .setIndicator(createTabView(RecommendedproductListDetailActivity.this, "Details")),
                                        ProductSubPointFragment.class, bb);

                                Bundle b = new Bundle();
                               // b.putSerializable("ProductType", (Serializable) product_item);
                                b.putString("DocumentPath", DocumentPath);
                                b.putSerializable("productDocumentList", (Serializable) Product_document_detailList);
                                mTabHostCel.addTab(
                                        mTabHostCel.newTabSpec("Tab2")
                                                .setIndicator(createTabView(RecommendedproductListDetailActivity.this, "Downloads")),
                                        ProductDocumentFragment.class, b);


                                linCalc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(product_dosage_detailList.size()>0) {
                                            startActivity(new Intent(RecommendedproductListDetailActivity.this, ProductmCalculator_Activity.class)
                                                    .putExtra("ProductDosage", (Serializable) product_dosage_detailList)
                                                    .putExtra("ProductName", product_item.getProduct_name())
                                            );
                                        }else{
                                            Utility.createShortSnackBar(linMain, "Data not available");

                                        }

                                    }
                                });
                                imgCalc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(product_dosage_detailList.size()>0) {

                                            startActivity(new Intent(RecommendedproductListDetailActivity.this, ProductmCalculator_Activity.class)
                                                    .putExtra("ProductDosage", (Serializable) product_dosage_detailList)
                                                    .putExtra("ProductName", product_item.getProduct_name())
                                            );
                                        }else{
                                            Utility.createShortSnackBar(linMain, "Data not available");

                                        }
                                    }
                                });



                            } else {
                                Utility.createShortSnackBar(linMain, "Failure22");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchProductDetail> call, Throwable t) {
                        Utility.createShortSnackBar(linMain, "Failure");
                    }
                });
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalogue_tab_bg, null);

        LinearLayout linTab = view.findViewById(R.id.linTab);
        //  linTab.setBackgroundColor(Color.parseColor(colorActive));
        //  linTab.setBackground(R.drawable.a);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);


        return view;
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
           /* setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
            headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(RecommendedproductListDetailActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(RecommendedproductListDetailActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(RecommendedproductListDetailActivity.this)
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

        }
    }

    public void getLink() {
        String appCode = "524195205462";
        final Uri deepLink = Uri.parse("http://example.com/promo?discount");

        String packageName = getApplicationContext().getPackageName();

        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode + ".app.goo.gl")
                .path("/")
                .appendQueryParameter("link", deepLink.toString())
                .appendQueryParameter("apn", packageName);

        dynamicLink = builder.build();
        linShare.setEnabled(true);
    }
    public void shareLink() {
        try {
           /* URL url = new URL(URLDecoder.decode(dynamicLink.toString(),
                    "UTF-8"));*/

            URL url = new URL(URLDecoder.decode("https://bayer2020.page.link/newsfeed",
                    "UTF-8"));
            Log.i(TAG, "URL = " + url.toString());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link");
            intent.putExtra(Intent.EXTRA_TEXT, url.toString());
            startActivity(intent);
        } catch (Exception e) {
            Log.i(TAG, "Could not decode Uri: " + e.getLocalizedMessage());
        }
    }
}
