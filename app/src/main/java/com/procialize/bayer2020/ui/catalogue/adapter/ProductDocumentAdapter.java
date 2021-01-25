package com.procialize.bayer2020.ui.catalogue.adapter;

import android.content.Context;
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
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;

import java.util.List;

public class ProductDocumentAdapter extends RecyclerView.Adapter<ProductDocumentAdapter.ProductViewHolder> {

    private List<Product_document_detail> productLists;
    private Context context;
    private ProductDocumentAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public ProductDocumentAdapter(Context context, List<Product_document_detail> productLists,
                                          ProductDocumentAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public ProductDocumentAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_download_row, parent, false);
        return new ProductDocumentAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductDocumentAdapter.ProductViewHolder holder, final int position) {

        final Product_document_detail productType = productLists.get(position);
        holder.tv_title.setText(productType.getProduct_document_filename());

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
        void onContactSelected(Product_document_detail pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_right_arrow ;
        public LinearLayout ll_row;
        TextView tv_title;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_row = itemView.findViewById(R.id.ll_row);
            iv_right_arrow = itemView.findViewById(R.id.iv_right_arrow);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}