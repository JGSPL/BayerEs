package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_item;

import java.util.List;

public class RedeemHistoryAdapter extends RecyclerView.Adapter<RedeemHistoryAdapter.ProductViewHolder> {

    private List<redeem_history_item> productLists;
    private Context context;
    private RedeemHistoryAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public RedeemHistoryAdapter(Context context, List<redeem_history_item> productLists, RedeemHistoryAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public RedeemHistoryAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_history_row, parent, false);
        return new RedeemHistoryAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RedeemHistoryAdapter.ProductViewHolder holder, final int position) {

        final redeem_history_item redeemItem = productLists.get(position);
        holder.txt_cat_name.setText(redeemItem.getProduct_name());
        holder.txtQuantity.setText(redeemItem.getQuantity());
        holder.txt_point.setText(redeemItem.getPoints());
        if(redeemItem.getStatus().equalsIgnoreCase("0")){
            holder.txt_status.setText("Requested");
            holder.txt_status.setTextColor(Color.parseColor("#002e46"));

        }else  if(redeemItem.getStatus().equalsIgnoreCase("3")){
            holder.txt_status.setText("Delivered");
            holder.txt_status.setTextColor(Color.parseColor("#5a9e31"));


        }else  if(redeemItem.getStatus().equalsIgnoreCase("2")){
            holder.txt_status.setText("Processed");
            holder.txt_status.setTextColor(Color.parseColor("#f14433"));


        }else  if(redeemItem.getStatus().equalsIgnoreCase("1")){
            holder.txt_status.setText("Approved");
            holder.txt_status.setTextColor(Color.parseColor("#008bde"));


        }else  if(redeemItem.getStatus().equalsIgnoreCase("4")){
            holder.txt_status.setText("Complete");
            holder.txt_status.setTextColor(Color.parseColor("#008bde"));


        }
        String dateTime = redeemItem.getRedemption_date();
        if (!dateTime.isEmpty()) {
            String convertedDate = CommonFunction.convertDateRedeem(dateTime);
            holder.txt_date.setText(convertedDate);
        }

        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(productLists.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(redeem_history_item pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date, txt_cat_name, txtQuantity,txt_point,txt_status;
        LinearLayout linear1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_cat_name = itemView.findViewById(R.id.txt_cat_name);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txt_point = itemView.findViewById(R.id.txt_point);
            txt_status = itemView.findViewById(R.id.txt_status);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
}

