package com.procialize.eventapp.ui.login.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.validateOTP;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.ui.login.model.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseObservable {
    private Login login;

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

    public LoginViewModel() {
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
                    setToastMessage(response.body().getHeader().get(0).getMsg());
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                setToastMessage(errorMessage);
            }
        });
    }

    private void otpValidate(String username, String otp) {


        mApiService.validateOTP("0", username, otp).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    setToastMessage(response.body().getHeader().get(0).getMsg());

                } else {
                    setToastMessage(response.body().getHeader().get(0).getMsg());
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {
                setToastMessage(errorMessage);
            }
        });
    }


}
