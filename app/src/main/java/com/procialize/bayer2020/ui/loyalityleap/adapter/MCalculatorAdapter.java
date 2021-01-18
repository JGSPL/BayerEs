package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.ui.loyalityleap.model.m_points_list;
import com.procialize.bayer2020.ui.loyalityleap.view.MPointActivity;

import java.util.List;

public class MCalculatorAdapter extends RecyclerView.Adapter<MCalculatorAdapter.ProductViewHolder> {

    private List<m_points_list> productLists;
    private Context context;
    private MCalculatorAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;
    Long mpoints, points, TotalPoints;
    Long points1;
    String str;
    
    public MCalculatorAdapter(Context context, List<m_points_list> productLists, MCalculatorAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

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
        holder.txt_packsize.setText(travel.getPack());
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
                            points = Long.parseLong(holder.mpointspermpin.getText().toString().trim());
                            mpoints = Long.parseLong(holder.txt_noofmpin.getText().toString().trim());

                            TotalPoints = points * mpoints;
                            String s = String.valueOf(TotalPoints);
                            str = s.replace(".", "");

//                            Integer result = Integer.valueOf(str) / 10;
                            holder.txt_total.setText(String.valueOf(str));

                            points1 = Long.parseLong(holder.txt_noofmpin.getText().toString().trim());
                            String txt_score = MPointActivity.txt_score.getText().toString().trim();
                            String txt_total = holder.txt_total.getText().toString().trim();

                            if (txt_score.equalsIgnoreCase("")) {
//                                txt_score = "0";
//                                Float mPointtotal = Float.valueOf(txt_score) + Float.valueOf(txt_total);
                                String s1 = String.valueOf(txt_total);
                                String res = s1.replace(".", "");
                                MPointActivity.txt_score.setText(res);
                            } else {
                                Long mPointtotal = Long.parseLong(txt_score) + Long.parseLong(txt_total);
                                String s1 = String.valueOf(mPointtotal);
                                String res = s1.replace(".", "");
                                MPointActivity.txt_score.setText(res);
                            }
                            holder.txt_noofmpin.setFocusable(false);
                            holder.txt_noofmpin.setEnabled(false);
                            return true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(m_points_list pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_product, txt_packsize, mpointspermpin, txt_total;
        EditText txt_noofmpin;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product = itemView.findViewById(R.id.txt_product);
            txt_packsize = itemView.findViewById(R.id.txt_packsize);
            mpointspermpin = itemView.findViewById(R.id.mpointspermpin);
            txt_total = itemView.findViewById(R.id.txt_total);

            txt_noofmpin = itemView.findViewById(R.id.txt_noofmpin);
        }
    }
}

