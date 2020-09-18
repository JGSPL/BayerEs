package com.procialize.eventapp.ui.login.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.procialize.eventapp.BR;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.validateOTP;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.login.model.Login;
import com.procialize.eventapp.ui.login.view.LoginActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_GCM_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_TOKEN;

public class LoginViewModel extends BaseObservable {
    private Login login;
    Context context;
    ActivityLoginBinding activityLoginBinding;
    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Mobile No. not valid";
    private String termsandconditions = "Please Agreed to the terms and conditions";
    APIService mApiService = ApiUtils.getAPIService();

    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String userEmail;
    public String userPassword;

    public String getUserOTP() {
        return userOTP;
    }

    public void setUserOTP(String userOTP) {
        this.userOTP = userOTP;
    }

    public String userOTP;
    @Bindable
    private String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }


    public void setloginEmail(String userEmail) {
        login.setEmail(userEmail);
        notifyPropertyChanged(BR.loginEmail);
    }

    @Bindable
    public String getloginEmail() {
        return userEmail;
    }

    @Bindable
    public String getloginPassword() {
        return userPassword;
    }


    @Bindable
    public String getOtp() {
        return userOTP;
    }

    public void setloginPassword(String password) {
        login.setPassword(password);
        notifyPropertyChanged(BR.loginPassword);
    }

    public LoginViewModel(Context context) {
        this.context=context;
        login = new Login(getloginEmail(), getloginPassword());
    }

   /* @Bindable
    public boolean isSelected() {
//       if()
    }*/

    public void onLoginClicked() {

        if (isInputDataValid()) {
//            setToastMessage(successMessage);
            userLogin(getloginEmail());
        } else {
            setToastMessage(errorMessage);
        }
    }

    public void onBackClicked() {
       /* activityLoginBinding.linearLoginView.setVisibility(View.VISIBLE);
        activityLoginBinding.linearOTPView.setVisibility(View.GONE);*/
        setToastMessage("back");
    }

    public void onOTPSubmitClicked() {

        otpValidate(getloginEmail(), getOtp());
    }

    public void onResendOTPClicked() {
    }

    public boolean isInputDataValid() {
        if (getloginEmail() == null || getloginEmail().isEmpty())
            return false;
        /*else if (getloginPassword() == null || getloginPassword().isEmpty())
            return false;
        else if (TextUtils.isEmpty(getloginEmail()) || !Patterns.EMAIL_ADDRESS.matcher(getloginEmail()).matches() || getloginPassword().length() < 5)
            return false;*/
        else
            return true;
    }

    private void userLogin(String username) {


        mApiService.LoginWithOrganizer("0", username).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    setToastMessage(response.body().getHeader().get(0).getMsg());
                } else {
                    if(response!=null) {
                        setToastMessage(response.body().getHeader().get(0).getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                setToastMessage(errorMessage);
            }
        });
    }

    private void otpValidate(String username, final String otp) {
        mApiService.validateOTP("0", username, otp).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    setToastMessage(response.body().getHeader().get(0).getMsg());

                    RefreashToken refreashToken = new RefreashToken(context);
                    String data = refreashToken.decryptedData(response.body().getToken().toString().trim());
                    refreashToken.decodeRefreashToken(data);

                    //LoginActivity.sessionManager.storeAuthHeaderkey(response.body().getTokenpreenrypt());
                    HashMap<String,String> map = new HashMap<>();
                    map.put(SharedPreferencesConstant.OTP, otp);
                    map.put(AUTHERISATION_KEY,response.body().getTokenpreenrypt());
                    SharedPreference.putPref(context,map);
                } else {
                    if (response.body() != null) {
                        if (response.body().getHeader().get(0).getType().equalsIgnoreCase("error")) {
                            setToastMessage("Invalid credentials!");
                        } else {
                            setToastMessage(response.body().getHeader().get(0).getMsg());
                        }

                    } else {
                        setToastMessage("Invalid credentials!");
                    }
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {
                setToastMessage(errorMessage);
            }
        });
    }
}
