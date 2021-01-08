package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_status_item;

import java.util.List;

public class RedeemHistoryStatusAdapter extends RecyclerView.Adapter<RedeemHistoryStatusAdapter.ProductViewHolder> {

    private List<redeem_history_status_item> productLists;
    private Context context;
    private RedeemHistoryStatusAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public RedeemHistoryStatusAdapter(Context context, List<redeem_history_status_item> productLists, RedeemHistoryStatusAdapter.ProductAdapterListner listener) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;

    }

    @NonNull
    @Override
    public RedeemHistoryStatusAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.redeem_status_row, parent, false);
        return new RedeemHistoryStatusAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RedeemHistoryStatusAdapter.ProductViewHolder holder, final int position) {

        final redeem_history_status_item redeemItem = productLists.get(position);
        int pos = position+1;
        holder.txt_status.setText("Status "+pos+ " : "+ redeemItem.getStatus());

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(redeem_history_status_item pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_status;
        LinearLayout linear1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_status = itemView.findViewById(R.id.txt_status);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
}

