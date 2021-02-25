package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.catalogue.adapter.PestRecommendedProductsAdapter;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestRecommendedProducts;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;
import com.procialize.bayer2020.ui.upskill.adapter.UpskillAdapter;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import java.io.Serializable;
import java.util.List;

public class RecommendedProductFragment extends Fragment implements PestRecommendedProductsAdapter.ProductAdapterListner{


    View rootView;
    RecyclerView rv_recommended_product;
    String strRecommendedPath;
    private List<CataloguePestRecommendedProducts> cataloguePestRecommendedProducts;
    PestRecommendedProductsAdapter pestRecommendedProductsAdapter;
    public RecommendedProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recommended_product, container, false);

        cataloguePestRecommendedProducts = (List<CataloguePestRecommendedProducts>) getArguments().getSerializable("recommendedeProductList");
        strRecommendedPath = getArguments().getString("strRecommendedPath");
        rv_recommended_product = rootView.findViewById(R.id.rv_recommended_product);

        setupEventAdapter(cataloguePestRecommendedProducts);
        return  rootView;
    }

    public void setupEventAdapter(List<CataloguePestRecommendedProducts> commentList) {
        if (commentList != null) {
            pestRecommendedProductsAdapter = new PestRecommendedProductsAdapter(getActivity(), commentList, this,strRecommendedPath);
            rv_recommended_product.setLayoutManager(new GridLayoutManager(getActivity(),2));
            rv_recommended_product.setAdapter(pestRecommendedProductsAdapter);
            pestRecommendedProductsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(CataloguePestRecommendedProducts pollList) {
       /* startActivity(new Intent(getContext(), ProductListDetailActivity.class)
                .putExtra("Imageurl", Imageurl)
                .putExtra("Product", (Serializable) product_item));*/
    }
}