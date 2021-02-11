package com.procialize.bayer2020.ui.catalogue.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.catalogue.adapter.ProductDocumentAdapter;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.document.view.DocumentActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.DOCUMENT_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class ProductDocumentFragment extends Fragment implements ProductDocumentAdapter.ProductAdapterListner{


    View rootView;
    RecyclerView rv_recommended_product;
    private List<Product_document_detail> ProductdocumentList;
    ProductDocumentAdapter productDocumetAdapter;
    String DocumentPath,api_token,event_id;
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

        api_token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(getContext(), EVENT_ID);

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
        pdfview.putExtra("DocId", pollList.getId());

        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + DocumentPath+pollList.getProduct_document_filename());
       /* pdfview.putExtra("url1", ApiConstant.imgURL + "uploads/travel_gallery/" + document.getFile_name());
        pdfview.putExtra("type",  document.getType());
        pdfview.putExtra("content",  document.getContent());*/

        startActivity(pdfview);
    }

    @Override
    public void onMoreSelected(Product_document_detail event, int position) {
        String path = SharedPreference.getPref(getContext(), DOCUMENT_MEDIA_PATH);
        new DownloadFile().execute(DocumentPath + event.getProduct_document_filename());

        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getContext(),api_token,
                event_id,
                ProductdocumentList.get(0).getProduct_id(),
                "product_document_download",
                "Product Document",
                "10");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------

    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getContext());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "BayerEs/Documents/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("error", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getContext(),
                    message, Toast.LENGTH_LONG).show();

//            sharePdf(folder + fileName, PdfViewerActivity.this);
        }
    }

}