package com.procialize.bayer2020.ui.document.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.document.model.DocumentDetail;
import com.procialize.bayer2020.ui.document.view.DocumentDetailActivity;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class DocumentGridAdapter extends RecyclerView.Adapter<DocumentGridAdapter.DocumentViewHolder> {

    Context context;
    List<DocumentDetail> docList;
    DocumentGridAdapter.DocumentListAdapterListner listener;
    public static boolean isClickable = true;

    public DocumentGridAdapter(Context context, List<DocumentDetail> docList) {
        this.context = context;
        this.docList = docList;

    }

    @NonNull
    @Override
    public DocumentGridAdapter.DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_item, parent, false);
        return new DocumentGridAdapter.DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentGridAdapter.DocumentViewHolder holder, final int position) {
        //Newsfeed_detail feedData = feed_detail.get(position);
        final DocumentDetail doc = docList.get(position);

        String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        holder.doccard.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        holder.txt_name.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));

        holder.txt_name.setText(doc.getDocument_name());

        holder.relative_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClickable = false;
                Intent pdfview = new Intent(context, DocumentDetailActivity.class);
                pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + doc.getDocument_file_name());
                pdfview.putExtra("url1",  doc.getDocument_file_name());
                context.startActivity(pdfview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }


    public class DocumentViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_name;
        CardView doccard;
        RelativeLayout relative_doc;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            doccard = itemView.findViewById(R.id.doc_card);
            relative_doc = itemView.findViewById(R.id.relative_doc);

        }
    }

    public interface DocumentListAdapterListner {
        void onMoreSelected(DocumentDetail event, int position);
    }
}
