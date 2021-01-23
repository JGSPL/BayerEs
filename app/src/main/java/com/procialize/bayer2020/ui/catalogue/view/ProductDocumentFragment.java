package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.catalogue.adapter.ProductDocumentAdapter;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;

import java.util.List;

public class ProductDocumentFragment extends Fragment implements ProductDocumentAdapter.ProductAdapterListner{


    View rootView;
    RecyclerView rv_recommended_product;
    private List<Product_document_detail> ProductdocumentList;
    ProductDocumentAdapter productDocumetAdapter;
    String DocumentPath;
    public ProductDocumentFragment() {
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

        ProductdocumentList = (List<Product_document_detail>) getArguments().getSerializable("productDocumentList");
        DocumentPath = getArguments().getString("DocumentPath");
        rv_recommended_product = rootView.findViewById(R.id.rv_recommended_product);

        setupEventAdapter(ProductdocumentList);
        return  rootView;
    }

    public void setupEventAdapter(List<Product_document_detail> commentList) {
        if (commentList != null) {
            productDocumetAdapter = new ProductDocumentAdapter(getActivity(), commentList, this,DocumentPath);
            rv_recommended_product.setLayoutManager(new GridLayoutManager(getActivity(),1));
            rv_recommended_product.setAdapter(productDocumetAdapter);
            productDocumetAdapter.notifyDataSetChanged();
        }
    }

   

    @Override
    public void onContactSelected(Product_document_detail pollList) {
        Intent pdfview = new Intent(getContext(), ProductDocumentDetailActivity.class);
        //pdfview.putExtra("url", "https://drive.google.com/viewerng/viewer?embedded=true&url=" + ApiConstant.imgURL + "uploads/travel_gallery/" + document.getFile_name());

        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + DocumentPath+pollList.getProduct_document_original_filename());
       /* pdfview.putExtra("url1", ApiConstant.imgURL + "uploads/travel_gallery/" + document.getFile_name());
        pdfview.putExtra("type",  document.getType());
        pdfview.putExtra("content",  document.getContent());*/

        startActivity(pdfview);
    }
}