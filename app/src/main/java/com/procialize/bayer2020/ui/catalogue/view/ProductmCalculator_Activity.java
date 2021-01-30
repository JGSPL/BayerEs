package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.catalogue.model.PestTypeItem;
import com.procialize.bayer2020.ui.catalogue.model.product_dosage_detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class ProductmCalculator_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    Toolbar mToolbar;
    ImageView headerlogoIv;
    String token, eventid, docurl, productId = "1",productName;
    List<product_dosage_detail> product_dosage_detailList = new ArrayList<>();
    TextView tvProductTitle, txt_area,txt_quan, txt_quantity, txt_quantitySolu;
    String high;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_calculator);

        setUpToolbar();
        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        product_dosage_detailList = (List<product_dosage_detail>) getIntent().getSerializableExtra("ProductDosage");
        productName = getIntent().getStringExtra("ProductName");

        tvProductTitle = findViewById(R.id.tvProductTitle);
        txt_area = findViewById(R.id.txt_area);
        spinner = findViewById(R.id.spinner);
        txt_quan = findViewById(R.id.txt_quan);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_quantitySolu = findViewById(R.id.txt_quantitySolu);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.high_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if(product_dosage_detailList!=null){
            tvProductTitle.setText(productName);
            txt_quan.setText(product_dosage_detailList.get(0).getAmount_unit());
            txt_quantity.setText(product_dosage_detailList.get(0).getAmount_unit());
            txt_quantitySolu.setText(product_dosage_detailList.get(0).getAmount_unit());

        }
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

            String eventLogo = SharedPreference.getPref(ProductmCalculator_Activity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ProductmCalculator_Activity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ProductmCalculator_Activity.this)
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

            mToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        high = parent.getItemAtPosition(position).toString();
        if(high.equalsIgnoreCase("High")){
            txt_area.setText(product_dosage_detailList.get(0).getHigh_deposition_rate());

        }else if(high.equalsIgnoreCase("Low")){
            txt_area.setText(product_dosage_detailList.get(0).getLow_deposition_rate());

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
