package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestDetails;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestRecommendedProducts;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductDetail;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;
import com.procialize.bayer2020.ui.catalogue.model.Product_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_item;
import com.procialize.bayer2020.ui.catalogue.model.product_dosage_detail;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class ProductListDetailActivity extends AppCompatActivity {

    private APIService eventApi;
    private FragmentTabHost mTabHostCel;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    String token, eventid, imageurl, productId = "1";
    List<product_subpoint_detail> product_subpoint_detailList = new ArrayList<>();
    List<Product_document_detail> Product_document_detailList = new ArrayList<>();
    List<product_dosage_detail> product_dosage_detailList = new ArrayList<>();

    LinearLayout linMain;
    ImageView imgCover;
    TextView productTitle;
    Product_item product_item;
    LinearLayout linCalc,linShare, linBuyNow;
    Button imgCalc;

    private ConnectionDetector cd;
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
        product_item = (Product_item) getIntent().getSerializableExtra("Product");
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



       /* mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab1")
                        .setIndicator(createTabView(this, "Details")),
                ProductFragment.class, null);*/
       /* mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab2")
                        .setIndicator(createTabView(this, "Downloads")),
                ProductListDetailActivity.class, null);*/
                //PestFragment.class, null);



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
                            RefreashToken refreashToken = new RefreashToken(ProductListDetailActivity.this);
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
                                bb.putSerializable("ProductType", (Serializable) product_item);
                                bb.putString("DocumentPath", DocumentPath);

                                bb.putSerializable("productSubPoint", (Serializable) product_subpoint_detailList);
                                mTabHostCel.addTab(
                                        mTabHostCel.newTabSpec("Tab1")
                                                .setIndicator(createTabView(ProductListDetailActivity.this, "Details")),
                                        ProductSubPointFragment.class, bb);

                                Bundle b = new Bundle();
                                b.putSerializable("ProductType", (Serializable) product_item);
                                b.putString("DocumentPath", DocumentPath);
                                b.putSerializable("productDocumentList", (Serializable) Product_document_detailList);
                                mTabHostCel.addTab(
                                        mTabHostCel.newTabSpec("Tab2")
                                                .setIndicator(createTabView(ProductListDetailActivity.this, "Downloads")),
                                        ProductDocumentFragment.class, b);


                                linCalc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(product_dosage_detailList.size()>0) {
                                            startActivity(new Intent(ProductListDetailActivity.this, ProductmCalculator_Activity.class)
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

                                            startActivity(new Intent(ProductListDetailActivity.this, ProductmCalculator_Activity.class)
                                                    .putExtra("ProductDosage", (Serializable) product_dosage_detailList)
                                                    .putExtra("ProductName", product_item.getProduct_name())
                                            );
                                        }else{
                                            Utility.createShortSnackBar(linMain, "Data not available");

                                        }
                                    }
                                });

                                productTitle.setText(product_item.getProduct_name());
                                Glide.with(ProductListDetailActivity.this)
                                        .load(eventLists.getProduct_imagepath()+product_item.getProduct_image())
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(imgCover);
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

            String eventLogo = SharedPreference.getPref(ProductListDetailActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ProductListDetailActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ProductListDetailActivity.this)
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

}
