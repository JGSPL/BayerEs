package com.procialize.bayer2020.ui.quiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.quiz.model.QuizList;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    Context context;
    List<QuizList> quizList;
    QuizListAdapter.QuizListAdapterListner listener;
    public static boolean isClickable = true;
    String logoUrl;

    public QuizListAdapter(Context context, List<QuizList> quizList, QuizListAdapter.QuizListAdapterListner listener) {
        this.context = context;
        this.quizList = quizList;
        this.listener = listener;

        logoUrl = SharedPreference.getPref(context, QUIZLOGO_MEDIA_PATH);
    }

    @NonNull
    @Override
    public QuizListAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_item_row, parent, false);
        return new QuizListAdapter.QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizListAdapter.QuizViewHolder holder, final int position) {
        //Newsfeed_detail feedData = feed_detail.get(position);
        final QuizList quiz = quizList.get(position);

        /*String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        holder.linQuiz.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));
        holder.layoutBottom.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));
        holder.quiz_title_txt.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
        holder.quiz_status.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.textViewTime.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.right_arrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
*/
        /*if (logoUrl != null) {
            holder.progressBar.setVisibility(View.GONE);

            Glide.with(context).load(logoUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.profileIV.setImageResource(R.drawable.quizlogo);

                            holder.progressBar.setVisibility(View.GONE);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.profileIV);

        } else {
            holder.progressBar.setVisibility(View.GONE);

        }
        if (position == 0) {
            holder.rl_bg_image.setVisibility(View.VISIBLE);
        } else {
            holder.rl_bg_image.setVisibility(View.GONE);
        }*/

        if(quizList.get(position).getQuiz_question().get(0).getReplied().equalsIgnoreCase("0")){
            holder.progressBarCircle.setVisibility(View.INVISIBLE);
            holder.textViewTime.setVisibility(View.INVISIBLE);
            holder.quiz_status.setText("Tap to start");
        }else {
            holder.progressBarCircle.setVisibility(View.VISIBLE);
            holder.textViewTime.setVisibility(View.VISIBLE);
            holder.quiz_status.setText("Completed");

            holder.textViewTime.setText(Integer.parseInt(String.valueOf(quizList.get(position).getTotal_correct())) + "/" +
                    Integer.parseInt(String.valueOf(quizList.get(position).getQuiz_question().size())));

            holder.progressBarCircle.setMax(Integer.parseInt(String.valueOf(quizList.get(position).getQuiz_question().size())));

            holder.quiz_list_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isClickable = false;
                    listener.onMoreSelected(quiz, position);

                }
            });
            if(quizList.get(position).getTotal_correct().equalsIgnoreCase("0")){
                holder.progressBarCircle.setProgress(0);

            }else {
//                holder.progressBarCircle.setMin(Integer.parseInt(quizList.get(position).getTotal_correct()));
                holder.progressBarCircle.setProgress(Integer.parseInt(quizList.get(position).getTotal_correct()));
            }
        }

        holder.quiz_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClickable = false;
                listener.onMoreSelected(quiz, position);

            }
        });
        holder.quiz_title_txt.setText(quiz.getFolder_name());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }


    public class QuizViewHolder extends RecyclerView.ViewHolder {


        public TextView quiz_title_txt, quiz_status, textViewTime;
        //RelativeLayout rl_bg_image;
        ImageView right_arrow;
        ProgressBar  progressBarCircle;
        LinearLayout quiz_list_layout,linQuiz;
        //RelativeLayout layoutBottom;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title_txt = itemView.findViewById(R.id.quiz_title_txt);
            /*rl_bg_image = itemView.findViewById(R.id.rl_bg_image);
            progressBar = itemView.findViewById(R.id.progressBar);
            profileIV = itemView.findViewById(R.id.profileIV);*/
            quiz_status = itemView.findViewById(R.id.quiz_status);
            progressBarCircle = itemView.findViewById(R.id.progressBarCircle);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            quiz_list_layout = itemView.findViewById(R.id.quiz_list_layout);
            linQuiz = itemView.findViewById(R.id.linQuiz);
            //layoutBottom = itemView.findViewById(R.id.layoutBottom);
            right_arrow = itemView.findViewById(R.id.right_arrow);


        }
    }

    public interface QuizListAdapterListner {
        void onMoreSelected(QuizList event, int position);
    }
}