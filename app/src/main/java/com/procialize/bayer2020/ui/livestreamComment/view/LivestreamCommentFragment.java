package com.procialize.bayer2020.ui.livestreamComment.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procialize.bayer2020.R;

public class LivestreamCommentFragment extends Fragment {


    public LivestreamCommentFragment() {
        // Required empty public constructor
    }

    public static LivestreamCommentFragment newInstance() {

        return new LivestreamCommentFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_livestream_comment, container, false);
    }
}