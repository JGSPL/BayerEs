package com.procialize.bayer2020.ui.login.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.BR;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.GetterSetter.LoginOrgamizerToken;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.GetterSetter.loginTokendetail;
import com.procialize.bayer2020.GetterSetter.resendOTP;
import com.procialize.bayer2020.GetterSetter.validateOTP;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.databinding.ActivityLoginBinding;
import com.procialize.bayer2020.ui.login.model.Login;
import com.procialize.bayer2020.ui.loyalityleap.model.My_point;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;

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
    String vToken;

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
        if(activityLoginBinding.termsCheckBox.isChecked()) {

            if (isInputDataValid()) {
                if (cd.isConnectingToInternet()) {
                    activityLoginBinding.progressBar2.setVisibility(View.VISIBLE);
                    activityLoginBinding.btnSubmit.setClickable(false);
                    userLogin(getloginEmail());
                } else {
                    setToastMessage(interneterror);
                }
            } else {
                setToastMessage(errorMessage1);
            }
        }else{
            setToastMessage("Please check term and condition for continue");

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

    public void PasscodeValidateOTP(String OTP) {
        if (cd.isConnectingToInternet()) {
            activityLoginBinding.btnOTPSubmit.setClickable(false);
            otpValidate(getloginEmail(), OTP);
            activityLoginBinding.progressBar2.setVisibility(View.VISIBLE);

        } else {
            setToastMessage(interneterror);
        }

    }

    public boolean isInputDataValid() {
        if (getloginEmail() == null || getloginEmail().isEmpty())
            return false;
        else if (getloginEmail().length()<10)
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


        mApiService.LoginWithOrganizer("1", username).enqueue(new Callback<LoginOrgamizerToken>() {
            @Override
            public void onResponse(Call<LoginOrgamizerToken> call, Response<LoginOrgamizerToken> response) {
                if (response.isSuccessful()) {

                    String strEventList = response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(context);
                    String data = refreashToken.decryptedData(strEventList);

                    loginTokendetail tokenData = new Gson().fromJson(data, new TypeToken<loginTokendetail>() {
                    }.getType());
                    if(tokenData!=null){
                        vToken = tokenData.getvToken();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(SharedPreferencesConstant.vToken, vToken);
                    }
                    activityLoginBinding.progressBar2.setVisibility(View.GONE);

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
            public void onFailure(Call<LoginOrgamizerToken> call, Throwable t) {
                activityLoginBinding.btnSubmit.setClickable(true);
                setToastMessage(errorMessage);
            }
        });
    }

    private void otpValidate(String username, final String otp) {

        mApiService.validateOTP("1", username, otp,vToken).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    activityLoginBinding.progressBar2.setVisibility(View.GONE);

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

    public void passcodeResendOTP() {
        if (cd.isConnectingToInternet()) {
            activityLoginBinding.btnResendOTP.setClickable(false);
            activityLoginBinding.editOtp.setText("");
            resendOtp(getloginEmail());
        } else {
            setToastMessage(interneterror);
        }
    }

    private void resendOtp(String username) {
        mApiService.ResendOTP("1", username).enqueue(new Callback<resendOTP>() {
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
