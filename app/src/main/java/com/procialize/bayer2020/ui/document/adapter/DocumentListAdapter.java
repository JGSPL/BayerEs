package com.procialize.bayer2020.ui.document.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.document.model.DocumentDetail;
import com.procialize.bayer2020.ui.document.view.DocumentDetailActivity;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder> {

    Context context;
    List<DocumentDetail> docList;
    DocumentListAdapter.DocumentListAdapterListner listener;
    public static boolean isClickable = true;

    public DocumentListAdapter(Context context, List<DocumentDetail> docList,DocumentListAdapterListner listener) {
        this.context = context;
        this.docList = docList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public DocumentListAdapter.DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doc_list_item, parent, false);
        return new DocumentListAdapter.DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentListAdapter.DocumentViewHolder holder, final int position) {
        //Newsfeed_detail feedData = feed_detail.get(position);
        final DocumentDetail doc = docList.get(position);

     /*   String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);
        String eventColor = SharedPreference.getPref(context, EVENT_COLOR_1);
        int color = Color.parseColor(eventColor);
        String eventColor3Opacity40 = eventColor3.replace("#", "");

        holder.doc_list_layout.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        holder.quiz_title_txt.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
        holder.img_pdf.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.img_dwnload.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);*/
        holder.quiz_title_txt.setText(doc.getDocument_name());

        holder.doc_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClickable = false;
                Intent pdfview = new Intent(context, DocumentDetailActivity.class);
                pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + doc.getDocument_file_name());
                pdfview.putExtra("url1", doc.getDocument_file_name());
                pdfview.putExtra("DocId", doc.getDoc_id());

                context.startActivity(pdfview);
            }
        });

        holder.rl_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMoreSelected(doc,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(docList!=null) {
            return docList.size();
        }else{
            return 0;
        }

    }


    public class DocumentViewHolder extends RecyclerView.ViewHolder {


        public TextView quiz_title_txt;
        LinearLayout doc_list_layout;
        ImageView img_pdf, img_dwnload;
        RelativeLayout rl_download;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title_txt = itemView.findViewById(R.id.quiz_title_txt);
            doc_list_layout = itemView.findViewById(R.id.doc_list_layout);
            img_dwnload = itemView.findViewById(R.id.img_dwnload);
            img_pdf = itemView.findViewById(R.id.img_pdf);
            rl_download = itemView.findViewById(R.id.rl_download);


        }
    }

    public interface DocumentListAdapterListner {
        void onMoreSelected(DocumentDetail event, int position);
    }

}
