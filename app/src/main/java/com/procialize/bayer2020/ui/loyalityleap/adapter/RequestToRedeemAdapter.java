package com.procialize.bayer2020.ui.loyalityleap.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.Constant;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.loyalityleap.model.RequestToRedeem;
import com.procialize.bayer2020.ui.loyalityleap.view.RequestToRedeemActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class RequestToRedeemAdapter extends RecyclerView.Adapter<RequestToRedeemAdapter.ProductViewHolder> {

    private List<RequestToRedeem> productLists;
    private Context context;
    private RequestToRedeemAdapter.ProductAdapterListner listener;
    private LayoutInflater inflater;
    String imageurl;
    Dialog myDialog;
    ProgressBar progressbar;
    String eventid, apikey;
    APIService mAPIService;

    public RequestToRedeemAdapter(Context context, List<RequestToRedeem> productLists, RequestToRedeemAdapter.ProductAdapterListner listener, String imageurl) {
        this.productLists = productLists;
        this.listener = listener;
        this.context = context;
        this.imageurl = imageurl;
        mAPIService = ApiUtils.getAPIService();
        apikey = SharedPreference.getPref(context, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(context, EVENT_ID);

    }

    @NonNull
    @Override
    public RequestToRedeemAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_to_redeem_row, parent, false);
        return new RequestToRedeemAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestToRedeemAdapter.ProductViewHolder holder, final int position) {

        final RequestToRedeem redeemItem = productLists.get(position);
        holder.txt_rewardname.setText(redeemItem.getProduct_name());

        holder.txt_points.setText("Value =" + redeemItem.getProduct_value() + " Points");

        Glide.with(context).load(imageurl + redeemItem.getProduct_image())
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.image_reward.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(holder.image_reward);

       /* if (Integer.parseInt(redeemItem.getProduct_value()) <= Integer.parseInt(RequestToRedeemActivity.txtRedeemPoint.getText().toString())) {
            holder.btn_reedem.setVisibility(View.VISIBLE);
            holder.btn_reedemnot.setVisibility(View.GONE);

        } else {
            holder.btn_reedem.setVisibility(View.GONE);
            holder.btn_reedemnot.setVisibility(View.VISIBLE);

            int Total = (Integer.parseInt(redeemItem.getProduct_value()) - Integer.parseInt(RequestToRedeemActivity.txtRedeemPoint.getText().toString()));
            holder.btn_reedemnot.setText(String.valueOf(Total) + " Points more needed.");
        }*/

        if(redeemItem.getRedeem_flag().equalsIgnoreCase("1")){
            holder.btn_reedem.setVisibility(View.VISIBLE);
            holder.btn_reedemnot.setVisibility(View.GONE);

        }else if(redeemItem.getRedeem_flag().equalsIgnoreCase("0")){
            holder.btn_reedem.setVisibility(View.GONE);
            holder.btn_reedemnot.setVisibility(View.VISIBLE);

            holder.btn_reedemnot.setText(redeemItem.getRedeem_status_line());

        }

        holder.btn_reedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showratedialouge(redeemItem.getProduct_name(), redeemItem.getProduct_value(), redeemItem.getProduct_code());
            }
        });


    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public interface ProductAdapterListner {
        void onContactSelected(RequestToRedeem pollList);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_rewardname, txt_points, txt_needed;
        ImageView image_reward;
        Button btn_reedem, btn_reedemnot;
        LinearLayout linear1;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_rewardname = itemView.findViewById(R.id.txt_rewardname);
            txt_points = itemView.findViewById(R.id.txt_points);
            btn_reedemnot = itemView.findViewById(R.id.btn_reedemnot);
            btn_reedem = itemView.findViewById(R.id.btn_reedem);
            image_reward = itemView.findViewById(R.id.image_reward);

            linear1 = itemView.findViewById(R.id.linear1);
        }
    }

    private void showratedialouge(final String name, final String value, final String productcode) {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.confirmation_dialog);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.btn_cancel);
        final Button btn_submit = myDialog.findViewById(R.id.btn_submit);
        final EditText edit_quantity = myDialog.findViewById(R.id.edit_quantity);
        TextView txtValue = myDialog.findViewById(R.id.txtValue);
        TextView txt_name = myDialog.findViewById(R.id.name);
        progressbar = myDialog.findViewById(R.id.progressBar);

        final EditText edit_address = myDialog.findViewById(R.id.edit_address);

        final EditText edit_email = myDialog.findViewById(R.id.edit_email);

        txt_name.setText(name);
        txtValue.setText("Value = " + value + " Points");

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_quantity.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter any value for proceed", Toast.LENGTH_SHORT).show();

                }else if (edit_address.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter address for proceed", Toast.LENGTH_SHORT).show();

                }else if (edit_email.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter email for proceed", Toast.LENGTH_SHORT).show();

                } else {
                    Long quantity = Long.parseLong(edit_quantity.getText().toString());
                    int totalpoints = Integer.parseInt(RequestToRedeemActivity.txtRedeemPoint.getText().toString());
                    Long mpoints = Long.parseLong(value) * quantity;
                    String address = edit_address.getText().toString();
                    String email = edit_email.getText().toString();

                    if (mpoints > Long.parseLong(RequestToRedeemActivity.txtRedeemPoint.getText().toString())) {
                        Toast.makeText(context, "You dont have sufficient balance points", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_submit.setEnabled(false);
                        btn_submit.setClickable(false);
                        redeemRequest(eventid, apikey, productcode, value, String.valueOf(quantity), String.valueOf(mpoints),email,address);
                    }
                }


            }
        });
    }

    public void redeemRequest(String eventid, String token, String product_code, String product_name, String no_of_points, final String mpoints, String email, String address) {
        showProgress();
//        showProgress();
        mAPIService.RedeemRequest(token, eventid, product_code, product_name, no_of_points, email,address).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {

                if (response.isSuccessful()) {
//                    btn_submit.setEnabled(false);
//                    btn_submit.setClickable(false);
                    dismissProgress();
                    Toast.makeText(context, response.body().getHeader().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    Long value = Long.parseLong(RequestToRedeemActivity.txtRedeemPoint.getText().toString())- Long.parseLong(mpoints);
                    RequestToRedeemActivity.txtRedeemPoint.setText(value.toString());
                    myDialog.dismiss();
                   /* if ((Integer.parseInt(response.body().getTotal_available_point())) < 0) {
                        RequestToRedeemActivity.txtRedeemPoint.setText("0");
                    } else {
                        RequestToRedeemActivity.txtRedeemPoint.setText(response.body().getTotal_available_point());
                    }*/
                   /* Intent intent = new Intent(context, RequestToRedeemActivity.class);
                    context.startActivity(intent);*/
                } else {

                    dismissProgress();
                    myDialog.dismiss();


                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Low network or no network");
                myDialog.dismiss();
//                btn_submit.setEnabled(false);
//                btn_submit.setClickable(false);

            }
        });
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progressbar.setVisibility(View.GONE);
    }

}
