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
import com.procialize.bayer2020.ui.loyalityleap.model.PurchaseHistory_row;
import com.procialize.bayer2020.ui.loyalityleap.view.PurchaseHistoryActivity;

import java.util.List;

public class PurchageHistoryAdapter extends RecyclerView.Adapter<PurchageHistoryAdapter.ProductViewHolder> {

    private List<PurchaseHistory_row> productLists;
    private Context context;
    private PurchageHistoryAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public PurchageHistoryAdapter(Context context, List<PurchaseHistory_row> productLists, PurchageHistoryAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public PurchageHistoryAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.purchage_history_row, parent, false);
        return new PurchageHistoryAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PurchageHistoryAdapter.ProductViewHolder holder, int position) {

        final PurchaseHistory_row redeemItem = productLists.get(position);
        holder.txt_product_name.setText(redeemItem.getProduct_name());
        holder.txt_volume.setText(redeemItem.getVolume());
        holder.txt_mPoint.setText(redeemItem.getMpoint());
        int i = position+1;
        holder.txt_srno.setText(String.valueOf(i));
        String dateTime = redeemItem.getYear();
        if (!dateTime.isEmpty()) {
            String convertedDate = CommonFunction.convertYear(dateTime);
            holder.txtyear.setText(convertedDate);
        }
        if(productLists.size()>0){
            int Tot = 0;
            if(position==0) {
                for (int j = 0; j < productLists.size(); j++) {
                    Tot = Tot + Integer.parseInt(productLists.get(j).getMpoint());

                }
                PurchaseHistoryActivity.txtPurchagePoint.setText(String.valueOf(Tot));

            }

        }
        // Tot = Tot + Integer.parseInt(redeemItem.getMpoint());

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(PurchaseHistory_row pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_srno, txt_product_name, txtyear,txt_volume,txt_mPoint;
        LinearLayout linear1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_srno = itemView.findViewById(R.id.txt_srno);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txtyear = itemView.findViewById(R.id.txtyear);
            txt_volume = itemView.findViewById(R.id.txt_volume);
            txt_mPoint = itemView.findViewById(R.id.txt_mPoint);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
}

