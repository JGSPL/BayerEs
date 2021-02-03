package com.procialize.bayer2020.ui.catalogue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.procialize.bayer2020.ui.document.model.DocumentDetail;

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
        View view = LayoutInflater.from(context).inflate(R.layout.doc_list_item, parent, false);
        return new ProductDocumentAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductDocumentAdapter.ProductViewHolder holder, final int position) {

        final Product_document_detail productType = productLists.get(position);
        holder.quiz_title_txt.setText(productType.getProduct_document_filename());

        holder.doc_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(productLists.get(position));
            }
        });
        holder.rl_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMoreSelected(productLists.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(Product_document_detail pollList);
        void onMoreSelected(Product_document_detail event, int position);

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView quiz_title_txt;
        LinearLayout doc_list_layout;
        ImageView img_pdf, img_dwnload;
        RelativeLayout rl_download;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title_txt = itemView.findViewById(R.id.quiz_title_txt);
            doc_list_layout = itemView.findViewById(R.id.doc_list_layout);
            img_dwnload = itemView.findViewById(R.id.img_dwnload);
            img_pdf = itemView.findViewById(R.id.img_pdf);
            rl_download = itemView.findViewById(R.id.rl_download);


        }
       /* public ImageView iv_right_arrow ;
        public LinearLayout ll_row;
        TextView tv_title;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_row = itemView.findViewById(R.id.ll_row);
            iv_right_arrow = itemView.findViewById(R.id.iv_right_arrow);
            tv_title = itemView.findViewById(R.id.tv_title);
        }*/
    }
}