package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    TextView tvProductTitle ,txt_quan, txt_quantity, txt_quantitySolu, txtAmountConvert, tv_area, tv_QuantityWater,tv_QuantitySolu, tv_QuantityPro;
    EditText txt_area, edtAmountConvert;
    String high;
    Spinner spinner, spinnersqare, spinnersqare2;
    String conversionValue;
    LinearLayout linear;
   // String spinnerLevel[];
    List<String> spinnerLevel = new ArrayList<>();

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

        tv_QuantityWater = findViewById(R.id.tv_QuantityWater);
        tv_QuantitySolu = findViewById(R.id.tv_QuantitySolu);
        tv_QuantityPro = findViewById(R.id.tv_QuantityPro);
        tv_area = findViewById(R.id.tv_area);
        spinnersqare = findViewById(R.id.spinnersqare);
        spinnersqare2 = findViewById(R.id.spinnersqare2);

        /*if(productName.equalsIgnoreCase("Maxforce Forte") || productName.equalsIgnoreCase("Maxforce Quantum")){
           // tv_QuantityWater.setText("");
            tv_QuantityWater.setText("Number of Gel placements to be applied in infested area");
        }else if(productName.equalsIgnoreCase("Barcelo Granules")){
             tv_QuantityWater.setText("Amount of water to be treated (ml)");
        }else if(productName.contains("Kingfog")){
            tv_QuantityWater.setText("Amount of Diesel required (ml)");
        }*/
        if (product_dosage_detailList != null) {
            tv_QuantityWater.setText(product_dosage_detailList.get(0).getRequire_for_mixing_label());
            tv_QuantitySolu.setText(product_dosage_detailList.get(0).getInfestation_level_label());
            tv_QuantityPro.setText(product_dosage_detailList.get(0).getQuantity_of_product_label());


        }


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.amount_array1, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnersqare.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.amount_array,R.layout.spinner_item);
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


        try {
            edtAmountConvert.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    try {

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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(product_dosage_detailList!=null){
            if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("0")){
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.any_array, R.layout.spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
                high = "any";
            }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("1")){
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.high_array, R.layout.spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                high = "high";
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
            }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2")){
                if(!(product_dosage_detailList.get(0).getApp_type1_label().equalsIgnoreCase(""))){
                    spinnerLevel.add(product_dosage_detailList.get(0).getApp_type1_label());
                    high = product_dosage_detailList.get(0).getApp_type1_label();
                } if(!(product_dosage_detailList.get(0).getApp_type2_label().equalsIgnoreCase(""))){
                    spinnerLevel.add(product_dosage_detailList.get(0).getApp_type2_label());
                } if(!(product_dosage_detailList.get(0).getApp_type3_label().equalsIgnoreCase(""))){
                    spinnerLevel.add(product_dosage_detailList.get(0).getApp_type3_label());
                } if(!(product_dosage_detailList.get(0).getApp_type4_label().equalsIgnoreCase(""))){
                    spinnerLevel.add(product_dosage_detailList.get(0).getApp_type4_label());
                } if(!(product_dosage_detailList.get(0).getApp_type5_label().equalsIgnoreCase(""))){
                    spinnerLevel.add(product_dosage_detailList.get(0).getApp_type5_label());
                }
                // Create an ArrayAdapter using the string array and a default spinner layout
               /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        spinnerLevel, R.layout.spinner_item);*/
                ArrayAdapter dataAdapter = new ArrayAdapter(this, R.layout.spinner_item, spinnerLevel);

                // Specify the layout to use when the list of choices appears
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(this);
            }
            tvProductTitle.setText(productName);
            txt_quan.setText(product_dosage_detailList.get(0).getAmount_unit());
           // txt_quantity.setText(product_dosage_detailList.get(0).getAmount_unit());
            if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2")){
                if(!(product_dosage_detailList.get(0).getDiluted_quantity_1().equalsIgnoreCase(""))) {

                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_1());
                }
            }else {
                txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_solution_quantity());
            }
            tv_area.setText("Area To be treated (sq.mt)");

            edtAmountConvert.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                        try {

                            if (edtAmountConvert.getText().toString().isEmpty()) {
                                Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {

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


/*
            txt_area.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                        try {

                            if (txt_area.getText().toString().isEmpty()) {
                                Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                spinner.setOnItemSelectedListener(ProductmCalculator_Activity.this);
                                String txt_score = txt_area.getText().toString().trim();
                                String txt_total = product_dosage_detailList.get(0).getDiluted_solution_quantity();

                                if (txt_score.equalsIgnoreCase("")) {
                                    Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                } else {
                                    if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2")){
                                        if(product_dosage_detailList.get(0).getApp_type1_label().equalsIgnoreCase(high)){
                                            if(!(product_dosage_detailList.get(0).getDiluted_quantity_1().equalsIgnoreCase(""))) {

                                                String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_1();
                                                Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total2);
                                                String s1 = String.valueOf(mPointtotal);
                                                //String res = s1.replace(".", "");
                                                txt_quantity.setText(s1);
                                            }
                                        }else if(product_dosage_detailList.get(0).getApp_type2_label().equalsIgnoreCase(high)){
                                            if(!(product_dosage_detailList.get(0).getDiluted_quantity_2().equalsIgnoreCase(""))) {

                                                String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_2();
                                                Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total2);
                                                String s1 = String.valueOf(mPointtotal);
                                                //String res = s1.replace(".", "");
                                                txt_quantity.setText(s1);
                                            }

                                        }else if(product_dosage_detailList.get(0).getApp_type3_label().equalsIgnoreCase(high)){
                                            if(!(product_dosage_detailList.get(0).getDiluted_quantity_3().equalsIgnoreCase(""))) {

                                                String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_3();
                                                Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total2);
                                                String s1 = String.valueOf(mPointtotal);
                                                //String res = s1.replace(".", "");
                                                txt_quantity.setText(s1);
                                            }
                                        }else if(product_dosage_detailList.get(0).getApp_type4_label().equalsIgnoreCase(high)){
                                            if(!(product_dosage_detailList.get(0).getDiluted_quantity_4().equalsIgnoreCase(""))) {

                                                String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_4();
                                                Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total2);
                                                String s1 = String.valueOf(mPointtotal);
                                                //String res = s1.replace(".", "");
                                                txt_quantity.setText(s1);
                                            }
                                        }else if(product_dosage_detailList.get(0).getApp_type5_label().equalsIgnoreCase(high)){
                                            if(!(product_dosage_detailList.get(0).getDiluted_quantity_5().equalsIgnoreCase(""))) {
                                                String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_5();
                                                Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total2);
                                                String s1 = String.valueOf(mPointtotal);
                                                //String res = s1.replace(".", "");
                                                txt_quantity.setText(s1);
                                            }
                                        }
                                    }else{
                                        Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total);
                                        String s1 = String.valueOf(mPointtotal);
                                        //String res = s1.replace(".", "");
                                        txt_quantity.setText(s1);
                                    }



                                    Float area ;
                                    if(txt_area.getText().toString().isEmpty()){
                                        area = 0f;
                                    }else{
                                        area =  Float.parseFloat(txt_area.getText().toString());
                                    }
                                    if(high.equalsIgnoreCase("High")){
                                        // Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getHigh_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getHigh_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));


                                    }else if(high.equalsIgnoreCase("Low")){
                                        //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getLow_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getLow_deposition_rate());

                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));
                                    }else if(high.equalsIgnoreCase("any")){
                                        //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getAny_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getAny_deposition_rate());

                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));
                                    }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2")){
                                        if(product_dosage_detailList.get(0).getApp_type1_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type1_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type2_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type2_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type3_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type3_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type4_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type4_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type5_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type5_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }
                                    }

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
*/


            try {
                txt_area.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        try {

                                String txt_score = txt_area.getText().toString().trim();
                                String txt_total = product_dosage_detailList.get(0).getDiluted_solution_quantity();

                                if (txt_score.equalsIgnoreCase("")) {
                                    Toast.makeText(ProductmCalculator_Activity.this, "Please Enter Value", Toast.LENGTH_SHORT).show();
                                } else {

                                    try {
                                        Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(txt_total);
                                        String s1 = String.valueOf(mPointtotal);
                                        //String res = s1.replace(".", "");
                                        txt_quantity.setText(s1);
                                    }catch(Exception e){

                                    }

                                    Float area ;
                                    if(txt_area.getText().toString().isEmpty()){
                                        area = 0f;
                                    }else{
                                        area =  Float.parseFloat(txt_area.getText().toString());
                                    }
                                    if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("1") && high.equalsIgnoreCase("High")){

                                        // Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getHigh_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getHigh_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));


                                    }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("1") && high.equalsIgnoreCase("Low")){
                                        //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getLow_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getLow_deposition_rate());

                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));
                                    }else if( high.equalsIgnoreCase("any")){
                                        //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getAny_amount());
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getAny_deposition_rate());

                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));
                                    }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2") &&product_dosage_detailList.get(0).getApp_type1_label().equalsIgnoreCase(high)) {
                                        Float amountReq = area * Float.parseFloat(product_dosage_detailList.get(0).getApp_type1_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq / contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));

                                        try {
                                            Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(product_dosage_detailList.get(0).getDiluted_quantity_1());
                                            String s1 = String.valueOf(mPointtotal);
                                            //String res = s1.replace(".", "");
                                            txt_quantity.setText(s1);
                                            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_1());

                                        }catch(Exception e){

                                        }
                                    }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2") && product_dosage_detailList.get(0).getApp_type2_label().equalsIgnoreCase(high)) {
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type2_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));

                                        try {
                                            Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(product_dosage_detailList.get(0).getDiluted_quantity_2());
                                            String s1 = String.valueOf(mPointtotal);
                                            //String res = s1.replace(".", "");
                                            txt_quantity.setText(s1);
                                            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_2());
                                        }catch(Exception e){

                                        }

                                    }else if( product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2") && product_dosage_detailList.get(0).getApp_type3_label().equalsIgnoreCase(high)){
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type3_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));

                                        try {
                                            Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(product_dosage_detailList.get(0).getDiluted_quantity_3());
                                            String s1 = String.valueOf(mPointtotal);
                                            //String res = s1.replace(".", "");
                                            txt_quantity.setText(s1);
                                            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_3());
                                        }catch(Exception e){

                                        }

                                    }else if( product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2") && product_dosage_detailList.get(0).getApp_type4_label().equalsIgnoreCase(high)){
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type4_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));

                                        try {
                                            Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(product_dosage_detailList.get(0).getDiluted_quantity_4());
                                            String s1 = String.valueOf(mPointtotal);
                                            //String res = s1.replace(".", "");
                                            txt_quantity.setText(s1);
                                            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_4());
                                        }catch(Exception e){

                                        }

                                    }else if( product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2") && product_dosage_detailList.get(0).getApp_type5_label().equalsIgnoreCase(high)){
                                        Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type5_deposition_rate());
                                        Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                        Float quantity = amountReq/contentProduct;
                                        txt_quan.setText(String.valueOf(quantity));

                                        try {
                                            Double mPointtotal = Double.parseDouble(txt_score) * Double.parseDouble(product_dosage_detailList.get(0).getDiluted_quantity_5());
                                            String s1 = String.valueOf(mPointtotal);
                                            //String res = s1.replace(".", "");
                                            txt_quantity.setText(s1);
                                            txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_5());
                                        }catch(Exception e){

                                        }

                                    }

                                        /*if(product_dosage_detailList.get(0).getApp_type1_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type1_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type2_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type2_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type3_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type3_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type4_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type4_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }else if(product_dosage_detailList.get(0).getApp_type5_label().equalsIgnoreCase(high)){
                                            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type5_deposition_rate());
                                            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                                            Float quantity = amountReq/contentProduct;
                                            txt_quan.setText(String.valueOf(quantity));
                                        }*/

                                }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


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
        Float area ;
        Double areaDel;
        high = parent.getItemAtPosition(position).toString();
        if(txt_area.getText().toString().isEmpty()){
            area = 0f;
            areaDel = 0.0;
        }else{
           area =  Float.parseFloat(txt_area.getText().toString());
            areaDel = Double.parseDouble(txt_area.getText().toString());

        }
        if(high.equalsIgnoreCase("High") && product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("1")){
           // Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getHigh_amount());
            try{
            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getHigh_deposition_rate());
            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

            Float quantity = amountReq/contentProduct;
            txt_quan.setText(String.valueOf(quantity));
            }catch(Exception e){

            }


        }else if(high.equalsIgnoreCase("Low") && product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("1")){
            //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getLow_amount());
            try {
                Float amountReq = area * Float.parseFloat(product_dosage_detailList.get(0).getLow_deposition_rate());

                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq / contentProduct;
                txt_quan.setText(String.valueOf(quantity));
            }catch(Exception e){

            }
        }else if(high.equalsIgnoreCase("any") && product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("0")){
            //Float amountReq = Float.parseFloat(product_dosage_detailList.get(0).getAny_amount());
            try{
            Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getAny_deposition_rate());

            Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

            Float quantity = amountReq/contentProduct;
            txt_quan.setText(String.valueOf(quantity));
            }catch(Exception e){

            }
        }else if(product_dosage_detailList.get(0).getInfestation_level().equalsIgnoreCase("2")){
            if(position==0){
                high = product_dosage_detailList.get(0).getApp_type1_label();

                Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type1_deposition_rate());
                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq/contentProduct;
                txt_quan.setText(String.valueOf(quantity));

                if(!(product_dosage_detailList.get(0).getDiluted_quantity_1().equalsIgnoreCase(""))) {

                    String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_1();
                    Double mPointtotal = areaDel* Double.parseDouble(txt_total2);
                    String s1 = String.valueOf(mPointtotal);
                    //String res = s1.replace(".", "");
                    txt_quantity.setText(s1);
                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_1());
                }

            }else  if(position==1){
                high = product_dosage_detailList.get(0).getApp_type2_label();

                Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type2_deposition_rate());
                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq/contentProduct;
                txt_quan.setText(String.valueOf(quantity));

                if(!(product_dosage_detailList.get(0).getDiluted_quantity_2().equalsIgnoreCase(""))) {

                    String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_2();
                    Double mPointtotal = areaDel * Double.parseDouble(txt_total2);
                    String s1 = String.valueOf(mPointtotal);
                    //String res = s1.replace(".", "");
                    txt_quantity.setText(s1);
                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_2());

                }

            }else  if(position==2){
                high = product_dosage_detailList.get(0).getApp_type3_label();

                Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type3_deposition_rate());
                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq/contentProduct;
                txt_quan.setText(String.valueOf(quantity));
                if(!(product_dosage_detailList.get(0).getDiluted_quantity_3().equalsIgnoreCase(""))) {

                    String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_3();
                    Double mPointtotal = areaDel * Double.parseDouble(txt_total2);
                    String s1 = String.valueOf(mPointtotal);
                    //String res = s1.replace(".", "");
                    txt_quantity.setText(s1);
                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_3());
                }

            }else  if(position==3){
                high = product_dosage_detailList.get(0).getApp_type4_label();

                Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type4_deposition_rate());
                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq/contentProduct;
                txt_quan.setText(String.valueOf(quantity));

                if(!(product_dosage_detailList.get(0).getDiluted_quantity_4().equalsIgnoreCase(""))) {

                    String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_4();
                    Double mPointtotal =areaDel * Double.parseDouble(txt_total2);
                    String s1 = String.valueOf(mPointtotal);
                    //String res = s1.replace(".", "");
                    txt_quantity.setText(s1);
                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_4());
                }

            }else  if(position==4){
                high = product_dosage_detailList.get(0).getApp_type5_label();

                Float amountReq = area* Float.parseFloat(product_dosage_detailList.get(0).getApp_type5_deposition_rate());
                Float contentProduct = Float.parseFloat(product_dosage_detailList.get(0).getContent_of_product());

                Float quantity = amountReq/contentProduct;
                txt_quan.setText(String.valueOf(quantity));

                if(!(product_dosage_detailList.get(0).getDiluted_quantity_5().equalsIgnoreCase(""))) {

                    String txt_total2 = product_dosage_detailList.get(0).getDiluted_quantity_5();
                    Double mPointtotal = areaDel * Double.parseDouble(txt_total2);
                    String s1 = String.valueOf(mPointtotal);
                    //String res = s1.replace(".", "");
                    txt_quantity.setText(s1);
                    txt_quantitySolu.setText(product_dosage_detailList.get(0).getDiluted_quantity_4());
                }

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
