package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import com.procialize.bayer2020.R;

public class CatalogueFragment extends Fragment {
    private FragmentTabHost mTabHostCel;

    public static CatalogueFragment newInstance() {

        return new CatalogueFragment();
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalogue_tab_bg, null);

        LinearLayout linTab = view.findViewById(R.id.linTab);
        //  linTab.setBackgroundColor(Color.parseColor(colorActive));
        //  linTab.setBackground(R.drawable.a);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);


        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.catalogue_fragment,
                container, false);




        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mTabHostCel = (FragmentTabHost) getActivity().findViewById(android.R.id.tabhost);
        mTabHostCel.setup(getContext(), getChildFragmentManager(), R.id.realtabcontent);

            mTabHostCel.addTab(
                    mTabHostCel.newTabSpec("Tab1")
                            .setIndicator(createTabView(getActivity(), "Product")),
                    ProductFragment.class, null);
            mTabHostCel.addTab(
                    mTabHostCel.newTabSpec("Tab2")
                            .setIndicator(createTabView(getActivity(), "Pest")),
                    PestFragment.class, null);
                    // PestProductDetailsFragment.class, null);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHostCel.clearAllTabs();
        super.onDestroy();
    }

}