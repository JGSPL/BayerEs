package com.procialize.eventapp.ui.speaker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.speaker.model.Speaker_Doc;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    List<Speaker_Doc> pdfLists;
    private Context context;
    private PdfListAdapterListner listener;
    String pdfpath;
    public PdfAdapter(Context context, List<Speaker_Doc> pdfLists, PdfListAdapterListner listener , String pdfpath) {
        this.pdfLists = pdfLists;
        this.listener = listener;
        this.context = context;
        this.pdfpath =pdfpath;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_pdf_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tv_pdf_name.setPaintFlags(holder.tv_pdf_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String pdfName = pdfLists.get(position).getPdf_name();
        String s1 = pdfName.substring(0, 1).toUpperCase() + pdfName.substring(1);
        holder.tv_pdf_name.setText(s1 );

        holder.tv_pdf_name.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        holder.linBox.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_4)));

        holder.tv_pdf_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pdfLists.get(position), pdfpath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfLists.size();
    }

    public interface PdfListAdapterListner {
        void onContactSelected(Speaker_Doc Speaker_Doc, String path);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_pdf_name;
        LinearLayout linBox;

        public MyViewHolder(View view) {
            super(view);
            tv_pdf_name = view.findViewById(R.id.tv_pdf_name);
            linBox = view.findViewById(R.id.linBox);
        }
    }
}