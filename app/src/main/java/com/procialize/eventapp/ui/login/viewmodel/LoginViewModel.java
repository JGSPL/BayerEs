package com.procialize.eventapp.ui.login.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.procialize.eventapp.BR;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.resendOTP;
import com.procialize.eventapp.GetterSetter.validateOTP;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.ui.login.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;

public class LoginViewModel extends BaseObservable {
    private Login login;
    Context context;
    ActivityLoginBinding activityLoginBinding;
    private String successMessage = "Login was successful";
    private String errorMessage = "Please enter valid Email ID/Mobile no";
    private String errorMessage1 = "Please enter valid Email ID/Mobile no.";
    private String errorMessage2 = "Please enter valid OTP";
    private String termsandconditions = "Please Agreed to the terms and conditions";
    private String interneterror = "No Internet Connection.";
    APIService mApiService = ApiUtils.getAPIService();
    ConnectionDetector cd;

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

    public LoginViewModel(Context context,ActivityLoginBinding activityLoginBinding) {
        this.context=context;
        this.activityLoginBinding=activityLoginBinding;
        login = new Login(getloginEmail(), getloginPassword());
        cd = ConnectionDetector.getInstance(context);
    }

   /* @Bindable
    public boolean isSelected() {
//       if()
    }*/

    public void onLoginClicked() {

        if (isInputDataValid()) {
            if (cd.isConnectingToInternet()) {
                activityLoginBinding.btnSubmit.setClickable(false);
                userLogin(getloginEmail());
            } else {
                setToastMessage(interneterror);
            }
        } else {
            setToastMessage(errorMessage1);
        }
    }

    public void onBackClicked() {
       /* activityLoginBinding.linearLoginView.setVisibility(View.VISIBLE);
        activityLoginBinding.linearOTPView.setVisibility(View.GONE);*/
        setToastMessage("back");
    }

    public void onTextDesignClicked() {
        setToastMessage("DesignAndDevelopedby");
    }

    public void onOTPSubmitClicked() {
        if (isotpValid()) {
            if (cd.isConnectingToInternet()) {
                activityLoginBinding.btnOTPSubmit.setClickable(false);
                otpValidate(getloginEmail(), getOtp());
            } else {
                setToastMessage(interneterror);
            }
        } else {
            setToastMessage(errorMessage2);
        }

    }

    public void onResendOTPClicked() {
        if (isInputDataValid()) {
            if (cd.isConnectingToInternet()) {
                activityLoginBinding.btnResendOTP.setClickable(false);
                activityLoginBinding.editOtp.setText("");
                resendOtp(getloginEmail());
            } else {
                setToastMessage(interneterror);
            }
        } else {
            setToastMessage(errorMessage1);
        }

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

    public boolean isotpValid() {
        if (getOtp() == null || getOtp().isEmpty())
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
                    activityLoginBinding.btnSubmit.setClickable(true);
                    setToastMessage(response.body().getHeader().get(0).getMsg());
                } else {
                    if (response.body() != null) {
                        activityLoginBinding.btnSubmit.setClickable(true);
                        setToastMessage(response.body().getHeader().get(0).getMsg());
                    } else {
                        activityLoginBinding.btnSubmit.setClickable(true);
                        setToastMessage("Please enter valid Email ID/Mobile no");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                activityLoginBinding.btnSubmit.setClickable(true);
                setToastMessage(errorMessage);
            }
        });
    }

    private void otpValidate(String username, final String otp) {

        mApiService.validateOTP("0", username, otp).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    activityLoginBinding.btnOTPSubmit.setClickable(true);
                    setToastMessage(response.body().getHeader().get(0).getMsg());

                    RefreashToken refreashToken = new RefreashToken(context);
                    String data = refreashToken.decryptedData(response.body().getToken().toString().trim());
                    refreashToken.decodeRefreashToken(data);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(SharedPreferencesConstant.OTP, otp);
                    String authorizationtolen= CommonFunction.stripquotes(data);
                    map.put(AUTHERISATION_KEY, authorizationtolen);
                    SharedPreference.putPref(context, map);
                } else {
                    activityLoginBinding.btnOTPSubmit.setClickable(true);
                    if (response.body() != null) {
                        if (response.body().getHeader().get(0).getType().equalsIgnoreCase("error")) {
                            setToastMessage("Invalid OTP");
                        } else {
                            setToastMessage(response.body().getHeader().get(0).getMsg());
                        }

                    } else {
                        setToastMessage("Invalid OTP");
                    }
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {
                activityLoginBinding.btnOTPSubmit.setClickable(true);
                setToastMessage(errorMessage);
            }
        });
    }

    private void resendOtp(String username) {
        mApiService.ResendOTP("0", username).enqueue(new Callback<resendOTP>() {
            @Override
            public void onResponse(Call<resendOTP> call, Response<resendOTP> response) {
                try {
                    if (response.isSuccessful()) {
                        activityLoginBinding.btnResendOTP.setClickable(true);
                        setToastMessage("OTP sent on register email id/mobile no");
                    } else {
                        activityLoginBinding.btnResendOTP.setClickable(true);
                        if (response.body() != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("error")) {
                                setToastMessage("Invalid credentials!");
                            } else {
                                setToastMessage(response.body().getHeader().get(0).getMsg());
                            }
                        } else if (response.code() == 400) {
                            setToastMessage("User Block for 30 min due to too many attempts");
                        } else {
                            setToastMessage("Please try after some time");
                        }
                    }
                }catch (Exception e){
                    activityLoginBinding.btnResendOTP.setClickable(true);
                    setToastMessage("Please try after some time");
                }

            }

            @Override
            public void onFailure(Call<resendOTP> call, Throwable t) {
                activityLoginBinding.btnResendOTP.setClickable(true);
                setToastMessage(errorMessage);
            }
        });
    }
}
