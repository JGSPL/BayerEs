package com.procialize.eventapp.ui.SpotQuiz.View;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.quiz.model.QuizList;
import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.model.QuizOption;
import com.procialize.eventapp.ui.quiz.model.QuizQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.QUIZSPOTLOGO_MEDIA_PATH;

public class SpotQuizFragment extends Fragment {
    String api_token, eventid;
    CardView Quizcard;
    ImageView quizlogo;
    Button btnQuizStart;
    ProgressBar progressbar;
    TextView txt_msg, title;
    Agenda agenda;
    private APIService eventApi;
    MutableLiveData<QuizListing> quizList = new MutableLiveData<>();
    List<QuizList> quizLists;
    List<QuizQuestion> questionList;
    List<QuizOption> quizOptionLists;
    LinearLayout linear;

    public static SpotQuizFragment newInstance() {

        return new SpotQuizFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_spot_quiz, container, false);

        btnQuizStart = root.findViewById(R.id.btnQuizStart);
        Quizcard = root.findViewById(R.id.Quizcard);
        quizlogo = root.findViewById(R.id.logoIv);
        title = root.findViewById(R.id.title);
        txt_msg = root.findViewById(R.id.txt_msg);
        progressbar = root.findViewById(R.id.progressbar);
        linear = root.findViewById(R.id.linear);
        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");

//        CommonFunction.showBackgroundImage(getContext(), Quizcard);
        Quizcard.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_2)));
        btnQuizStart.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        btnQuizStart.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_2)));
        title.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        txt_msg.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));

        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            getQuiz(api_token, eventid);

        } else {
        }

        btnQuizStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizLists.size() > 0) {
                    QuizList quizList = new QuizList();
                    quizList.setQuiz_id(quizLists.get(0).getQuiz_id());
                    quizList.setFolder_id(quizLists.get(0).getFolder_id());
                    quizList.setTimer(quizLists.get(0).getTimer());
                    quizList.setFolder_name(quizLists.get(0).getFolder_name());
                    quizList.setTotal_correct(quizLists.get(0).getTotal_correct());
                    questionList = quizLists.get(0).getQuiz_question();


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("questionlist", (Serializable) questionList);
                    bundle.putString("id", quizLists.get(0).getQuiz_id());
                    bundle.putString("folder_id", quizLists.get(0).getFolder_id());
                    bundle.putString("folder_name", quizLists.get(0).getFolder_name());
                    bundle.putString("timer", quizLists.get(0).getTimer());
                    bundle.putString("total_correct", quizLists.get(0).getTotal_correct());
                    bundle.putString("session_id", agenda.getSession_id());

                    if (quizLists.get(0).getQuiz_question().get(0).getReplied().equalsIgnoreCase("1")) {
//                        Bundle bundle1 = new Bundle();
//                        bundle1.putString("folder_id", quizLists.get(0).getFolder_id());
//                        bundle1.putString("folderName",  quizLists.get(0).getFolder_name());
//                        bundle1.putString("Answers", quizLists.get(0).getTotal_correct());
//                        bundle1.putString("TotalQue", quizLists.get(0).getTotal_quiz());
//                        bundle1.putString("id", quizLists.get(0).getQuiz_id());
//                        bundle1.putString("session_id", agenda.getSession_id());
//                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        SpotQuizResultFragment fragInfo = new SpotQuizResultFragment();
//                        fragInfo.setArguments(bundle1);
//                        transaction.replace(R.id.fragment_frame, fragInfo);
//                        transaction.addToBackStack(null);
//                        transaction.commit();

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("folderName", quizLists.get(0).getFolder_name());
                        bundle1.putString("Answers", quizLists.get(0).getTotal_correct());
                        bundle1.putString("TotalQue",quizLists.get(0).getTotal_quiz());
                        bundle1.putString("id", quizLists.get(0).getQuiz_id());
                        bundle1.putString("folder_id", quizLists.get(0).getFolder_id());
                        bundle1.putString("session_id",  agenda.getSession_id());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        YourScroreFragment fragInfo = new YourScroreFragment();
                        fragInfo.setArguments(bundle1);
                        transaction.replace(R.id.fragment_frame, fragInfo);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        SpotQuizDetailFragment fragInfo = new SpotQuizDetailFragment();
                        fragInfo.setArguments(bundle);
                        transaction.replace(R.id.fragment_frame, fragInfo);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Utility.createShortSnackBar(Quizcard, "Poll not available");

                }
            }
        });

        return root;


    }


    public MutableLiveData<QuizListing> getQuiz(String token, String eventid) {
        eventApi = ApiUtils.getAPIService();

        eventApi.SpotQuizFetch(token, eventid, agenda.getSession_id())
                .enqueue(new Callback<QuizListing>() {
                    @Override
                    public void onResponse(Call<QuizListing> call, Response<QuizListing> response) {
                        if (response.isSuccessful()) {
                            try {


                                quizList.setValue(response.body());
                                String strCommentList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(getContext());
                                String data = refreashToken.decryptedData(strCommentList);
                                String decrypteventdetail = refreashToken.decryptedData(response.body().getDetail());
//                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getDetailpreencrypt().getLogo_url_path()));
//                    String strFilePath = CommonFunction.stripquotes(event.getDetailpreencrypt().getLogo_url_path());
                                JsonParser jp = new JsonParser();
                                JsonElement je = jp.parse(decrypteventdetail);
                                JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                                JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                                JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                                JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");
                                String strFilePath = String.valueOf(je5);
                                Gson gson = new Gson();
                                quizLists = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                                }.getType());
                                HashMap<String, String> map = new HashMap<>();
                                map.put(QUIZSPOTLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                                SharedPreference.putPref(getActivity(), map);
                                String logoUrl = SharedPreference.getPref(getActivity(), QUIZLOGO_MEDIA_PATH);
                                if (quizList != null) {
                                    Glide.with(getActivity()).load(logoUrl)
                                            .apply(RequestOptions.skipMemoryCacheOf(false))
                                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    quizlogo.setImageResource(R.drawable.quizlogo);

                                                    progressbar.setVisibility(View.GONE);
                                                    return true;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progressbar.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            }).into(quizlogo);


                                } else {
                                    progressbar.setVisibility(View.GONE);
//                            progressView.setVisibility(View.GONE);

                                }


                            } catch (Exception e) {
                                linear.setVisibility(View.GONE);
                                txt_msg.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    @Override
                    public void onFailure(Call<QuizListing> call, Throwable t) {
                        quizList.setValue(null);
                    }
                });

        return quizList;
    }

}

