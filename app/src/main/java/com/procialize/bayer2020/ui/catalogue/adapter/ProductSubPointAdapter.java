package com.procialize.bayer2020.ui.catalogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.Animations;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;

import java.util.List;

public class ProductSubPointAdapter extends RecyclerView.Adapter<ProductSubPointAdapter.ProductViewHolder> {

    private List<product_subpoint_detail> productLists;
    private Context context;
    private ProductSubPointAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;
    int i = 0;

    public ProductSubPointAdapter(Context context, List<product_subpoint_detail> productLists,
                                  ProductSubPointAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public ProductSubPointAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_subpoint_row, parent, false);
        return new ProductSubPointAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductSubPointAdapter.ProductViewHolder holder, final int position) {

        final product_subpoint_detail productType = productLists.get(position);
        holder.tv_title.setText(productType.getTitle());
        holder.tv_desc.setText(productType.getDescription());

/*
        holder.iv_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    holder.tv_desc.setVisibility(View.VISIBLE);
                    holder.iv_right_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cross));

                    i =1;
                }else if(i==1){
                    holder.tv_desc.setVisibility(View.GONE);
                    i = 0;
                    holder.iv_right_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus));

                }
            }
        });
*/
        holder.iv_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tv_desc.getVisibility() == View.VISIBLE) {
                    Animations.collapse(holder.tv_desc);
                    // holder.iv_right_arrow.setBackgroundResource(R.drawable.ic_plus);
                    holder.iv_right_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus));

                } else {
                    Animations.expand(holder.tv_desc);
                    // holder.iv_right_arrow.setBackgroundResource(R.drawable.ic_cross);
                    holder.iv_right_arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cross));

                }
                /*if(i==0){
                    Animations.expand(holder.tv_desc);
                    //holder.tv_desc.setVisibility(View.VISIBLE);
                    i =1;
                }else if(i==1){
                    Animations.collapse(holder.tv_desc);
                   // holder.tv_desc.setVisibility(View.GONE);
                    i = 0;
                }*/
            }
        });

        holder.ll_row.setOnClickListener(new View.OnClickListener() {
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
        void onContactSelected(product_subpoint_detail pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_right_arrow ;
        public LinearLayout ll_row;
        TextView tv_title, tv_desc;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_row = itemView.findViewById(R.id.ll_row);
            iv_right_arrow = itemView.findViewById(R.id.iv_right_arrow);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);

        }
    }
}
