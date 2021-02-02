package com.procialize.bayer2020.ui.catalogue.view;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.adapter.ProductSubPointAdapter;
import com.procialize.bayer2020.ui.catalogue.model.Product_item;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;

import org.jsoup.Jsoup;

import java.util.List;

public class ProductSubPointFragment extends Fragment implements ProductSubPointAdapter.ProductAdapterListner{


    View rootView;
    RecyclerView rv_recommended_product;
    private List<product_subpoint_detail> ProductSubpointList;
    ProductSubPointAdapter productDocumetAdapter;
    String DocumentPath;
    TextView txtDescription;
    Product_item product_item;

    String spannedString;
    String postStatus;
    public ProductSubPointFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_product_subpoint, container, false);

        ProductSubpointList = (List<product_subpoint_detail>) getArguments().getSerializable("productSubPoint");
        DocumentPath = getArguments().getString("DocumentPath");
        product_item = (Product_item)getArguments().getSerializable("ProductType");

        rv_recommended_product = rootView.findViewById(R.id.rv_recommended_product);
        txtDescription = rootView.findViewById(R.id.txtDescription);


        if (product_item.getProduct_long_description().contains("\n")) {
            postStatus =product_item.getProduct_long_description().trim().replace("\n", "<br/>");
        } else {
            postStatus = product_item.getProduct_long_description().trim();
        }
        spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
            txtDescription.setText(Utility.trimTrailingWhitespace(strPost));
        } else {
            Spanned strPost = Html.fromHtml(spannedString);
            txtDescription.setText(Utility.trimTrailingWhitespace(strPost));
        }

        //txtDescription.setText(product_item.getProduct_long_description());
        setupEventAdapter(ProductSubpointList);
        return  rootView;
    }

    public void setupEventAdapter(List<product_subpoint_detail> commentList) {
        if (commentList != null) {
            productDocumetAdapter = new ProductSubPointAdapter(getActivity(), commentList, this,DocumentPath);
            rv_recommended_product.setLayoutManager(new GridLayoutManager(getActivity(),1));
            rv_recommended_product.setAdapter(productDocumetAdapter);
            productDocumetAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onContactSelected(product_subpoint_detail pollList) {

    }
}
