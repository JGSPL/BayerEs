package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.PestTypeItem;
import com.procialize.bayer2020.ui.catalogue.model.product_dosage_detail;
import com.procialize.bayer2020.ui.loyalityleap.view.MPointActivity;

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
    TextView tvProductTitle ,txt_quan, txt_quantity, txt_quantitySolu, txtAmountConvert;
    EditText txt_area, edtAmountConvert;
    String high;
    Spinner spinner, spinnersqare, spinnersqare2;
    String conversionValue;
    LinearLayout linear;

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
        linear = findViewById(R.id.linear);

        tvProductTitle = findViewById(R.id.tvProductTitle);
        txt_area = findViewById(R.id.txt_area);
        spinner = findViewById(R.id.spinner);
        txt_quan = findViewById(R.id.txt_quan);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_quantitySolu = findViewById(R.id.txt_quantitySolu);
        txtAmountConvert = findViewById(R.id.txtAmountConvert);
        edtAmountConvert = findViewById(R.id.edtAmountConvert);

        spinnersqare = findViewById(R.id.spinnersqare);
        spinnersqare2 = findViewById(R.id.spinnersqare2);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.amount_array1, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnersqare.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.amount_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnersqare2.setAdapter(adapter3);

        // Class Spinner implementing onItemSelectedListener
        spinnersqare2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // do something upon option selection
                conversionValue = parent.getItemAtPosition(position).toString();
                // if(txt_area.getText().toString().isEmpty()) {
                if (conversionValue.equalsIgnoreCase("Square feet")) {
                    try {
                        String value = edtAmountConvert.getText().toString();
                        // Double fvalue = Float.parseFloat(value)*10.7;
                        Double fvalue = Double.parseDouble(value);

                        txtAmountConvert.setText(String.valueOf(fvalue));
                    }catch (Exception e){

                    }

                } else if (conversionValue.equalsIgnoreCase("Square meter")) {
                    String value = edtAmountConvert.getText().toString();
                    Double fvalue = Float.parseFloat(value)/10.7;
                    txtAmountConvert.setText(String.valueOf(fvalue));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });



        if(product_dosage_detailList!=null){
            if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("0")){
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.any_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
            }else{
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.high_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
            }
            tvProductTitle.setText(productName);
            txt_quan.setText(product_dosage_detailList.get(0).getAmount_unit());
            txt_quantity.setText(product_dosage_detailList.get(0).getAmount_unit());
            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_solution_quantity());

            txt_area.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                        try {

                            if (txt_area.getText().toString().isEmpty()) {
                                Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {

                                String txt_score = txt_area.getText().toString().trim();
                                String txt_total = product_dosage_detailList.get(0).getDiluted_solution_quantity();

                                if (txt_score.equalsIgnoreCase("")) {
                                    Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                } else {
                                    Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total);
                                    String s1 = String.valueOf(mPointtotal);
                                    String res = s1.replace(".", "");
                                    txt_quantity.setText(res);
                                }
                                //txt_area.setFocusable(false);
                               // txt_area.setEnabled(false);
                                return true;

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            });

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
            Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getHigh_amount());
            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

            Float quantity = amountReq/contentProduct;
            txt_quan.setText(String.valueOf(quantity));


        }else if(high.equalsIgnoreCase("Low")){
            Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getLow_amount());
            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

            Float quantity = amountReq/contentProduct;
            txt_quan.setText(String.valueOf(quantity));
        }else if(high.equalsIgnoreCase("any")){
            Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getAny_amount());
            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

            Float quantity = amountReq/contentProduct;
            txt_quan.setText(String.valueOf(quantity));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
