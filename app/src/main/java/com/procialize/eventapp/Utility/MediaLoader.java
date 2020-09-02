package com.procialize.eventapp.Utility;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.procialize.eventapp.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView)
                .load(url)
                .error(R.mipmap.placeholder)
                .placeholder(R.mipmap.placeholder)
                .into(imageView);
    }
}
