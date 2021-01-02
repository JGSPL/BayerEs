package com.procialize.bayer2020.ui.loyalityleap.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.procialize.bayer2020.R;


public class LoyalityLeapFragment  extends Fragment {
    ImageView imgScheame;

    public static LoyalityLeapFragment newInstance() {

        return new LoyalityLeapFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_loyality_leap, container, false);
        imgScheame = root.findViewById(R.id.imgScheame);
        imgScheame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScheameOfferListActivity.class));

            }
        });

        return root;

    }
}
