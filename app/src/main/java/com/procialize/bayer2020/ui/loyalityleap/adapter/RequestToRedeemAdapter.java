package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.ui.loyalityleap.model.RequestToRedeem;

import java.util.List;

public class RequestToRedeemAdapter extends RecyclerView.Adapter<RequestToRedeemAdapter.ProductViewHolder> {

    private List<RequestToRedeem> productLists;
    private Context context;
    private RequestToRedeemAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public RequestToRedeemAdapter(Context context, List<RequestToRedeem> productLists, RequestToRedeemAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public RequestToRedeemAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_to_redeem_row, parent, false);
        return new RequestToRedeemAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestToRedeemAdapter.ProductViewHolder holder, final int position) {

        final RequestToRedeem redeemItem = productLists.get(position);
        holder.txt_points.setText(redeemItem.getProduct_name());
       
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
        void onContactSelected(RequestToRedeem pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_rewardname, txt_points, txt_needed;
        ImageView image_reward;
        Button btn_reedem;
        LinearLayout linear1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_rewardname = itemView.findViewById(R.id.txt_rewardname);
            txt_points = itemView.findViewById(R.id.txt_points);
            txt_needed = itemView.findViewById(R.id.txt_needed);
            btn_reedem = itemView.findViewById(R.id.btn_reedem);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
}
