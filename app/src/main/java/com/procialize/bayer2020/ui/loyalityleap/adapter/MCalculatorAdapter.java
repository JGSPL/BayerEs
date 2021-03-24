package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.ui.catalogue.view.ProductmCalculator_Activity;
import com.procialize.bayer2020.ui.loyalityleap.model.m_points_list;
import com.procialize.bayer2020.ui.loyalityleap.view.MPointActivity;

import java.util.List;

public class MCalculatorAdapter extends RecyclerView.Adapter<MCalculatorAdapter.ProductViewHolder> {

    private List<m_points_list> productLists;
    private Context context;
    private MCalculatorAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;
    Double mpoints, points, TotalPoints;
    Double points1;
    String str;
    Double TotalScore;
    String txt_score,txt_total;
    Double[] intArray; // Array Declared
     int size;
    public MCalculatorAdapter(Context context, List<m_points_list> productLists, MCalculatorAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;
        size = productLists.size();
        intArray = new Double[size];
        for (int i = 0; i < size; i++) {
            intArray[i] = 0.0;
        }

    }

    @NonNull
    @Override
    public MCalculatorAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mpointcalcitem, parent, false);

        return new MCalculatorAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MCalculatorAdapter.ProductViewHolder holder, final int position) {

        final m_points_list travel = productLists.get(position);
        holder.txt_product.setText(travel.getProduct_name());
        holder.txt_packsize.setText(travel.getPack()+travel.getUnit());
        holder.mpointspermpin.setText(travel.getPoints());

        holder.txt_noofmpin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    holder.txt_noofmpin.getText().clear();
                }
                return false;
            }
        });
        try {
            holder.txt_noofmpin.addTextChangedListener(new TextWatcher() {

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
                        if (holder.txt_noofmpin.getText().toString().isEmpty()) {
                            points = Double.parseDouble(holder.mpointspermpin.getText().toString().trim());
                            mpoints = 0.0;

                            TotalPoints = points * mpoints;
                            String s4 = String.valueOf(TotalPoints);

                            holder.txt_total.setText(String.valueOf(s4));

                            points1 = 0.0;
                            txt_score = MPointActivity.txt_score.getText().toString().trim();
                            txt_total = holder.txt_total.getText().toString().trim();
                            TotalScore = Double.parseDouble(txt_total);
                            intArray[position] = TotalScore;
                            Double TotalFinal = 0.0;
                            for (int i = 0; i < size; i++) {
                                TotalFinal = TotalFinal + intArray[i];
                            }
                            String s1 = String.valueOf(TotalFinal);
                            MPointActivity.txt_score.setText(s1);
                        }else {
                            points = Double.parseDouble(holder.mpointspermpin.getText().toString().trim());
                            mpoints = Double.parseDouble(holder.txt_noofmpin.getText().toString().trim());

                            TotalPoints = points * mpoints;
                            String s4 = String.valueOf(TotalPoints);

                            holder.txt_total.setText(String.valueOf(s4));

                            points1 = Double.parseDouble(holder.txt_noofmpin.getText().toString().trim());
                            txt_score = MPointActivity.txt_score.getText().toString().trim();
                            txt_total = holder.txt_total.getText().toString().trim();
                            TotalScore = Double.parseDouble(txt_total);
                            intArray[position] = TotalScore;
                            Double TotalFinal = 0.0;
                            for (int i = 0; i < size; i++) {
                                TotalFinal = TotalFinal + intArray[i];
                            }
                            String s1 = String.valueOf(TotalFinal);
                            MPointActivity.txt_score.setText(s1);
                        }


                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }



/*
        holder.txt_noofmpin.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    try {

                        if (holder.txt_noofmpin.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Please Enter Value", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                           */
/* points = Double.parseDouble(holder.mpointspermpin.getText().toString().trim());
                            mpoints = Double.parseDouble(holder.txt_noofmpin.getText().toString().trim());

                            TotalPoints = points * mpoints;
                            String s = String.valueOf(TotalPoints);
                            str = s.replace(".", "");

                            holder.txt_total.setText(String.valueOf(str));

                            points1 = Long.parseLong(holder.txt_noofmpin.getText().toString().trim());
                            String txt_score = MPointActivity.txt_score.getText().toString().trim();
                            String txt_total = holder.txt_total.getText().toString().trim();

                            if (txt_score.equalsIgnoreCase("")) {
                                String s1 = String.valueOf(txt_total);
                                String res = s1.replace(".", "");
                                MPointActivity.txt_score.setText(res);
                            } else {
                                Double mPointtotal = Double.parseDouble(txt_score) +Double.parseDouble(txt_total);
                                String s1 = String.valueOf(mPointtotal);
                                String res = s1.replace(".", "");
                                MPointActivity.txt_score.setText(res);
                            }
                            holder.txt_noofmpin.setFocusable(false);
                            holder.txt_noofmpin.setEnabled(false);*//*

                            Double mPointtotal = Double.parseDouble(txt_score) + Double.parseDouble(txt_total);
                            String s1 = String.valueOf(mPointtotal);

                            MPointActivity.txt_score.setText(s1);
                            holder.txt_noofmpin.setFocusable(false);
                            holder.txt_noofmpin.setEnabled(false);
                            return true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
/*else  if (event.getKeyCode() == KeyEvent.KEYCODE_DEL){
                    try {

                        if (holder.txt_noofmpin.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Please Enter Value", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {

                            String txt_score = MPointActivity.txt_score.getText().toString().trim();
                            Double mPointtotal = Double.parseDouble(txt_score) - TotalScore;
                            String s1 = String.valueOf(mPointtotal);
                            //   String res = s1.replace(".", "");
                            MPointActivity.txt_score.setText(s1);
                            return true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }*//*

                return false;
            }
        });
*/


    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ProductAdapterListner {
        void onContactSelected(m_points_list pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_product, txt_packsize, mpointspermpin, txt_total;
        EditText txt_noofmpin;
        LinearLayout linear1;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product = itemView.findViewById(R.id.txt_product);
            txt_packsize = itemView.findViewById(R.id.txt_packsize);
            mpointspermpin = itemView.findViewById(R.id.mpointspermpin);
            txt_total = itemView.findViewById(R.id.txt_total);
            linear1 = itemView.findViewById(R.id.linear1);

            txt_noofmpin = itemView.findViewById(R.id.txt_noofmpin);
        }
    }
}

