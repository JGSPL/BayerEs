package com.procialize.bayer2020.ui.catalogue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;

import java.util.List;

public class PestListAdapter extends RecyclerView.Adapter<PestListAdapter.ProductViewHolder> {

    private List<Pest_item> productLists;
    private Context context;
    private PestListAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;

    public PestListAdapter(Context context, List<Pest_item> productLists, PestListAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public PestListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_type_row, parent, false);
        return new PestListAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PestListAdapter.ProductViewHolder holder, final int position) {

        final Pest_item productType = productLists.get(position);

        Glide.with(holder.imageIv)
                .load(imageurl+productType.getPest_image())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.imageIv);

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
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
        void onContactSelected(Pest_item pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageIv ;
        public CardView mainLL;
        ProgressBar progressBar;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLL = itemView.findViewById(R.id.mainLL);
            imageIv = itemView.findViewById(R.id.imageIv);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}